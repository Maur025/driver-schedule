package com.kernotec.driverscheduleservice.jpa.dto.request;

import com.kernotec.core.jpa.dto.EntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationRequestStateDto extends EntityDto {

    private String name;
    private String code;
}
