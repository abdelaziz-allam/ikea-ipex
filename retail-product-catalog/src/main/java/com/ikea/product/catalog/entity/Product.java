package com.ikea.product.catalog.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
  @Id
  private UUID id;
  private String name;
  private String category;
  private String description;
  private double price;
  private String imageUrl;

}
