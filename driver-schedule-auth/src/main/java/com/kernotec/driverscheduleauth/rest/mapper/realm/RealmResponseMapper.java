package com.kernotec.driverscheduleauth.rest.mapper.realm;

import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.rest.dto.response.realm.RealmResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface RealmResponseMapper {

    RealmResponse toResponse(Realm realm);

    RealmResponse toResponse(UUID id);

    List<RealmResponse> toResponse(List<Realm> realmList);

    Set<RealmResponse> toResponse(Set<Realm> realmSet);
}
