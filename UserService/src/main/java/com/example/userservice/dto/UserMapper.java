package com.example.userservice.dto;

// UserMapper.java
import com.example.userservice.api.RequestLogin;
import com.example.userservice.api.RequestUser;
import com.example.userservice.api.ResponseUser;
import com.example.userservice.jpa.UserEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    // request to userDto
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "orders", ignore = true)
    UserDto fromRequest(RequestUser user);

    // response to userDto
    @Mapping(target = "pwd", ignore = true)
    UserDto fromResponse(ResponseUser user);

    // userDto to response
    @Mapping(target = "orders", ignore = true)
    ResponseUser toResponse(UserEntity user);

    // DTO -> Entity (엔티티의 DB/자동값은 무시)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)   // 서비스에서 채움
    @Mapping(target = "pwd", ignore = true)      // 서비스에서 해시
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(UserDto dto);

    // Entity -> DTO
    @Mapping(target = "pwd", ignore = true)
    @Mapping(target = "orders", ignore = true)
    UserDto fromEntity(UserEntity entity);

    // ListEntity to ListResponse
    @Mapping(target = "pwd", ignore = true)
    @Mapping(target = "orders", ignore = true)
    List<ResponseUser> toResponseList(List<UserEntity> users);
}
