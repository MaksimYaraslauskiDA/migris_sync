package org.demo.migrissync.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Institution {
    @JsonProperty("key")
    private String key;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("titleLt")
    private String titleLt;
    
    @JsonProperty("titleEn")
    private String titleEn;
}
