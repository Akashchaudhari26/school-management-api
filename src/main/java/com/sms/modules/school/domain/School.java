package com.sms.modules.school.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sms.modules.school.enums.SchoolStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "schools")
public class School {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    @Indexed
    private String schoolName;

    private String shortName;

    private String tagline;

    private String description;

    private Branding branding;

    private Contact contact;

    private Address address;

    private Management management;

    private Registration registration;

    private SchoolStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

}