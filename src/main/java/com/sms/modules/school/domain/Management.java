package com.sms.modules.school.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Management {

    private String ownerId;

    private String principalId;

    private String vicePrincipalId;

    private List<String> adminIds;

}