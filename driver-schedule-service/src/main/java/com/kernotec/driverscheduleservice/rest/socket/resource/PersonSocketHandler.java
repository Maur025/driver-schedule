package com.kernotec.driverscheduleservice.rest.socket.resource;

import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.rest.socket.SocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class PersonSocketHandler extends
    SocketHandler<PersonSocketHandler.Request, PersonResponse>
{

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
