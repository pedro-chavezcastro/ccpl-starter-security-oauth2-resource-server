package com.ccplsolutions.startersecurityoauth2resourceserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceServerErrorCode {

    AUTHENTICATION_ERROR("AUTHENTICATION_ERROR", "Error de autenticación"),
    ACCESS_DENIED("ACCESS_DENIED", "Acceso denegado");

    private final String code;
    private final String description;


}
