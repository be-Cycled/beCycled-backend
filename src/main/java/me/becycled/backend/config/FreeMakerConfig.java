package me.becycled.backend.config;

import freemarker.template.Configuration;
import freemarker.template.Version;
import me.becycled.backend.ByCycledBackendApplication;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum FreeMakerConfig {;

    public static final Version FREE_MAKER_VERSION = Configuration.VERSION_2_3_31;

    public static Configuration buildConfig() {
        final Configuration conf = new Configuration(FREE_MAKER_VERSION);
        conf.setClassForTemplateLoading(ByCycledBackendApplication.class, "/");
        conf.setDefaultEncoding("UTF-8");
        return conf;
    }
}
