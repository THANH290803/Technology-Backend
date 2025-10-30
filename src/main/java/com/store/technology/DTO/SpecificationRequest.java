package com.store.technology.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificationRequest {
    private String name;
    private String value;
    private Long configurationId;
}
