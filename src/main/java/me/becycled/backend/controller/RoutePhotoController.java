package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.RoutePhoto;
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
@RequestMapping("/route-photos")
@Api(description = "Фотографии маршрутов")
public class RoutePhotoController {

    private final DaoFactory daoFactory;

    @Autowired
    public RoutePhotoController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить фотографию маршрута по ее идентификатору")
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().getById(id);
        if (routePhoto == null) {
            return new ResponseEntity<>("Route photo is not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(routePhoto);
    }

    @RequestMapping(value = "/route/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить список фотографий по идентификатору маршрута")
    public ResponseEntity<?> getByRouteId(@PathVariable("id") final int id) {
        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().getByRouteId(id));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Получить все фотографии всех маршрутов")
    public ResponseEntity<List<RoutePhoto>> getAll() {
        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Создать фотографию маршрута")
    public ResponseEntity<RoutePhoto> create(@RequestBody final RoutePhoto entity) {
        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Удалить фотографию маршрута по ее идентификатору")
    public ResponseEntity<?> delete(@PathVariable("id") final int id) {
        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.UNAUTHORIZED);
        }

        final RoutePhoto routePhoto = daoFactory.getRoutePhotoDao().getById(id);
        if (routePhoto == null) {
            return new ResponseEntity<>("Route photo is not exist", HttpStatus.NOT_FOUND);
        }

        final Route route = daoFactory.getRouteDao().getById(routePhoto.getRouteId());
        if (route == null) {
            return new ResponseEntity<>("Route is not exist", HttpStatus.NOT_FOUND);
        }

        if (!route.getUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Route photo can be deleted by owner only", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(daoFactory.getRoutePhotoDao().delete(id));
    }
}
