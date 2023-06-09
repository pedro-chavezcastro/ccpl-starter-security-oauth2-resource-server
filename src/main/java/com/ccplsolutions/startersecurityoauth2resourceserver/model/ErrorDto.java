package com.ccplsolutions.startersecurityoauth2resourceserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("time_stamp")
    private String timeStamp;

    @JsonProperty("metadata")
    private List<String> metadata;

}

