package com.mycompany.authservice.service;

import com.mycompany.authservice.dto.*;
import com.mycompany.authservice.exception.DataNotFoundException;
import com.mycompany.authservice.exception.RoleNotAllowedException;
import com.mycompany.authservice.model.User;
import com.mycompany.authservice.repository.UserRepository;
import com.mycompany.authservice.security.JwtUtils;
import com.mycompany.authservice.security.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mycompany.authservice.security.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional(readOnly = true)
    public List<UserDto> getAllNotEnabledUsers() {
        var userDtoListType = new TypeToken<List<UserDto>>() {}.getType();
        List<UserDto> usersDto = modelMapper.map(userRepository.findAllByIsEnabledIsFalse(), userDtoListType);
        usersDto.forEach(user -> user.setRole(user.getRole().toLowerCase()));
        return usersDto;
    }

    @Transactional
    public UserResponseDto register(RegisterRequestDto registerRequestDTO) {
        if (registerRequestDTO.getRole().equals(Role.ADMIN))
            throw new RoleNotAllowedException(registerRequestDTO.getRole().name(), "Role not allowed");
        var user = modelMapper.map(registerRequestDTO, User.class);
        user.setUserUUID(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);

        return createAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var user = (User) authentication.getPrincipal();

        return createAuthResponse(user);
    }

    @Transactional
    public void enableUser(UUID uuid) {
        User user = userRepository.findByUserUUID(uuid)
                .orElseThrow(() -> new DataNotFoundException(uuid.toString(), "User not found"));
        user.setEnabled(true);
    }

    @Transactional
    public UserDto update(EditUserDto editUserDTO, UUID uuid) {
        var user = userRepository.findByUserUUID(uuid)
                .orElseThrow(() -> new DataNotFoundException(uuid.toString(), "User not found"));
        user.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));
        user.setEmail(editUserDTO.getEmail());

        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Transactional
    public void delete(UUID userUUID) {
        userRepository.deleteByUserUUID(userUUID);
    }

    private UserResponseDto createAuthResponse(User user) {
        var userResponseDto = modelMapper.map(user, UserResponseDto.class);
        String token = jwtUtils.generateJwt(user);
        userResponseDto.setToken(TOKEN_PREFIX + token);
        userResponseDto.setRole(userResponseDto.getRole().toLowerCase());
        return userResponseDto;
    }
}
