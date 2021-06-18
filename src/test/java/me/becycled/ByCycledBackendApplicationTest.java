package me.becycled;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author I1yi4
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ByCycledBackendApplicationTest {
}
