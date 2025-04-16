package com.yettensyvus.elex.domain.abstraction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yettensyvus.elex.domain.constants.USER_ROLE;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BasePerson extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    private String mobile;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private USER_ROLE role;
}
