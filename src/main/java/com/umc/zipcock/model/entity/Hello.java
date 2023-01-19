package com.umc.zipcock.model.entity;

import com.umc.zipcock.model.util.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Hello extends BaseEntity {

    @Id
    Long id;

    String content;
}
