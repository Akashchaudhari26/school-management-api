package com.sms.modules.staff.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffUpdateDTO {
    private String firstName;
    private String lastName;
    private String department;
    private String designation;
    @Email
    private String email;
    private String contactNumber;
    private LocalDate joiningDate;
    private Boolean isActive;
}
