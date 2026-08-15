package Expense_Calculator.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(
            String toEmail,
            String resetToken) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Password Reset");
        message.setText(
                "Click the link to reset your password:\n\n"
                        + "http://localhost:3000/reset-password?token="
                        + resetToken
                        + "\n\n"
                        + "This link will expire in 15 minutes."
        );

        mailSender.send(message);
    }
}
