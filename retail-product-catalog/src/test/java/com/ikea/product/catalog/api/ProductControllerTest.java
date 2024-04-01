package com.ikea.product.catalog.api;

import com.ikea.product.catalog.api.helper.APIHelperUtil;
import com.ikea.product.catalog.dto.APIResponse;
import com.ikea.product.catalog.dto.ProductDTO;
import com.ikea.product.catalog.dto.ProductDTOBuilder;
import com.ikea.product.catalog.entity.Product;
import com.ikea.product.catalog.repo.ProductRepository;
import com.ikea.product.catalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


import static com.ikea.product.catalog.constant.AppConstants.APIs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
class ProductControllerTest {
  @Mock
  private ProductService productService;
  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private ProductController productController;


  @Test
  void testAddProduct() {
    ProductDTO request = ProductDTOBuilder.builder()
            .category("CAT A")
            .name("Product A")
            .description("DESC A")
            .price(3.25)
            .imageUrl("image.png")
            .build();
    ProductDTO response = ProductDTOBuilder.builder()
            .id(UUID.randomUUID())
            .category("CAT A")
            .name("Product A")
            .description("DESC A")
            .price(3.25)
            .imageUrl("image.png")
            .build();
    when(productService.addProduct(request))
            .thenReturn(new APIHelperUtil<ProductDTO>().createUnifiedResponse(response, HttpStatus.CREATED,"product created successfully",PRODUCT_CATALOG_200_1));

    ResponseEntity<APIResponse<ProductDTO>> actualResponse = productController.addProduct(request);

    verify(productService, times(1)).addProduct(request);
    assertEquals(response.name(), Objects.requireNonNull(actualResponse.getBody()).result().name());
  }

  @Test
  void testGetAllProducts() {
    int offset = 0;
    int pageSize = 5;
    List<ProductDTO> response = List.of(
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product A").category("Category A").description("Description of Product A").price(10.99).imageUrl("image_url_1.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product B").category("Category B").description("Description of Product B").price(20.99).imageUrl("image_url_2.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product C").category("Category C").description("Description of Product C").price(30.99).imageUrl("image_url_3.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product D").category("Category D").description("Description of Product D").price(40.99).imageUrl("image_url_4.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product E").category("Category E").description("Description of Product E").price(50.99).imageUrl("image_url_5.jpg").build()
    );
    when(productService.getAllProducts(offset, pageSize))
            .thenReturn(new APIHelperUtil<List<ProductDTO>>().createUnifiedResponse(response,HttpStatus.OK,"All products loaded successfully",PRODUCT_CATALOG_200_2));

    ResponseEntity<APIResponse<List<ProductDTO>>> expected = productController.getAllProducts(offset, pageSize);

    verify(productService, times(1)).getAllProducts(offset, pageSize);
    assertEquals(Objects.requireNonNull(expected.getBody()).result().size(), response.size());
  }

  @Test
  void testSearchProducts() {
    String term = "duct";
    List<Product> products = List.of(
            new Product(UUID.randomUUID(), "Product A", "Category A", "Description of Product A", 10.99, "image_url_a.jpg"),
            new Product(UUID.randomUUID(), "Product B", "Category B", "Description of Product B", 20.99, "image_url_b.jpg"),
            new Product(UUID.randomUUID(), "Product C", "Category C", "Description of Product C", 30.99, "image_url_c.jpg"),
            new Product(UUID.randomUUID(), "Product D", "Category D", "Description of Product D", 40.99, "image_url_d.jpg"),
            new Product(UUID.randomUUID(), "Product E", "Category E", "Description of Product E", 930.99, "image_url_e.jpg")
    );
    List<ProductDTO> dto = List.of(
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product A").category("Category A").description("Description of Product A").price(10.99).imageUrl("image_url_1.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product B").category("Category B").description("Description of Product B").price(20.99).imageUrl("image_url_2.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product C").category("Category C").description("Description of Product C").price(30.99).imageUrl("image_url_3.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product D").category("Category D").description("Description of Product D").price(40.99).imageUrl("image_url_4.jpg").build(),
            ProductDTOBuilder.builder().id(UUID.randomUUID()).name("Product E").category("Category E").description("Description of Product E").price(50.99).imageUrl("image_url_5.jpg").build()
    );
    when(productRepository.findAll()).thenReturn(products);

    when(productService.search(term)).thenReturn(new APIHelperUtil<List<ProductDTO>>().createUnifiedResponse(dto,HttpStatus.OK,null,PRODUCT_CATALOG_200_4));

    ResponseEntity<APIResponse<List<ProductDTO>>> response = productController.searchProducts(term);

    verify(productService, times(1)).search(term);
    assertEquals(Objects.requireNonNull(response.getBody()).result().size(), dto.size());
  }

  @Test
  void testGetProductById() {
    UUID id = UUID.randomUUID();
    ProductDTO dto = ProductDTOBuilder.builder()
            .id(id)
            .category("CAT A")
            .name("Product A")
            .description("DESC A")
            .price(3.25)
            .imageUrl("image.png")
            .build();
    when(productService.getProduct(id))
            .thenReturn(new APIHelperUtil<ProductDTO>().createUnifiedResponse(dto, HttpStatus.CREATED,"product created successfully",PRODUCT_CATALOG_200_3));

    ResponseEntity<APIResponse<ProductDTO>> response = productController.getProductById(id);

    verify(productService, times(1)).getProduct(id);
    assertEquals(Objects.requireNonNull(response.getBody()).result(), dto);
  }
}