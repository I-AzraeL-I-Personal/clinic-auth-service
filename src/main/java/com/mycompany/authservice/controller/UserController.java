package com.mycompany.authservice.controller;

import com.mycompany.authservice.dto.*;
import com.mycompany.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/pending")
    public ResponseEntity<List<UserDto>> getNotEnabledUsers() {
        return ResponseEntity.ok(userService.getAllNotEnabledUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginRequestDto));
    }

    @PutMapping("/users/{uuid}")
    public ResponseEntity<UserDto> editUser(@PathVariable UUID uuid, @Valid @RequestBody EditUserDto editUserDTO) {
        return ResponseEntity.ok(userService.update(editUserDTO, uuid));
    }

    @PatchMapping("/users/{uuid}")
    public ResponseEntity<Void> enableUser(@PathVariable UUID uuid) {
        userService.enableUser(uuid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        userService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
