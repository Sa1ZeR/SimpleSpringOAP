package com.sa1zer.botcamp7.service;

import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.exeption.EntityNotFoundException;
import com.sa1zer.botcamp7.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order with %s not found", id)));
    }

    @Transactional
    public Order save(Order order) {
        return orderRepo.save(order);
    }

    @Transactional
    public void delete(Order order) {
        orderRepo.delete(order);
    }

    @Transactional
    public void deleteById(Long id) {
        orderRepo.deleteById(id);
    }
}
