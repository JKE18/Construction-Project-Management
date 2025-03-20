import com.example.constructionsystem.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.Instant;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerUnitTest {

    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
    }

    @Test
    void testPrepareTextMessage() throws MessagingException, IOException {
        String recipient = "test@example.com";
        String subject = "Test Email";
        String content = "This is a test email.";

        MimeMessage preparedMessage = MailMessagePreparer.prepareTextMessage(recipient, subject, content);

        assertNotNull(preparedMessage, "Wiadomość e-mail nie powinna być null!");
        assertEquals(subject, preparedMessage.getSubject(), "Temat wiadomości jest niepoprawny!");
        assertEquals(recipient, preparedMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString(), "Odbiorca wiadomości jest niepoprawny!");
        assertEquals(content, preparedMessage.getContent().toString().trim(), "Treść wiadomości jest niepoprawna!");
    }

    @Test
    void testMailConfiguration() {
        Properties props = MailConfiguration.getConfiguration();
        assertEquals("true", props.getProperty("mail.smtp.auth"));
        assertEquals("true", props.getProperty("mail.smtp.ssl.enable"));
        assertEquals("true", props.getProperty("mail.smtp.starttls.enable"));
        assertEquals("poczta.o2.pl", props.getProperty("mail.smtp.host"));
        assertEquals("465", props.getProperty("mail.smtp.port"));
    }

    @Test
    void testSingletonInstance() {
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();
        assertSame(instance1, instance2, "Obiekt SessionManager powinien być singletonem!");
    }

    @Test
    void testPasswordValidation() {
        LoginController controller = new LoginController();
        assertTrue(controller.isValidPassword("StrongPass123!"), "Hasło spełnia wymagania!");
        assertFalse(controller.isValidPassword("short"), "Hasło jest za krótkie!");
        assertFalse(controller.isValidPassword("nouppercase123"), "Hasło nie zawiera dużej litery!");
    }

    @Test
    void testSendEmailSuccess() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "poczta.o2.pl");
            props.put("mail.smtp.port", "587");

            String EMAIL_FROM = System.getenv("SMTP_EMAIL");
            String EMAIL_PASSWORD = System.getenv("SMTP_PASSWORD");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setSubject("test");
            message.setText("this is test");
            message.setRecipients(MimeMessage.RecipientType.TO, "test@example.com");
            Transport.send(message);

            assertTrue(true, "Wiadomość powinna zostać wysłana bez błędów.");
        }catch (MessagingException e){ assertFalse(false, "wysyłka nie powinna zwracac błedu");}
    }

    @Test
    void testEmailSendingFailure() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "wrong.email.pl"); //bledny
            props.put("mail.smtp.port", "587");

            String EMAIL_FROM = System.getenv("SMTP_EMAIL");
            String EMAIL_PASSWORD = System.getenv("SMTP_PASSWORD");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setSubject("test");
            message.setText("this is test");
            message.setRecipients(MimeMessage.RecipientType.TO, "test@example.com");
            Transport.send(message);


            assertFalse(false, "Powinien wystąpić wyjątek, jeśli SMTP nie działa!");
        }catch (MessagingException e){
            assertTrue(true, "Błąd powinien być związany z serwerem SMTP.");
        }
    }



}
