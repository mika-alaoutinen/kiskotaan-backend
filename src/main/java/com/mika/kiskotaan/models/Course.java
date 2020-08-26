package com.mika.kiskotaan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Course extends EntityModel {

    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @NotNull
    @Min(18)
    @Max(150)
    private int par;
}
