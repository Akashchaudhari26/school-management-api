package com.sms.modules.schoolConfig.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchoolClass {

    @Id

    private String id; // "NURSERY", "LKG", "UKG", "GRADE_1"

    private String displayName; // "Veda", "Agni", "Arjun"

    private String program; // "Pre-Primary", "Primary"

    @Column(name = "display_order")
    private int order;

    @JdbcTypeCode(SqlTypes.JSON)

    private List<Section> sections = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)

    private List<String> subjectNames = new ArrayList<>();
}
