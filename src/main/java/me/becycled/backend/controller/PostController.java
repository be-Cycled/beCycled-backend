package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.post.Post;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Suren Kalaychyan
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final DaoFactory daoFactory;

    @Autowired
    public PostController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Post post = daoFactory.getPostDao().getById(id);
        if (post == null) {
            return new ResponseEntity<>("Not found post", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(post);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(daoFactory.getPostDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> create(@RequestBody final Post entity) {
        return ResponseEntity.ok(daoFactory.getPostDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id") final int id,
                                    @RequestBody final Post entity) {
        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.BAD_REQUEST);
        }

        final Post post = daoFactory.getPostDao().getById(id);
        if (post == null) {
            return new ResponseEntity<>("Post not exist", HttpStatus.NOT_FOUND);
        }
        if (!post.getUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Only owner can update post", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(daoFactory.getPostDao().update(entity));
    }
}
