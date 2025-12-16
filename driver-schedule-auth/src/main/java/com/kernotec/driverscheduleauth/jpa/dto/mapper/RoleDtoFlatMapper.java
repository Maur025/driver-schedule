package com.kernotec.driverscheduleauth.jpa.dto.mapper;

import com.kernotec.driverscheduleauth.jpa.dto.RoleDto;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoleDtoFlatMapper {

    @Mapping(target = "realm", ignore = true)
    RoleDto toDto(Role role);

    List<RoleDto> toDto(List<Role> roleList);

    Set<User> toDto(Set<Role> roleSet);
}
