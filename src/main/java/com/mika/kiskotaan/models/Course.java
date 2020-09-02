package com.mika.kiskotaan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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
    @Size(min = 1, max = 30)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hole> holes = new ArrayList<>();
}
