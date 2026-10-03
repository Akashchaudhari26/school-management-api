package com.sms.modules.iam.domain;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Table;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles", uniqueConstraints = @jakarta.persistence.UniqueConstraint(name = "uk_role_name_tenant", columnNames = {
        "name", "tenant_id" }))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private RoleName name; // ADMIN, TEACHER, PARENT, STUDENT, ACCOUNTANT, PRINCIPAL
    private String description;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> permissions;
    private boolean isDefault;
    private String tenantId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

}
