package com.kernotec.driverscheduleauth.rest.mapper.user;

import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.rest.dto.response.user.UserResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface UserResponseMapper {

    UserResponse toResponse(User user);

    UserResponse toResponse(UUID id);

    List<UserResponse> toResponse(List<User> userList);

    Set<UserResponse> toResponse(Set<User> userSet);
}
