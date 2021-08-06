package me.becycled.backend.model.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import me.becycled.backend.config.FreeMakerConfig;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum EmailUtils {;

    public static String buildResetPasswordTopic() {
        return "beCycled — Восстановление пароля";
    }

    public static String buildResetPasswordBody(final String firstName, final String lastName, final String password) {
        return buildTemplate("templates/reset-password.ftl", Map.of(
            "username", firstName + " " + lastName,
            "password", password)
        );
    }

    private static String buildTemplate(final String templateName, final Map<String, ?> values) {
        try (StringWriter sw = new StringWriter()) {
            final Configuration config = FreeMakerConfig.buildConfig();
            final Template template = config.getTemplate(templateName);
            template.process(values, sw);
            sw.flush();
            return sw.getBuffer().toString();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot create telegram notification html body: " + ex.getMessage(), ex);
        }
    }
}
