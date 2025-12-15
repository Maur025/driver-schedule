package com.kernotec.driverscheduleauth.rest.mapper.role;

import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.rest.dto.response.role.RoleResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface RoleResponseMapper {

    RoleResponse toResponse(Role role);

    RoleResponse toResponse(UUID id);

    List<RoleResponse> toResponse(List<Role> roleList);

    Set<RoleResponse> toResponse(Set<Role> roleSet);
}
