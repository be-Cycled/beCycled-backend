package me.becycled.backend.service.email;

import me.becycled.backend.exception.EmailNotSendException;

import java.util.Collections;
import java.util.List;

/**
 * @author I1yi4
 */
public interface EmailService {

    void send(List<String> to, List<String> cc, List<String> bcc, String subject, String body, BodyType bodyType, List<Attachment> attachments) throws EmailNotSendException;

    default void send(final String to, final String subject, final String body, final BodyType bodyType) throws EmailNotSendException {
        send(Collections.singletonList(to), Collections.emptyList(), Collections.emptyList(), subject, body, bodyType, Collections.emptyList());
    }

    default void send(final List<String> to, final String subject, final String body, final BodyType bodyType) throws EmailNotSendException {
        send(to, Collections.emptyList(), Collections.emptyList(), subject, body, bodyType, Collections.emptyList());
    }

    default void send(final String to, final String subject, final String body, final BodyType bodyType, final Attachment attachment) throws EmailNotSendException {
        send(Collections.singletonList(to), Collections.emptyList(), Collections.emptyList(), subject, body, bodyType, Collections.singletonList(attachment));
    }

    default void send(final List<String> to, final String subject, final String body, final BodyType bodyType, final Attachment attachment) throws EmailNotSendException {
        send(to, Collections.emptyList(), Collections.emptyList(), subject, body, bodyType, Collections.singletonList(attachment));
    }

    default void send(final List<String> to, final String subject, final String body, final BodyType bodyType, final List<Attachment> attachments) throws EmailNotSendException {
        send(to, Collections.emptyList(), Collections.emptyList(), subject, body, bodyType, attachments);
    }
}
