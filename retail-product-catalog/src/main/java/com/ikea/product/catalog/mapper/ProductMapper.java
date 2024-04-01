package com.ikea.product.catalog.mapper;

import com.ikea.product.catalog.dto.ProductDTO;
import com.ikea.product.catalog.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

  ProductDTO map(Product product);
  Product map(ProductDTO dto);

  ProductDTO map(@MappingTarget ProductDTO dto, Product product);

  List<Product> map(List<ProductDTO> dtos);
  List<ProductDTO> map(Page<Product> products);

  List<ProductDTO> mapList(List<Product> products);

}
