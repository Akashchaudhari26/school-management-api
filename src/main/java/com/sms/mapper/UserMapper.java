package com.sms.mapper;

import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.dto.UserResponse;

public class UserMapper {
    public static UserResponse toResponse(User u) {
        if (u == null)
            return null;
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setFullName(u.getFullName());
        r.setEmail(u.getEmail());
        r.setMobile(u.getMobile());
        r.setProfileImageUrl(u.getProfileImageUrl());
        r.setStatus(u.getStatus());
        r.setRoleName(u.getRoleName());
        r.setTenantId(u.getTenantId());
        r.setUserId(u.getUserId());
        r.setAdharNumber(u.getAdharNumber());
        return r;
    }
}