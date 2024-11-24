package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserSimpleDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class UserMapper {

    UserDto toDto(User user) {
        return new UserDto(user.getId(),
                           user.getFirstName(),
                           user.getLastName(),
                           user.getBirthdate(),
                           user.getEmail());
    }

    UserBasicDto toBasicInfoDto(User user) {
        String fullName = user.getFirstName() + "_" + user.getLastName();
        return new UserBasicDto(user.getId(), fullName);
    }


    UserSimpleDto toSimpleDto(User user) {
        return new UserSimpleDto(user.getId(),
                user.getFirstName(),
                user.getLastName());
    }
    User toEntity(UserDto userDto) {
        return new User(
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.birthdate(),
                        userDto.email());
    }

    User toUpdateEntity(UserDto userDto, User user) {
        if(userDto.firstName() != null) {
            user.setFirstName(userDto.firstName());
        }

        if(userDto.lastName() != null) {
            user.setLastName(userDto.lastName());
        }

        if(userDto.birthdate() != null) {
            user.setBirthdate(userDto.birthdate());
        }

        if(userDto.email() != null) {
            user.setEmail(userDto.email());
        }

        return user;
    }
}
