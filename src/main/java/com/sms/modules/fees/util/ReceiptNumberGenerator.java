package com.sms.modules.fees.util;

import java.time.LocalDate;
import java.util.UUID;

public class ReceiptNumberGenerator {

    public static String generate() {
        return "RCPT-" + LocalDate.now().getYear() + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
