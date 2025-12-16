package com.kernotec.driverscheduleauth.jpa.dto.mapper;

import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {RoleDtoFlatMapper.class})
public interface UserDtoMapper {

    UserDto toDto(User user);

    List<UserDto> toDto(List<User> userList);

    Set<User> toDto(Set<User> userSet);
}
