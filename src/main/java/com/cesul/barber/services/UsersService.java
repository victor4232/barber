package com.cesul.barber.services;

import com.cesul.barber.dtos.UserCreationDTO;
import com.cesul.barber.entity.User;
import com.cesul.barber.mappers.UserMapper;
import com.cesul.barber.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UsersService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserCreationDTO create(UserCreationDTO user) {
        validateName(user.getName());
        validateEmail(user.getEmail());

        User entity = this.userMapper.toEntity(user);
        entity.setId(UUID.randomUUID());
        this.userRepository.save(entity);

        return this.userMapper.toDTO(entity);
    }

    public List<UserCreationDTO> findAll() {
        return this.userMapper.toDTOList(this.userRepository.findAll());
    }

    public Optional<UserCreationDTO> findById(UUID id) {
        return this.userRepository.findById(id).map(this.userMapper::toDTO);
    }

    public Optional<UserCreationDTO> update(UUID id, Map<String, String> request) {
        Optional<User> userOptional = this.userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User existingUser = userOptional.get();

        if (request.containsKey("name")) {
            validateName(request.get("name"));
            existingUser.setName(request.get("name"));
        }

        if (request.containsKey("email")) {
            validateEmail(request.get("email"));
            existingUser.setEmail(request.get("email"));
        }

        if (request.containsKey("avatarUrl")) {
            existingUser.setAvatarUrl(request.get("avatarUrl"));
        }

        this.userRepository.save(existingUser);

        return Optional.of(this.userMapper.toDTO(existingUser));
    }

    public Optional<UserCreationDTO> updateAvatar(UUID id, Map<String, String> request) {
        Optional<User> userOptional = this.userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User existingUser = userOptional.get();

        if (request.containsKey("avatarUrl")) {
            existingUser.setAvatarUrl(request.get("avatarUrl"));
            this.userRepository.save(existingUser);
        }

        return Optional.of(this.userMapper.toDTO(existingUser));
    }

    public boolean delete(UUID id) {
        return this.userRepository.deleteById(id);
    }

    public boolean clear() {
        this.userRepository.deleteAll();

        return true;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome é obrigatório.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O e-mail é obrigatório e deve ser válido.");
        }
    }
}