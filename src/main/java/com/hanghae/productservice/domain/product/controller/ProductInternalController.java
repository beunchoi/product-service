package com.hanghae.productservice.domain.product.controller;

import com.hanghae.productservice.domain.product.dto.ProductResponseDto;
import com.hanghae.productservice.domain.product.event.PaymentSuccessEvent;
import com.hanghae.productservice.domain.product.service.ProductService;
import com.hanghae.productservice.domain.product.service.ProductServiceImpl;
import com.hanghae.productservice.domain.product.service.RedissonProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/product")
@RequiredArgsConstructor
public class ProductInternalController {

  private final ProductService productService;
  private final RedissonProductService redissonProductService;

  @PutMapping("/{productId}/incr/{quantity}")
  public ResponseEntity<ProductResponseDto> increaseProductStock(
      @PathVariable("productId") String productId,
      @PathVariable("quantity") int quantity) {
    ProductResponseDto response = productService.increaseProductStock(quantity, productId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductResponseDto> getProductByProductId(@PathVariable("productId") String productId) {
    ProductResponseDto response = productService.getProductByProductId(productId);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /*
  * 테스트
  */

  @PostMapping("/decr")
  public ResponseEntity<?> decreaseProductStock(@RequestBody PaymentSuccessEvent event) {
    redissonProductService.decreaseProductStock(event);
    return ResponseEntity.status(200).build();
  }

}
