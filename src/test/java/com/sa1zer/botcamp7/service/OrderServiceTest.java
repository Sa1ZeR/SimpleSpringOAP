package com.sa1zer.botcamp7.service;

import com.sa1zer.botcamp7.exeption.EntityNotFoundException;
import com.sa1zer.botcamp7.repo.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderRepo orderRepo;
    @Autowired
    OrderService orderService;

    @BeforeEach
    void init() {
        orderRepo.deleteAll();
    }

    @Test
    void findById() {
        assertThrows(EntityNotFoundException.class, () -> orderService.findById(1L));
    }
}