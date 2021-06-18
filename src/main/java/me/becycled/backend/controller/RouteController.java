package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.User;
import me.becycled.backend.model.entity.route.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/routes")
public class RouteController {

    private final DaoFactory daoFactory;

    @Autowired
    public RouteController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Route route = daoFactory.getRouteDao().getById(id);
        if (route == null) {
            return new ResponseEntity<>("Not found route", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(route);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Route>> getAll() {
        return ResponseEntity.ok(daoFactory.getRouteDao().getAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id") final int id,
                                    @RequestBody final Route entity) {
        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.BAD_REQUEST);
        }

        final Route route = daoFactory.getRouteDao().getById(id);
        if (route == null) {
            return new ResponseEntity<>("Route not exist", HttpStatus.NOT_FOUND);
        }
        if (!route.getUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Only owner can update route", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(daoFactory.getRouteDao().update(entity));
    }
}
