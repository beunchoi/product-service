package com.hanghae.productservice.domain.product.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {

  private String title;
  private Integer price;
  private String category;
  private String description;
  private Integer stock;
}
