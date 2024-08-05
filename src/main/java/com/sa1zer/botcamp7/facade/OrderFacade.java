package com.sa1zer.botcamp7.facade;

import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.entity.OrderStatus;
import com.sa1zer.botcamp7.entity.User;
import com.sa1zer.botcamp7.mapper.OrderMapper;
import com.sa1zer.botcamp7.payload.dto.OrderDto;
import com.sa1zer.botcamp7.payload.request.CreateOrderRequest;
import com.sa1zer.botcamp7.payload.request.UpdateOrderRequest;
import com.sa1zer.botcamp7.service.OrderService;
import com.sa1zer.botcamp7.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        User user = userService.findById(request.userId());

        Order order = Order.builder()
                .desc(request.desc())
                .status(OrderStatus.CREATED)
                .build();

        order = orderService.save(order);
        user.getOrders().add(order);

        return orderMapper.map(order);
    }

    public OrderDto updateOrder(Long id, UpdateOrderRequest request) {
        Order order = orderService.findById(id);

        order.setStatus(request.status());

        return orderMapper.map(orderService.save(order));
    }

    public OrderDto findOrder(Long id) {
        return orderMapper.map(orderService.findById(id));
    }

    @Transactional
    public String delete(Long id) {
        orderService.deleteById(id);
        return String.format("Order with id %s successfully deleted", id);
    }
}
