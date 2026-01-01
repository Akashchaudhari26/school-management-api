package com.sms.modules.iam.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Document(collection = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private String id;
	private String name; // ADMIN, TEACHER, PARENT, STUDENT, ACCOUNTANT, PRINCIPAL
    private String description;
    private List<String> permissions;
    private boolean isDefault;
    private String tenantId;

}