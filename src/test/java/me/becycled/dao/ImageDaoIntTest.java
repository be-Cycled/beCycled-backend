package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.image.Image;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author I1yi4
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageDaoIntTest extends BaseIntegrationTest {

    @Test
    void create() {
        Image testImage = TestUtils.getTestImage();
        Image image = daoFactory.getImageDao().create(testImage);
        assertNotNull(image.getId());
        assertNotNull(image.getData());

        assertNotEquals(TestUtils.getTestImage().getId(), image.getId()); // db use autogenerate uuid
        assertNotEquals(TestUtils.getTestImage().getId(), testImage.getId()); // db use autogenerate uuid
        assertEquals(testImage.getData(), image.getData());
    }

    @Test
    void getById() {
        final Image image = daoFactory.getImageDao().create(TestUtils.getTestImage());
        final Image createPost = daoFactory.getImageDao().getById(image.getId());

        assertEquals(createPost, image);
    }

    @Test
    void getAll() {
        Image testImage = TestUtils.getTestImage();

        Image firstPost = daoFactory.getImageDao().create(testImage);
        testImage.setData(new byte[]{ 0x23, 0x43});
        Image secondPost = daoFactory.getImageDao().create(testImage);

        final List<Image> all = daoFactory.getImageDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(firstPost::equals));
        assertTrue(all.stream().anyMatch(secondPost::equals));
    }
}
