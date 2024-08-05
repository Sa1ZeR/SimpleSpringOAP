package com.sa1zer.botcamp7.facade;

import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.entity.User;
import com.sa1zer.botcamp7.mapper.OrderMapper;
import com.sa1zer.botcamp7.payload.dto.OrderDto;
import com.sa1zer.botcamp7.payload.request.CreateUserRequest;
import com.sa1zer.botcamp7.payload.request.UpdateUserRequest;
import com.sa1zer.botcamp7.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final OrderMapper orderMapper;

    @Transactional
    public User createUser(CreateUserRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .build();

        return userService.save(user);
    }

    @Transactional
    public User updateUser(UpdateUserRequest request) {
        User user = userService.findById(request.id());

        user.setEmail(request.email());
        user.setName(request.name());

        return userService.save(user);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllOrders(Long id) {
        return userService.findById(id).getOrders().stream()
                .map(orderMapper::map).toList();
    }

    @Transactional
    public String delete(Long id) {
        userService.deleteById(id);
        return String.format("User with id %s successfully deleted", id);
    }
}
