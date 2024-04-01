package com.ikea.product.catalog.api;


import com.ikea.product.catalog.dto.APIResponse;
import com.ikea.product.catalog.dto.ProductDTO;
import com.ikea.product.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Add Product",description = "Add New Product To Catalog", responses = {
          @ApiResponse(responseCode = "201",description = "Add Product")
  })
  @PostMapping
  public ResponseEntity<APIResponse<ProductDTO>> addProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.addProduct(productDTO);
  }


  @Operation(summary = "Get All Products",description = "Get All Products", responses = {
          @ApiResponse(responseCode = "200",description = "Get All Products")
  })
  @GetMapping
  public ResponseEntity<APIResponse<List<ProductDTO>>> getAllProducts(@NotNull @RequestParam int offset, @NotNull @RequestParam int pageSize) {
    return productService.getAllProducts(offset,pageSize);
  }

  @Operation(summary = "Search All Products",description = "Search All Products", responses = {
          @ApiResponse(responseCode = "200",description = "Search All Products")
  })
  @GetMapping("/search")
  public ResponseEntity<APIResponse<List<ProductDTO>>> searchProducts(@RequestParam String term) {
    return productService.search(term);
  }

  @Operation(summary = "Get Product By Id",description = "Get Product By Id", responses = {
          @ApiResponse(responseCode = "200",description = "Get Product By Id")
  })
  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<ProductDTO>>  getProductById(@PathVariable UUID id) {
    return productService.getProduct(id);
  }

}
