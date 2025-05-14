package org.demo.migrissync.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ServiceDescriptionResponse(@JsonProperty("visits") Visit visits) {
}
