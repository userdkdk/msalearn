package com.example.userservice.service;

import com.example.userservice.api.ResponseOrder;
import com.example.userservice.api.ResponseUser;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserMapper;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final Environment env;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity u = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + username));

        return createSpringSecurityUser(u.getId(), u.getPwd());
    }

    @Transactional
    public ResponseUser createUser(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setPwd(passwordEncoder.encode(userDto.getPwd()));
        userEntity.setUserId(UUID.randomUUID().toString());

        UserEntity saved = userRepository.save(userEntity);
        return userMapper.toResponse(saved);
    }
    public ResponseUser getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + userId));

        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        ResponseUser userDto = userMapper.toResponse(userEntity);

        List<ResponseOrder> orderList = new ArrayList<>();
        userDto.setOrders(orderList);

        return userDto;
    }

    public List<ResponseUser> getUserByAll() {
        List<UserEntity> entities = userRepository.findAll(); // JpaRepository 권장
        return userMapper.toResponseList(entities);
    }

    public UserDto getUserDetailsById(int id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + id));

        UserDto userDto = userMapper.fromEntity(userEntity);
        return userDto;
    }

    private UserDetails createSpringSecurityUser(Integer id, String password) {
        return User.builder()
                .username(String.valueOf(id))
                .password(password)
                .authorities(List.of()) // 권한이 필요하면 추가
                .build();
    }

    public Optional<? extends UserDetails> loadUserById(int userId) {
        return userRepository.findById(userId);
    }
}
