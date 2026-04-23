package com.kernotec.driverscheduleservice.rest.command.resource.person;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.rest.dto.resource.PersonCsvImportDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonCsvImportGetDtoCmd extends
    AbstractCommand<PersonCsvImportGetDtoCmd.Request, PersonCsvImportDto>
{

    @Override
    protected PersonCsvImportDto run(Request request) {
        String[] csvData = request.csvData;

        var personCsvImportDto = new PersonCsvImportDto();

        personCsvImportDto.setName(csvData[0].isBlank() ? null : csvData[0]);
        personCsvImportDto.setLastName(csvData[1].isBlank() ? null : csvData[1]);
        personCsvImportDto.setDocument(csvData[2].isBlank() ? null : csvData[2]);
        personCsvImportDto.setPhone(csvData[3].isBlank() ? null : csvData[3]);
        personCsvImportDto.setUsername(csvData[4].isBlank() ? null : csvData[4]);
        personCsvImportDto.setPersonType(csvData[5].isBlank() ? null : csvData[5]);

        return personCsvImportDto;
    }

    @Builder
    public record Request(@NotNull String[] csvData) {

    }
}
