package com.mika.kiskotaan.models;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    protected Long id;
}
