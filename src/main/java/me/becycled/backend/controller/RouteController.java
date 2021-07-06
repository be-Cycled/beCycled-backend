package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.route.Route;
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
 * @author I1yi4
 */
@RestController
@RequestMapping("/routes")
@Api(description = "Маршруты")
public class RouteController {

    private final DaoFactory daoFactory;

    @Autowired
    public RouteController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить маршрут по его идентификатору")
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Route route = daoFactory.getRouteDao().getById(id);
        if (route == null) {
            return new ResponseEntity<>("Route is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(route);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все маршруты")
    public ResponseEntity<List<Route>> getAll() {
        return ResponseEntity.ok(daoFactory.getRouteDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать маршрут")
    public ResponseEntity<Route> create(@RequestBody final Route entity) {
        return ResponseEntity.ok(daoFactory.getRouteDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Обновить маршрут по его идентификатору")
    public ResponseEntity<?> update(@PathVariable("id") final int id,
                                    @RequestBody final Route entity) {
        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final Route route = daoFactory.getRouteDao().getById(id);
        if (route == null) {
            return new ResponseEntity<>("Route is not exist", HttpStatus.NOT_FOUND);
        }
        if (!route.getUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Route can be updated by owner only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getRouteDao().update(entity));
    }
}
