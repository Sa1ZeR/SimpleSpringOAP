package com.sa1zer.botcamp7.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.entity.OrderStatus;
import com.sa1zer.botcamp7.entity.User;
import com.sa1zer.botcamp7.payload.request.CreateUserRequest;
import com.sa1zer.botcamp7.payload.request.UpdateUserRequest;
import com.sa1zer.botcamp7.repo.OrderRepo;
import com.sa1zer.botcamp7.repo.UserRepo;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        userRepo.deleteAll();
        orderRepo.deleteAll();
    }


    @Test
    void createUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Sergey", "testmail@mail.ru");
        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createIncorrectUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "testmailmail.ru");
        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateUser() throws Exception {
        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        userRepo.save(user);

        UpdateUserRequest request = new UpdateUserRequest(user.getId(), "Sergey", "newtestmail@mail.ru");

        mockMvc.perform(patch("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateNotFoundUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest(9999L, "Sergey", "newtestmail@mail.ru");

        mockMvc.perform(patch("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void orders() throws Exception {
        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        user.setOrders(Set.of(
                Order.builder()
                        .status(OrderStatus.CREATED)
                        .user(user)
                        .desc("FirstOrder").build(),
                Order.builder()
                        .status(OrderStatus.CREATED)
                        .user(user)
                        .desc("SecondOrder").build(),
                Order.builder()
                        .status(OrderStatus.IN_PROCESSING)
                        .user(user)
                        .desc("ThirdOrder").build()));

        userRepo.save(user);

        mockMvc.perform(get(String.format("/api/user/%s/orders", user.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteUser() throws Exception {
        User user = User.builder()
                .email("testmail@mail.ru")
                .name("Sergey")
                .build();

        userRepo.save(user);

        mockMvc.perform(delete(String.format("/api/user/delete/%s", user.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}