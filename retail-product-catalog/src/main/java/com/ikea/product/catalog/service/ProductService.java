package com.ikea.product.catalog.service;

import com.ikea.product.catalog.api.helper.APIHelperUtil;
import com.ikea.product.catalog.dto.APIResponse;
import com.ikea.product.catalog.dto.ProductDTO;
import com.ikea.product.catalog.entity.Product;
import com.ikea.product.catalog.mapper.ProductMapper;
import com.ikea.product.catalog.repo.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ikea.product.catalog.constant.AppConstants.APIs.*;


@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final FuzzySearch fuzzySearch;
  private final ProductMapper mapper;

  @Value("${fuzzy.search.threshold}")
  private int threshold;

  public ProductService(ProductRepository productRepository, FuzzySearch fuzzySearch, ProductMapper mapper) {
    this.productRepository = productRepository;
    this.fuzzySearch = fuzzySearch;
    this.mapper = mapper;
  }


  @PostConstruct
  public void init() {
    productRepository.saveAll(List.of(
      new Product(UUID.randomUUID(), "Product A", "Category A", "Description of Product A", 10.99, "https://www.ikea.com/se/en/images/products/groenby-picture-set-of-9-blue-landscape__0671717_pe716332_s5.jpg?f=xl%20900w"),
      new Product(UUID.randomUUID(), "Product B", "Category B", "Description of Product B", 20.99, "https://www.ikea.com/se/en/images/products/groenby-picture-set-of-9-blue-landscape__0671717_pe716332_s5.jpg?f=xl%20900w"),
      new Product(UUID.randomUUID(), "Product C", "Category C", "Description of Product C", 30.99, "https://www.ikea.com/se/en/images/products/groenby-picture-set-of-9-blue-landscape__0671717_pe716332_s5.jpg?f=xl%20900w"),
      new Product(UUID.randomUUID(), "Product D", "Category D", "Description of Product D", 40.99, "https://www.ikea.com/se/en/images/products/groenby-picture-set-of-9-blue-landscape__0671717_pe716332_s5.jpg?f=xl%20900w"),
      new Product(UUID.randomUUID(), "Product E", "Category E", "Description of Product E", 930.99, "https://www.ikea.com/se/en/images/products/groenby-picture-set-of-9-blue-landscape__0671717_pe716332_s5.jpg?f=xl%20900w")
    ));
  }

  public ResponseEntity<APIResponse<ProductDTO>> addProduct(ProductDTO dto) {
    Product product = mapper.map(dto);
    product.setId(UUID.randomUUID());
    Product result = productRepository.save(product);
    ProductDTO response = mapper.map(result);
    return new APIHelperUtil<ProductDTO>().createUnifiedResponse(response, HttpStatus.CREATED,"product created successfully",PRODUCT_CATALOG_200_1);
  }

  public ResponseEntity<APIResponse<List<ProductDTO>>> getAllProducts(int offset, int pageSize) {
    Page<Product> result = productRepository.findAll(PageRequest.of(offset,pageSize));
    List<ProductDTO> response = mapper.map(result);
    return new APIHelperUtil<List<ProductDTO>>().createUnifiedResponse(response,HttpStatus.OK,"All products loaded successfully",PRODUCT_CATALOG_200_2);
  }

  public ResponseEntity<APIResponse<ProductDTO>>  getProduct(UUID id) {
    return productRepository.findById(id)
            .map(record -> {
              ProductDTO response = mapper.map(record);
              return new APIHelperUtil<ProductDTO>().createUnifiedResponse(response, HttpStatus.CREATED,"product created successfully",PRODUCT_CATALOG_200_3);
            }).orElseThrow(() -> new EntityNotFoundException("Record Not Found"));

  }

  public ResponseEntity<APIResponse<List<ProductDTO>>> search(String term) {
    List<Product> products = productRepository.findAll();
    List<Product> searchResult = fuzzySearch.search(term, products, threshold);
    List<ProductDTO> result = mapper.mapList(searchResult);
    return new APIHelperUtil<List<ProductDTO>>().createUnifiedResponse(result,HttpStatus.OK,null,PRODUCT_CATALOG_200_4);
  }
}
