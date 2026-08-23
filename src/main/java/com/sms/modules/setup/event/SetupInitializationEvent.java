package com.sms.modules.setup.event;

import com.sms.modules.setup.dto.SetupApplicationRequest;

public record SetupInitializationEvent(
        SetupApplicationRequest request) {
}