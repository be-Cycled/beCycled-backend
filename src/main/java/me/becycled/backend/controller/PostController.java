package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.becycled.backend.exception.AuthException;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.post.Post;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Suren Kalaychyan
 * @author I1yi4
 */
@RestController
@RequestMapping("/posts")
@Api(description = "Статьи")
public class PostController {

    private final DaoFactory daoFactory;
    private final AccessService accessService;

    @Autowired
    public PostController(final DaoFactory daoFactory,
                          final AccessService accessService) {
        this.daoFactory = daoFactory;
        this.accessService = accessService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить статью по ее идентификатору")
    public ResponseEntity<Post> getById(
        @ApiParam("Идентификатор статьи") @PathVariable("id") final int id) {

        final Post post = daoFactory.getPostDao().getById(id);
        if (post == null) {
            throw new NotFoundException(ErrorMessages.notFound(Post.class));
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
    public ResponseEntity<Post> update(
        @ApiParam("Идентификатор статьи") @PathVariable("id") final int id,
        @ApiParam("Данные статьи") @RequestBody final Post entity) {

        if (id != entity.getId()) {
            throw new WrongRequestException(ErrorMessages.differentIdentifierInPathAndBody());
        }

        final User curUser = accessService.getCurrentAuthUser();
        if (curUser == null) {
            throw new AuthException(ErrorMessages.authError());
        }

        final Post post = daoFactory.getPostDao().getById(id);
        if (post == null) {
            throw new NotFoundException(ErrorMessages.notFound(Post.class));
        }

        if (!post.getUserId().equals(curUser.getId())) {
            throw new WrongRequestException(ErrorMessages.onlyOwnerCanUpdateEntity(Post.class));
        }

        return ResponseEntity.ok(daoFactory.getPostDao().update(entity));
    }
}
