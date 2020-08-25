package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Course extends EntityModel {

    @NotBlank
    @Size(min = 3, max = 34)
    private String name;

    @NotNull
    @Min(1)
    @Max(6)
    private int par;
}
