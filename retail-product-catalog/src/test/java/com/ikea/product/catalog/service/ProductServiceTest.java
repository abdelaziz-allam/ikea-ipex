package com.ikea.product.catalog.service;

import com.ikea.product.catalog.dto.APIResponse;
import com.ikea.product.catalog.dto.ProductDTO;
import com.ikea.product.catalog.dto.ProductDTOBuilder;
import com.ikea.product.catalog.dto.ResponseStatus;
import com.ikea.product.catalog.entity.Product;
import com.ikea.product.catalog.mapper.ProductMapper;
import com.ikea.product.catalog.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper mapper;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        ProductDTO dto = ProductDTOBuilder.builder()
                .category("Test Category")
                .description("Test Description")
                .imageUrl("test_image.jpg")
                .price(10.0)
                .name("Test Product")
                .build();
        Product product = new Product(UUID.randomUUID(), "Test Product", "Test Category", "Test Description", 10.0, "test_image.jpg");
        when(productRepository.save(any())).thenReturn(product);
        when(mapper.map(dto)).thenReturn(product);

        ResponseEntity<APIResponse<ProductDTO>> response = productService.addProduct(dto);

        assertNotNull(response);
        assertEquals(ResponseStatus.SUCCESS, Objects.requireNonNull(response.getBody()).status());
        assertEquals("product created successfully", response.getBody().message());
    }

    @Test
    void testGetAllProducts() {
        int offset = 0;
        int pageSize = 5;
        List<Product> productList = List.of(
                new Product(UUID.randomUUID(), "Product A", "Category A", "Description A", 10.0, "image_url_a.jpg"),
                new Product(UUID.randomUUID(), "Product B", "Category B", "Description B", 20.0, "image_url_b.jpg")
        );
        Page<Product> page = new PageImpl<>(productList);
        List<ProductDTO> dtos = List.of(
                ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product A").category("Category A").description("Description of Product A").price(10.99).imageUrl("image_url_1.jpg").build(),
                ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product B").category("Category B").description("Description of Product B").price(20.99).imageUrl("image_url_2.jpg").build()
        );

        when(productRepository.findAll(PageRequest.of(offset,pageSize))).thenReturn(page);
        when(mapper.map(page)).thenReturn(dtos);

        ResponseEntity<APIResponse<List<ProductDTO>>> response = productService.getAllProducts(offset, pageSize);

        assertNotNull(dtos);
        assertEquals(ResponseStatus.SUCCESS, Objects.requireNonNull(response.getBody()).status());
        assertEquals("All products loaded successfully", response.getBody().message());
        assertEquals(dtos.size(), response.getBody().result().size());
    }

    @Test
    void testGetProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Product A", "Category A", "Description of Product A", 10.99, "image_url_1.jpg");
        ProductDTO dto = ProductDTOBuilder.builder().id(id).name("Product A").category("Category A").description("Description of Product A").price(10.99).imageUrl("image_url_1.jpg").build();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        when( mapper.map(product)).thenReturn(dto);
        ResponseEntity<APIResponse<ProductDTO>> response = productService.getProduct(id);

        assertNotNull(response);
        assertEquals(ResponseStatus.SUCCESS, Objects.requireNonNull(response.getBody()).status());
        assertEquals("product created successfully", response.getBody().message());
    }

}
