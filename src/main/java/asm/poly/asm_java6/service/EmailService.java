package asm.poly.asm_java6.service;

public interface EmailService {
    void sendVerifyEmail(String toEmail, String fullName, String code);
}
