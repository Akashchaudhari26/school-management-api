package com.sms.modules.school.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Table;
import org.springframework.data.annotation.LastModifiedDate;

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
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "schools")
public class School {

    @Id

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String slug;
    private String schoolName;

    private String shortName;

    private String tagline;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)

    private Branding branding;

    @JdbcTypeCode(SqlTypes.JSON)

    private Contact contact;

    @JdbcTypeCode(SqlTypes.JSON)

    private Address address;

    @JdbcTypeCode(SqlTypes.JSON)

    private Management management;

    @JdbcTypeCode(SqlTypes.JSON)

    private Registration registration;

    @Enumerated(EnumType.STRING)
    private SchoolStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

}
