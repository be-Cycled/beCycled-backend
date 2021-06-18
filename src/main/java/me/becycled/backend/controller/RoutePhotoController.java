package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.User;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.RoutePhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/route-photos")
public class RoutePhotoController {

    private final DaoFactory daoFactory;

    @Autowired
    public RoutePhotoController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().getById(id);
        if (routePhoto == null) {
            return new ResponseEntity<>("Not found route photo", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(routePhoto);
    }

    @RequestMapping(value = "/route/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByRouteId(@PathVariable("id") final int id) {
        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().getByRouteId(id));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoutePhoto>> getAll() {
        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().getAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id") final int id) {
        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.BAD_REQUEST);
        }

        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().getById(id);
        if (routePhoto == null) {
            return new ResponseEntity<>("Route photo not exist", HttpStatus.NOT_FOUND);
        }

        final Route route = daoFactory.getRouteDao().getById(routePhoto.getRouteId());
        if (route == null) {
            return new ResponseEntity<>("Route photo not exist", HttpStatus.NOT_FOUND);
        }
        if (!route.getUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Only owner can delete photo from route", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().delete(id));
    }
}