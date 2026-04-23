package com.kernotec.driverscheduleservice.jpa.service.resource;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.resource.PersonType;
import com.kernotec.driverscheduleservice.jpa.repository.resource.PersonTypeRepository;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class PersonTypeService extends BaseServiceImpl<PersonType, UUID> {

    private final PersonTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Person Type";
    }

    @Override
    protected BaseRepository<PersonType, UUID> repository() {
        return repository;
    }

    public Set<String> getCodesOfPersonTypeIds(Set<UUID> personTypeIdSet) {
        if (personTypeIdSet.isEmpty()) {
            log.debug("Person type ID set is empty, returning empty code set");
            return Set.of();
        }

        Map<UUID, PersonType> personTypeMap = findAll().stream()
            .collect(Collectors.toMap(PersonType::getId, personType -> personType));

        Set<String> personTypeCodes = new HashSet<>();

        for (UUID personTypeId : personTypeIdSet) {
            if (!personTypeMap.containsKey(personTypeId)) {
                log.debug("Person type with id '{}' not found, skipping...", personTypeId);
                continue;
            }

            PersonType personType = personTypeMap.get(personTypeId);
            personTypeCodes.add(personType.getCode());
        }

        return personTypeCodes;
    }
}
