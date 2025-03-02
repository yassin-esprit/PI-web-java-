package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com"; // Replace with your SMTP server
    private static final String SMTP_PORT = "587"; // Port for TLS
    private static final String EMAIL_USERNAME = "yassinejaouani@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "aklp dfce cnnt kuil "; // Replace with your email password

    /**
     * Send an email notification to a participant.
     *
     * @param toEmail   The participant's email address.
     * @param subject   The email subject.
     * @param body      The email body.
     */
    public static void sendEmail(String toEmail, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME)); // Set sender
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Set recipient
            message.setSubject(subject); // Set subject
            message.setText(body); // Set body

            Transport.send(message); // Send the email

            System.out.println("Email sent successfully to: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}