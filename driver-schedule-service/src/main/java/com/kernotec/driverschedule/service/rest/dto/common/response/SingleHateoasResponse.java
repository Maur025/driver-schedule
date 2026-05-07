package com.kernotec.driverschedule.service.rest.dto.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SingleHateoasResponse<T> extends RepresentationModel<SingleHateoasResponse<T>> {

    private Integer code;
    private String message;
    private EntityModel<T> data;

    public SingleHateoasResponse() {
    }

    @Builder
    public SingleHateoasResponse(Integer code, String message, T data, List<Link> links) {
        this.code = code;
        this.message = message;

        if (data != null) {
            this.data = EntityModel.of(data, links);
            return;
        }

        if (links == null) {
            return;
        }

        this.add(links);
    }
}
