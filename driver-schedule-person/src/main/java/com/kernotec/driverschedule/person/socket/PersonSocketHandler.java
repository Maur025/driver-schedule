package com.kernotec.driverschedule.person.socket;

import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.person.rest.dto.response.PersonResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseMapper;
import com.kernotec.driverschedule.person.socket.PersonSocketHandler.Request;
import com.kernotec.driverschedule.socket.service.SocketHandler;
import com.kernotec.driverschedule.socket.service.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class PersonSocketHandler extends SocketHandler<Request, PersonResponse> {

    private final PersonService personService;
    private final PersonResponseMapper personResponseMapper;

    public PersonSocketHandler(WebSocketHandler webSocketHandler, PersonService personService,
        PersonResponseMapper personResponseMapper)
    {
        super(webSocketHandler);
        this.personService = personService;
        this.personResponseMapper = personResponseMapper;
    }

    @Override
    protected String getTopic(Request request) {
        return request.topic();
    }

    @Override
    protected Set<UUID> getToList(Request request) {
        return request.toList();
    }

    @Override
    protected PersonResponse getResponseData(Request request) {
        Person person = personService.findByIdThrow(request.personId());
        return personResponseMapper.toResponse(person);
    }

    @Builder
    public record Request(@NotNull UUID personId, @NotNull String topic, Set<UUID> toList) {

    }
}
