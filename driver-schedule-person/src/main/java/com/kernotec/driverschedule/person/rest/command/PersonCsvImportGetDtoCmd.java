package com.kernotec.driverschedule.person.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.common.util.CsvImportUtil;
import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import com.kernotec.driverschedule.person.rest.dto.PersonCsvImportDto;
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
        String[] csvData = request.csvData();

        if (csvData == null || csvData.length == 0) {
            return null;
        }

        var personCsvImportDto = new PersonCsvImportDto();

        personCsvImportDto.setUsername(CsvImportUtil.getValueOfCsv(csvData[1]));
        personCsvImportDto.setName(CsvImportUtil.getValueOfCsv(csvData[2]));
        personCsvImportDto.setLastName(CsvImportUtil.getValueOfCsv(csvData[3]));
        personCsvImportDto.setDocument(CsvImportUtil.getValueOfCsv(csvData[4]));
        personCsvImportDto.setPhoneWhatsapp(CsvImportUtil.getValueOfCsv(csvData[5]));
        personCsvImportDto.setPhoneWork(CsvImportUtil.getValueOfCsv(csvData[6]));
        personCsvImportDto.setPersonType(
            PersonTypeEnum.getByValueEs(CsvImportUtil.getValueOfCsv(csvData[7])));

        return personCsvImportDto;
    }

    @Builder
    public record Request(@NotNull String[] csvData) {

    }
}
