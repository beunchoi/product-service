package com.hanghae.productservice.domain.product.service;

import com.hanghae.productservice.domain.product.entity.Product;
import com.hanghae.productservice.domain.product.event.PaymentSuccessEvent;
import com.hanghae.productservice.domain.product.repository.ProductRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedissonProductService {

  private final RedissonClient redissonClient;
  private final ProductRepository productRepository;

  public void decreaseProductStock(PaymentSuccessEvent event) {
    RLock lock = redissonClient.getLock(event.getProductId());

    try {
      boolean available = lock.tryLock(10, 2, TimeUnit.SECONDS);

      if (!available) {
        log.error("락 획득 실패");
        return;
      }

      Product product = productRepository.findByProductId(event.getProductId())
          .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
//    if (product.getStock() <= event.getQuantity()) {
      if (product.getStock() >= event.getQuantity()) {
        product.updateStock(product.getStock() - event.getQuantity());
        productRepository.save(product);
        log.info("재고 DB 저장 성공");
      } else {
        log.info("재고 부족");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }
}
