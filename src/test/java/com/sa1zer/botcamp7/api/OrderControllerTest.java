package com.sa1zer.botcamp7.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.entity.OrderStatus;
import com.sa1zer.botcamp7.entity.User;
import com.sa1zer.botcamp7.payload.request.CreateOrderRequest;
import com.sa1zer.botcamp7.payload.request.UpdateOrderRequest;
import com.sa1zer.botcamp7.repo.OrderRepo;
import com.sa1zer.botcamp7.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createOrder() throws Exception {
        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        userRepo.save(user);

        CreateOrderRequest request = new CreateOrderRequest(user.getId(), "firstOrder");

        mockMvc.perform(post("/api/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateOrder() throws Exception {
        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        Order secondOrder = Order.builder()
                .status(OrderStatus.CREATED)
                .user(user)
                .desc("SecondOrder").build();

        user.setOrders(Set.of(
                Order.builder()
                        .status(OrderStatus.CREATED)
                        .user(user)
                        .desc("FirstOrder").build(),
                secondOrder,
                Order.builder()
                        .status(OrderStatus.IN_PROCESSING)
                        .user(user)
                        .desc("ThirdOrder").build()));

        userRepo.save(user);

        UpdateOrderRequest request = new UpdateOrderRequest(OrderStatus.IN_PROCESSING);

        mockMvc.perform(patch(String.format("/api/order/update/%s", secondOrder.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void getOrder() throws Exception {

        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        Order secondOrder = Order.builder()
                .status(OrderStatus.CREATED)
                .user(user)
                .desc("SecondOrder").build();

        user.setOrders(Set.of(
                Order.builder()
                        .status(OrderStatus.CREATED)
                        .user(user)
                        .desc("FirstOrder").build(),
                secondOrder,
                Order.builder()
                        .status(OrderStatus.IN_PROCESSING)
                        .user(user)
                        .desc("ThirdOrder").build()));

        userRepo.save(user);

        mockMvc.perform(get(String.format("/api/order/%s", secondOrder.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getOrderNotFound() throws Exception {
        mockMvc.perform(get(String.format("/api/order/%s", 9999))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleted() throws Exception {
        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        Order secondOrder = Order.builder()
                .status(OrderStatus.CREATED)
                .user(user)
                .desc("SecondOrder").build();

        user.setOrders(Set.of(
                Order.builder()
                        .status(OrderStatus.CREATED)
                        .user(user)
                        .desc("FirstOrder").build(),
                secondOrder,
                Order.builder()
                        .status(OrderStatus.IN_PROCESSING)
                        .user(user)
                        .desc("ThirdOrder").build()));

        userRepo.save(user);

        UpdateOrderRequest request = new UpdateOrderRequest(OrderStatus.IN_PROCESSING);

        mockMvc.perform(delete(String.format("/api/order/delete/%s", secondOrder.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}