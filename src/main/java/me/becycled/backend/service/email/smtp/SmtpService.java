package me.becycled.backend.service.email.smtp;

import me.becycled.backend.exception.EmailNotSendException;
import me.becycled.backend.service.email.Attachment;
import me.becycled.backend.service.email.BodyType;
import me.becycled.backend.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * @author I1yi4
 */
@Service
@Profile("!test")
public class SmtpService implements EmailService {

    private static final String EMAIL_PERSONAL = "no-reply";
    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public SmtpService(final JavaMailSender sender) {
        this.sender = sender;
    }

    @PostConstruct
    public void config() {
        System.setProperty("mail.mime.encodeparameters", "false");
        System.setProperty("mail.mime.charset", "UTF-8");
    }

    @Override
    @SuppressWarnings("ParameterNumber")
    public void send(final List<String> to, final List<String> cc, final List<String> bcc, final String subject,
                     final String body, final BodyType bodyType, final List<Attachment> attachments) throws EmailNotSendException {
        sendMessage(to, cc, bcc, subject, body, bodyType, attachments);
    }

    @SuppressWarnings("ParameterNumber")
    private void sendMessage(final List<String> to, final List<String> cc, final List<String> bcc, final String subject,
                             final String body, final BodyType bodyType, final List<Attachment> attachments) throws EmailNotSendException {
        try {
            final MimeMessage message = sender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to.toArray(new String[0]));
            helper.setCc(cc.toArray(new String[0]));
            helper.setBcc(bcc.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, bodyType == BodyType.HTML);
            message.addFrom(new InternetAddress[]{new InternetAddress(fromEmail, EMAIL_PERSONAL)});
            for (final Attachment attachment : attachments) {
                helper.addAttachment(attachment.getName(), new ByteArrayResource(attachment.getData()));
            }
            sender.send(message);

        } catch (Exception ex) {
            throw new EmailNotSendException("Error on sending email via SMTP: " + ex.getMessage(), ex);
        }
    }
}
