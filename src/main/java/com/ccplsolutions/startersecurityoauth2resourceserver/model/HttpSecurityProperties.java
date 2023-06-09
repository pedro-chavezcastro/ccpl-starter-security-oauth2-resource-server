package com.ccplsolutions.startersecurityoauth2resourceserver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Setter
@ConfigurationProperties(prefix = "security.oauth2.resourceserver")
public class HttpSecurityProperties {
    @Getter
    private boolean enable = true;

    private String uri;

    private String jwkSetUri = "certs";

    public String getJwkSetUri() {
        if (Objects.isNull(uri)) {
            return null;
        }
        return UriComponentsBuilder.fromUriString(this.uri).path(this.jwkSetUri).toUriString();
    }

}
