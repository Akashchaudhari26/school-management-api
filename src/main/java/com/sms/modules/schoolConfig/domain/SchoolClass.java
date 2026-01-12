package com.sms.modules.schoolConfig.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "classes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchoolClass {

    @Id
    private String id; // "NURSERY", "LKG", "UKG", "GRADE_1"

    private String displayName; // "Veda", "Agni", "Arjun"

    private String program; // "Pre-Primary", "Primary"

    private int order;

    private List<Section> sections = new ArrayList<>();

    private List<String> subjectIds = new ArrayList<>();
}