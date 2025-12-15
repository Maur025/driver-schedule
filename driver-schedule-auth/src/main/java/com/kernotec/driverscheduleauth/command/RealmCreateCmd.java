package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.Realm;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RealmCreateCmd extends
    AbstractTransactionalRequiredCommand<RealmCreateCmd.Request, UUID>
{

    private final RealmService realmService;

    @Override
    protected UUID run(Request request) {
        var realm = new Realm();

        realm.setName(request.name);

        realm = realmService.save(realm);
        return realm.getId();
    }

    @Builder
    public record Request(@NotNull String name) {

    }
}
