package com.sa1zer.botcamp7.repo;

import com.sa1zer.botcamp7.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
