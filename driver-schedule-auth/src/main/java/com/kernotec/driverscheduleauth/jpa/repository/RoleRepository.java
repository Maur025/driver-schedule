package com.kernotec.driverscheduleauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, UUID> {

}
