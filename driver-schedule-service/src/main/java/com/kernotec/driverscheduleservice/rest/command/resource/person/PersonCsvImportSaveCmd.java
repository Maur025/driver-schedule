package com.kernotec.driverscheduleservice.rest.command.resource.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.rest.dto.resource.PersonCsvImportDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonCsvImportSaveCmd extends
    AbstractTransactionalRequiredCommand<PersonCsvImportSaveCmd.Request, Void>
{

    @Override
    protected Void run(Request request) {
        if (request.dtoList.isEmpty()) {
            log.debug("No persons data to import.");
            return null;
        }

        for (PersonCsvImportDto personCsvImportDto : request.dtoList) {
            log.info("Importing person: {}", personCsvImportDto.getUsername());
        }

        return null;
    }

    @Builder
    public record Request(@NotNull List<PersonCsvImportDto> dtoList) {

    }
}
