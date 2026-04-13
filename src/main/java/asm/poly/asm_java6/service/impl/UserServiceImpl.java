package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.dto.CustomerSummaryDTO;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import asm.poly.asm_java6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public UserServiceImpl(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public void save(users user) {
        usersRepository.save(user);
    }

    @Override
    public void sendVerifyEmail(String toEmail, String fullName, String token) {
        String fromEmail = "nguyenphithong167@gmail.com"; // Email gửi
        String password = "lpag jica rptg bbwb";          // App password Gmail

        String subject = "Xác minh tài khoản Sneaker Store";
        String verifyLink = "http://localhost:8080/verify?token=" + token;
        String content = "<div style=\"font-family: Arial, sans-serif;\">" +
                "<h2>Xác minh tài khoản</h2>" +
                "<p>Xin chào " + (fullName == null || fullName.isBlank() ? "bạn" : fullName) + ",</p>" +
                "<p>Vui lòng nhấn vào link bên dưới để xác minh email:</p>" +
                "<p><a href=\"" + verifyLink + "\" style=\"background:#359EFF;color:#fff;padding:10px 20px;text-decoration:none;border-radius:6px;\">XÁC MINH TÀI KHOẢN</a></p>" +
                "<p>Nếu bạn không đăng ký, hãy bỏ qua email này.</p>" +
                "</div>";

        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Sneaker Store", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, toEmail);
            message.setSubject(subject, "UTF-8");
            message.setContent(content, "text/html; charset=UTF-8");


            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public users findByToken(String token) {
        return usersRepository.findByToken(token);
    }

    @Override
    public Page<CustomerSummaryDTO> getAllCustomerSummaries(Pageable pageable) {
        return usersRepository.findAllCustomerSummaries(pageable);
    }

    @Override
    public Page<CustomerSummaryDTO> getAllCustomerSummariesByStatus(boolean trangThai, Pageable pageable) {
        return usersRepository.findAllCustomerSummariesByTrangThai(trangThai, pageable);
    }

    @Override
    public Page<CustomerSummaryDTO> searchCustomers(String keyword, Boolean trangThai, Pageable pageable) {
        return usersRepository.searchCustomers(
                (keyword == null || keyword.isBlank()) ? null : keyword,
                trangThai,
                pageable
        );
    }

}