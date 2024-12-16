package com.spring1.meliSocial.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.request.PostDto;
import com.spring1.meliSocial.exception.ExistingDataException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.model.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.spring1.meliSocial.dto.request.ProductPromoDto;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.dto.response.PostIndexDto;
import com.spring1.meliSocial.dto.response.PostPromoDto;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IProductRepository;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.validation.UserValidation;
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
    final private ObjectMapper mapper;

    public PostService() {
        this.mapper = new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        this.mapper.registerModule(javaTimeModule);
    }

    @Override
    public void saveNewPost(PostDto postDto) {
        Optional<User> idUser = userRepository.getUserById(postDto.getUserId());
        if(idUser.isEmpty()){
            throw new ExistingDataException("El usuario con id: " + postDto.getUserId() + " no existe");
        }
        Post post = mapper.convertValue(postDto,Post.class);
        saveNewProduct(post);
        repository.saveNewPost(post);
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
            throw new BadRequestException("El id del producto ya existe");

        Post aux = this.mapper.convertValue(productDto, Post.class);
        repository.addNewProductPromo(aux);
    }

    @Override
    public PostIndexDto getPostsByUser(int userId, String order) {
        UserValidation.validateGetPostsByUserParams(userId, order);
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
        Optional<User> optionalUser = userRepository.getUserById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("El usuario con ID: " + userId + " no existe.");
        }

        User user = optionalUser.get();

        if (!user.isSeller()) {
            throw new BadRequestException("El usuario con ID: " + userId + " no es un vendedor.");
        }

        int promoProductsCount = repository.countProductsOnPromo(userId);

        return new PostPromoDto(
                user.getId(),
                user.getUserName(),
                promoProductsCount
        );
    }

    public List<PostDto> getAll(){
        return mapper.convertValue(repository.getPosts(), new TypeReference<List<PostDto>>() {});
    }

    @Override
    public void updatePromoDiscount(int id, double discount) {
        if(!repository.existsById(id))
            throw new NotFoundException("La publicación que quiere modificar no existe");
        repository.updatePromoDiscount(id, discount);
    }



    @Override
    public List<PostDto> getBestProductsOnPromo(Integer category) {
        List<Post> bestProductsOnPromo = repository.getBestProductsOnPromo();

        if (bestProductsOnPromo.isEmpty()) {
            throw new NotFoundException("No hay productos en promo");
        }

        return bestProductsOnPromo.stream()
                .filter(post -> post.getDiscount() > 0)
                .filter(post -> category == null || post.getCategory() == (int) category)
                .sorted((p1, p2) -> Double.compare(p2.getDiscount(), p1.getDiscount()))
                .limit(10)
                .map(post -> this.mapper.convertValue(post, PostDto.class))
                .toList();
    }
}
