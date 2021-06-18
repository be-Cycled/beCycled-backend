package me.becycled.backend;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ByCycledBackendApplicationTests {

    @MockBean
    DaoFactory daoFactory;

    @Test
    void contextLoads() {
    }
}
