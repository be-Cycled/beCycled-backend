package me.becycled.dao;

import me.becycled.BaseIntegrationTest;
import me.becycled.ByCycledBackendApplicationTest;
import me.becycled.backend.model.entity.post.Post;
import me.becycled.backend.model.entity.user.User;
import me.becycled.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Suren Kalaychyan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ByCycledBackendApplicationTest.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostDaoIntegrationTest extends BaseIntegrationTest {

    @Test
    void create() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);

        Post testPost = TestUtils.getTestPost();
        Post post = daoFactory.getPostDao().create(testPost);
        assertNotNull(post.getId());
        assertNotNull(post.getCreatedAt());

        testPost.setId(post.getId());
        testPost.setCreatedAt(post.getCreatedAt());

        assertEquals(testPost.getId(), post.getId());
        assertEquals(testPost.getTitle(), post.getTitle());
        assertEquals(testPost.getContent(), post.getContent());
        assertEquals(testPost.getPoster(), post.getPoster());
        assertEquals(testPost.getCreatedAt(), post.getCreatedAt());
    }

    @Test
    void getById() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);

        final Post post = daoFactory.getPostDao().create(TestUtils.getTestPost());
        final Post createPost = daoFactory.getPostDao().getById(post.getId());

        assertEquals(createPost, post);
    }

    @Test
    void getAll() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);

        Post firstPost = daoFactory.getPostDao().create(TestUtils.getTestPost());
        Post secondPost = daoFactory.getPostDao().create(TestUtils.getTestPost());

        final List<Post> all = daoFactory.getPostDao().getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(firstPost::equals));
        assertTrue(all.stream().anyMatch(secondPost::equals));
    }

    @Test
    void update() {
        final User testUser = TestUtils.getTestUser();
        daoFactory.getUserDao().create(testUser);
        final User user = TestUtils.getTestUser();
        user.setLogin("1");
        user.setEmail("1@gmail.com");
        user.setPhone("89001600020");
        daoFactory.getUserDao().create(user);

        final Post post = daoFactory.getPostDao().create(TestUtils.getTestPost());

        final Post testPost = new Post();
        testPost.setId(post.getId());
        testPost.setUserId(2);
        testPost.setTitle("qwe");
        testPost.setContent("asdf");
        testPost.setPoster("test");
        testPost.setCreatedAt(Instant.now());
        testPost.setUpdatedAt(Instant.now());

        final Post bdPost = daoFactory.getPostDao().update(testPost);
        assertNotNull(bdPost.getId());
        assertEquals(1, bdPost.getUserId().intValue());
        assertEquals("qwe", bdPost.getTitle());
        assertEquals("asdf", bdPost.getContent());
        assertEquals("test", bdPost.getPoster());
        assertEquals(post.getCreatedAt(), bdPost.getCreatedAt());
    }
}
