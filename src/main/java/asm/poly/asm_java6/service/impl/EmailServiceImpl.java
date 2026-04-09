package asm.poly.asm_java6.service.impl;


import org.springframework.stereotype.Service;

import asm.poly.asm_java6.service.EmailService;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendVerifyEmail(String toEmail, String fullName, String code) {
        String fromEmail = "nguyenphithong167@gmail.com";
        String password = "lpag jica rptg bbwb";

        String subject = "Mã xác nhận đặt lại mật khẩu - Sneaker Store";

        String name = (fullName == null || fullName.isBlank()) ? "bạn" : fullName;

        String content = """
                <div style="margin:0;padding:0;background:#0f172a;font-family:Arial,sans-serif;">
                
                  <div style="max-width:520px;margin:50px auto;background:#020617;
                       border-radius:20px;overflow:hidden;
                       box-shadow:0 20px 50px rgba(0,0,0,0.6);border:1px solid #1e293b;">
                
                    <!-- HEADER -->
                    <div style="padding:25px;text-align:center;
                         background:linear-gradient(135deg,#1e3a8a,#2563eb);">
                
                      <h1 style="margin:0;color:white;font-size:24px;letter-spacing:2px;">
                        SNEAKER STORE
                      </h1>
                
                      <p style="margin:5px 0 0;color:#cbd5f5;font-size:12px;">
                        Premium Experience
                      </p>
                    </div>
                
                    <!-- BODY -->
                    <div style="padding:35px;text-align:center;">
                
                      <h2 style="color:white;margin-bottom:10px;">
                        🔐 Reset Password
                      </h2>
                
                      <p style="color:#94a3b8;font-size:14px;">
                        Xin chào <b style="color:white;">%s</b>,
                      </p>
                
                      <p style="color:#94a3b8;font-size:14px;">
                        Sử dụng mã dưới đây để đặt lại mật khẩu:
                      </p>
                
                      <!-- CODE BOX -->
                      <div style="margin:30px auto;padding:20px 30px;
                           background:linear-gradient(135deg,#020617,#0f172a);
                           border:1px solid #334155;
                           border-radius:14px;
                           font-size:32px;
                           font-weight:bold;
                           color:#38bdf8;
                           letter-spacing:6px;
                           width:max-content;
                           box-shadow:0 0 15px rgba(56,189,248,0.5);">
                
                        %s
                
                      </div>
                
                      <p style="color:#64748b;font-size:13px;">
                        ⏱ Mã có hiệu lực trong <b style="color:white;">5 phút</b>
                      </p>
                
                      <!-- BUTTON (fake cho đẹp) -->
                      <div style="margin-top:25px;">
                        <a style="display:inline-block;
                           padding:12px 24px;
                           border-radius:10px;
                           background:linear-gradient(135deg,#3b82f6,#2563eb);
                           color:white;
                           text-decoration:none;
                           font-size:14px;
                           box-shadow:0 5px 15px rgba(37,99,235,0.4);">
                          Xác nhận ngay
                        </a>
                      </div>
                
                      <p style="margin-top:25px;color:#475569;font-size:12px;">
                        Nếu bạn không yêu cầu, hãy bỏ qua email này.
                      </p>
                
                    </div>
                
                    <!-- FOOTER -->
                    <div style="padding:15px;text-align:center;
                         background:#020617;border-top:1px solid #1e293b;
                         font-size:12px;color:#475569;">
                
                      © 2026 Sneaker Store • All rights reserved
                
                    </div>
                
                  </div>
                
                </div>
                """.formatted(name, code);

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
}
