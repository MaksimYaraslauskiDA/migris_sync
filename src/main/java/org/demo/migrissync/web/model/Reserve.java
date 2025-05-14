package org.demo.migrissync.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Reserve(
  @JsonProperty("serviceDescription") @JsonIgnoreProperties(ignoreUnknown = true) Map<String, String> serviceDescription) {
}
