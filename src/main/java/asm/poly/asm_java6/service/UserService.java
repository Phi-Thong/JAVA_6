package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.users;

public interface UserService {
    users findByEmail(String email);

    void save(users user);

    void sendVerifyEmail(String toEmail, String fullName, String token);

    users findByToken(String token);
}