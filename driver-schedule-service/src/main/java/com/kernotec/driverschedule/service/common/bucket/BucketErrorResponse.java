package com.kernotec.driverschedule.service.common.bucket;

import lombok.Builder;

@Builder
public record BucketErrorResponse(int code, String error, String message) {

}
