package com.yettensyvus.elex.utils;

import java.util.concurrent.ThreadLocalRandom;

public class OtpUtil {

    public static String generateOtp() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(ThreadLocalRandom.current().nextInt(10));
        }

        return otp.toString();
    }
}
