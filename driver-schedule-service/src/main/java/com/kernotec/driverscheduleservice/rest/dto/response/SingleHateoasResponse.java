package com.kernotec.driverscheduleservice.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.BaseResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SingleHateoasResponse<T> extends BaseResponse {

    private EntityModel<T> data;

    public SingleHateoasResponse() {
    }

    @Builder
    public SingleHateoasResponse(Integer code, String message, T data, List<Link> links) {
        super(code, message);
        this.data = EntityModel.of(data, links);
    }
}
