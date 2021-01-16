package com.cts.galvenize.gmdb.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieUpdateRequest {
    private String rating;
    private String review;
}
