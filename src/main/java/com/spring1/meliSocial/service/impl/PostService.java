package com.spring1.meliSocial.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.exception.ExistingDataException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.spring1.meliSocial.dto.ProductPromoDto;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.dto.PostIndexDto;
import com.spring1.meliSocial.dto.PostPromoDto;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IProductRepository;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;


@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository repository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public String saveNewPost(PostDto postDto) {
        Optional<User> idUser = userRepository.getUserById(postDto.getUserId());
        if(!idUser.isPresent()){
            throw new ExistingDataException("el usuario con id: " + postDto.getUserId() + " no existe");
        }
        Post post = mapper.convertValue(postDto,Post.class);
        saveNewProduct(post);
        return repository.saveNewPost(post);
    }

    public void saveNewProduct(Post post){
        Optional<Product> idProduct= productRepository.findId(post.getProduct().getId());
        if(idProduct.isPresent()){
            throw new ExistingDataException("El producto con el id " + post.getProduct().getId() + " ya existe");
        }

        List<Product> productList = productRepository.getProducts();
        productList.add(post.getProduct());
    }


    @Override
    public void addNewProductPromo(ProductPromoDto productDto) {
        if(repository.findById(productDto.getId()))
            throw new BadRequestException("el id del producto ya existe");

        ObjectMapper objectMapper = new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // Registrar el módulo en el ObjectMapper
        objectMapper.registerModule(javaTimeModule);

        Post aux = objectMapper.convertValue(productDto, Post.class);
        repository.addNewProductPromo(aux);
    }

    @Override
    public PostIndexDto getPostsByUser(int userId, String order) {
        Optional<User> user = userRepository.getUserById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("No se encontro el usuario.");
        }

        List<Integer> followedIds = user.get().getFollowed();

        Comparator<Post> comparator = (order.equalsIgnoreCase("date_asc"))
                ? Comparator.comparing(Post::getDate)
                : Comparator.comparing(Post::getDate).reversed();

        List<Post> filteredPosts = repository.getPosts().stream()
                .filter(p -> followedIds.contains(p.getUserId()))
                .filter(p -> p.getDate() != null && ChronoUnit.DAYS.between(p.getDate(), LocalDate.now()) <= 14)
                .sorted(comparator)
                .toList();

        return new PostIndexDto(userId, filteredPosts.stream()
                .map(fp -> mapper.convertValue(fp, PostDto.class))
                .toList());
    }

    @Override
    public PostPromoDto getProductsOnPromo(int userId) {
        Optional<User> userO = userRepository.getUserById(userId);

        if (userO.isEmpty()) {
            throw new BadRequestException("El usuario con ese ID no existe.");
        }

        User user = userO.get();

        int promoProductsCount = repository.countProductsOnPromo(userId);

        return new PostPromoDto(
                user.getId(),
                user.getUserName(),
                promoProductsCount
        );
    }
}
