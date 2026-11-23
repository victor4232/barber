package com.cesul.barber.mappers;

import com.cesul.barber.dtos.UserCreationDTO;
import com.cesul.barber.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public User toEntity(UserCreationDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAvatarUrl(dto.getAvatarUrl());

        return user;
    }

    public UserCreationDTO toDTO(User user) {
        UserCreationDTO dto = new UserCreationDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());

        return dto;
    }

    public List<UserCreationDTO> toDTOList(List<User> users) {
        return users.stream().map(this::toDTO).toList();
    }
}
