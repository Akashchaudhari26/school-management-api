package com.sms.core.exceptions;

public class InvalidFeePaymentException extends RuntimeException {
    public InvalidFeePaymentException(String message) {
        super(message);
    }
}
