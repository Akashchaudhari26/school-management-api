package com.sms.modules.iam.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Document(collection = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "uk_role_name_tenant", def = "{'name':1,'tenantId':1}", unique = true)
public class Role {
    @Id
    private String id;
    private RoleName name; // ADMIN, TEACHER, PARENT, STUDENT, ACCOUNTANT, PRINCIPAL
    private String description;
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