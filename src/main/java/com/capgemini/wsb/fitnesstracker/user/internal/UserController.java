package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of basic user information.
     * Fetches all user records from the database and maps them to a list of UserBasicDto objects.
     *
     * @return A list of UserBasicDto objects containing basic user information.
     */
    @GetMapping("/basic-info")
    public List<UserBasicDto> getAllUsersBasicInfo() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toBasicInfoDto)
                .toList();
    }

    /**
     * Retrieves user information based on the provided email address.
     * If a user with the specified email is not found, an exception is thrown.
     *
     * @param email The email address of the user to retrieve.
     * @return UserDto object containing user information if found.
     * @throws UserNotFoundException if no user with the specified email exists.
     */
    @GetMapping("/email/{email}")
    public UserDto getUserInfoByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.getUserByEmail(email);
        return userOptional.map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    /**
     * Adds a new user to the system. If the email address is already registered, an exception is thrown.
     * Logs the incoming user email and converts UserDto to User entity before saving.
     *
     * @param userDto UserDto object containing user details.
     * @return UserDto object with details of the newly created user.
     * @throws UserAlreadyExistsException if a user with the specified email already exists.
     * @throws IllegalArgumentException if there is an error during user creation.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("User with e-mail {} passed to the request", userDto.email());

        if (userService.getUserByEmail(userDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userDto.email() + " already exists.");
        }

        try {
            User user = userMapper.toEntity(userDto);
            userService.createUser(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Problem while creating user with email " + userDto.email() + ". " + e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a user by their ID.
     * Logs the user ID to be deleted, and throws an exception if there is an error.
     *
     * @param userId The ID of the user to delete.
     * @throws IllegalArgumentException if there is an issue during user deletion.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Request to delete user with ID: {}", userId);

        try {
            userService.deleteUser(userId);
        } catch ( Exception e) {
            throw new IllegalArgumentException("Problem while deleting user with ID " + userId + ". " + e.getMessage());
        }
    }


    /**
     * Retrieves a list of users older than the specified age.
     * Filters users by age and returns only those who are older than the provided age parameter.
     *
     * @param age The age threshold to filter users.
     * @return A list of UserDto objects for users older than the specified age.
     */
    @GetMapping("/older-than/{age}")
    public List<UserDto> getUsersOlderThan(@PathVariable int age) {
        return userService.getUsersOlderThan(age)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Updates a user's information based on their email address.
     * Updates the fields provided in the UserDto. Logs an exception if user is not found or update fails.
     *
     * @param userEmail The email address of the user to update.
     * @param userDto   The UserDto object containing updated user information.
     * @return The updated User object.
     * @throws IllegalArgumentException if the user is not found or if there is an error during update.
     */
    @PutMapping("/{userEmail}")
    public User updateUser(@PathVariable String userEmail, @RequestBody UserDto userDto) {
        try {
            User foundUser = userService.getUserByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("No accound registered using email: " + userEmail + "."));
            User updatedUser = userMapper.toUpdateEntity(userDto, foundUser);

            return userService.updateUser(updatedUser);
        } catch (Exception e) {
            throw new IllegalArgumentException("Problem while updating user with email: " + userEmail + ". " + e.getMessage());
        }
    }
}