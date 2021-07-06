package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.post.Post;
import me.becycled.backend.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Suren Kalaychyan
 */
@RestController
@RequestMapping("/posts")
@Api(description = "Статьи")
public class PostController {

    private final DaoFactory daoFactory;

    @Autowired
    public PostController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить статью по ее идентификатору")
    public ResponseEntity<?> getById(
        @ApiParam("Идентификатор статьи") @PathVariable("id") final int id) {

        final Post post = daoFactory.getPostDao().getById(id);
        if (post == null) {
            return new ResponseEntity<>("Post is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(post);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все статьи")
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(daoFactory.getPostDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать статью")
    public ResponseEntity<Post> create(
        @ApiParam("Данные статьи") @RequestBody final Post entity) {

        return ResponseEntity.ok(daoFactory.getPostDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить статью по ее идентификатору")
    public ResponseEntity<?> update(
        @ApiParam("Идентификатор статьи") @PathVariable("id") final int id,
        @ApiParam("Данные статьи") @RequestBody final Post entity) {

        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final Post post = daoFactory.getPostDao().getById(id);
        if (post == null) {
            return new ResponseEntity<>("Post not exist", HttpStatus.NOT_FOUND);
        }

        if (!post.getUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Post can be updated by owner only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getPostDao().update(entity));
    }
}
