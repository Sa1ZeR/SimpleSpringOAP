package com.sa1zer.botcamp7.service;

import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.entity.User;
import com.sa1zer.botcamp7.exeption.EntityNotFoundException;
import com.sa1zer.botcamp7.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with %s not found", id)));
    }

    @Transactional
    public User save(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }
}
