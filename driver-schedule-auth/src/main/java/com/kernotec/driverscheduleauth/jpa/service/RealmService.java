package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.exception.RealmException;
import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.jpa.repository.RealmRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public Optional<Realm> findByName(String name) {
        return repository.findByName(name);
    }

    public Realm findByNameThrow(String name) {
        return findByName(name).orElseThrow(
            () -> new RealmException("not.found", "'" + name + "'", HttpStatus.NOT_FOUND.value()));
    }
}
