package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.jpa.repository.RealmRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RealmService extends BaseServiceImpl<Realm, UUID> {

    private final RealmRepository repository;

    @Override
    protected String resourceName() {
        return "Realm";
    }

    @Override
    protected BaseRepository<Realm, UUID> repository() {
        return repository;
    }
}
