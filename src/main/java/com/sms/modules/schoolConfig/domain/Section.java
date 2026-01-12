package com.sms.modules.schoolConfig.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    private String name; // "A", "B", "Rose"
    private int capacity; // Optional: max students
}