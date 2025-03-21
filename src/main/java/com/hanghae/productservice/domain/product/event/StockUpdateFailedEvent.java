package com.hanghae.productservice.domain.product.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateFailedEvent {
  private String productId;
  private String orderId;
  private Integer quantity;
}
