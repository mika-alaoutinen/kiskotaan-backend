package com.mika.kiskotaan.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class EntityModel {

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    protected Long id;
}
