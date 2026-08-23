package com.sms.modules.setup.service;

import com.sms.modules.setup.dto.SetupApplicationRequest;
import com.sms.modules.setup.dto.SetupStatusResponse;

public interface SetupService {
    SetupStatusResponse getStatus();

    void initializeApplication(SetupApplicationRequest request);
}
