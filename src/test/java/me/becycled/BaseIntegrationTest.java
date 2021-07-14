package me.becycled;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import me.becycled.utils.TestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author I1yi4
 */
public abstract class BaseIntegrationTest {

    private static PostgreSQLContainer<?> testPostgres;
    private static boolean SCHEMA_INITIALIZED = false;

    private static final String SCHEMA_PATH = "schema/";

    @Configuration
    public static class TestDataSourceConfig {

        @PostConstruct
        public void init() {
            if (SCHEMA_INITIALIZED) {
                return;
            }

            testPostgres = new PostgreSQLContainer<>("postgres:13");
            testPostgres.start();

            final PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUrl(testPostgres.getJdbcUrl());
            ds.setUser(testPostgres.getUsername());
            ds.setPassword(testPostgres.getPassword());

            prepareTestSchema(ds);
            SCHEMA_INITIALIZED = true;
        }

        @Bean
        @Primary
        public DataSource dataSource() {
            if (!testPostgres.isRunning()) {
                throw new BeanInstantiationException(DataSource.class, "Test docker container is not running!");
            }

            final PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUrl(testPostgres.getJdbcUrl());
            ds.setUser(testPostgres.getUsername());
            ds.setPassword(testPostgres.getPassword());
            return ds;
        }
    }

    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected DaoFactory daoFactory;

    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;

    @AfterEach
    public void tearDown() throws Exception {
        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'");
             final ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                final String tableName = resultSet.getString("table_name");
                conn.prepareStatement("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE").execute();
            }
        }
    }

    private static void prepareTestSchema(final DataSource dataSource) {
        try (final Connection conn = dataSource.getConnection()) {
            for (final String script : getFileContentsInFolderByPath(SCHEMA_PATH)) {
                try (PreparedStatement stmt = conn.prepareStatement(script)) {
                    stmt.execute();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static List<String> getFileContentsInFolderByPath(final String folderPath) throws IOException, URISyntaxException {
        return Arrays.stream(getFileNamesInFolderByPath(folderPath))
            .sorted(String::compareTo) // Сортировка файлов по имени
            .map(scriptName -> {
                try {
                    return IOUtils.toString(
                        BaseIntegrationTest.class.getClassLoader().getResourceAsStream(folderPath + scriptName),
                        StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return "";
                }
            })
            .collect(Collectors.toList());
    }

    private static String[] getFileNamesInFolderByPath(final String folderPath) throws URISyntaxException, IOException {
        URL dirURL = BaseIntegrationTest.class.getClassLoader().getResource(folderPath);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
            final String className = BaseIntegrationTest.class.getName().replace('.', '/') + ".class";
            dirURL = BaseIntegrationTest.class.getClassLoader().getResource(className);
        }

        if (dirURL != null && dirURL.getProtocol().equals("jar")) {
            final String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
            try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                final Enumeration<JarEntry> entries = jar.entries();
                final Set<String> result = new HashSet<>();
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(folderPath)) {
                        final String entry = name.substring(folderPath.length());
                        final int checkSubDir = entry.indexOf('/');
                        if (checkSubDir >= 0) {
                            continue;
                        }

                        result.add(entry);
                    }
                }

                return result.toArray(new String[0]);
            }
        }

        throw new UnsupportedOperationException("Error on getting the list of files for URL " + dirURL);
    }

    protected User createUser(final User user) {
        UserAccount userAccount = daoFactory.getUserAccountDao().create(user, TestUtils.getTestUserAccount());
        return daoFactory.getUserDao().getById(userAccount.getUserId());
    }
}
