package com.sms.modules.staff.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "staff")
public class Staff {

    @Id
    private String id;

    private String fullName;
    private String email;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;

    @Indexed(unique = true)
    private String adhaar;

    private String staffType; // TEACHER | NON_TEACHING
    private String designation;
    private LocalDate joiningDate;

    @Indexed(unique = true)
    private String employeeCode;

    // Teacher-specific fields
    private List<String> subjects;
    private List<String> assignedClassIds;

    private String tenantId;

    private StaffStatus status;
}
