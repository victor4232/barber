
package com.cesul.barber.services;

import com.cesul.barber.dtos.UserCreationDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersService {

    private List<UserCreationDTO> users = new ArrayList<>();

    public UserCreationDTO create(UserCreationDTO user) {
        user.setId(UUID.randomUUID());
        this.users.add(user);

        return user;
    }

    public List<UserCreationDTO> findAll() {
        return this.users;
    }

    public Optional<UserCreationDTO> findById(UUID id) {
        return this.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<UserCreationDTO> update(UUID id, Map<String, String> request) {
        Optional<UserCreationDTO> userOptional = findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserCreationDTO existingUser = userOptional.get();

        if (request.containsKey("name")) {
            existingUser.setName(request.get("name"));
        }

        if (request.containsKey("email")) {
            existingUser.setEmail(request.get("email"));
        }

        return Optional.of(existingUser);
    }

    public Optional<UserCreationDTO> updateAvatar(UUID id, Map<String, String> request) {
        Optional<UserCreationDTO> userOptional = findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserCreationDTO existingUser = userOptional.get();

        if (request.containsKey("avatarUrl")) {
            existingUser.setAvatarUrl(request.get("avatarUrl"));
        }

        return Optional.of(existingUser);
    }

    public boolean delete(UUID id) {
        boolean removed = this.users.removeIf(u -> u.getId().equals(id));

        return removed;
    }

    public boolean clear() {
        this.users.clear();

        return true;
    }
}
