package me.becycled.backend.controller;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.competition.Competition;
import me.becycled.backend.model.entity.user.User;
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
@RequestMapping("/competitions")
public class CompetitionController {

    private final DaoFactory daoFactory;

    @Autowired
    public CompetitionController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") final int id) {
        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Not found competition", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(competition);
    }

    @RequestMapping(value = "/community/{nickname}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByCommunityNickname(@PathVariable("nickname") final String nickname) {
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getByCommunityNickname(nickname));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Competition>> getAll() {
        return ResponseEntity.ok(daoFactory.getCompetitionDao().getAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Competition> create(@RequestBody final Competition entity) {
        return ResponseEntity.ok(daoFactory.getCompetitionDao().create(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id") final int id,
                                    @RequestBody final Competition entity) {
        if (id != entity.getId()) {
            return new ResponseEntity<>("Different identifiers in request path and body", HttpStatus.BAD_REQUEST);
        }

        final User curUser = daoFactory.getUserDao().getByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (curUser == null) {
            return new ResponseEntity<>("Auth error", HttpStatus.BAD_REQUEST);
        }

        final Competition competition = daoFactory.getCompetitionDao().getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Competition not exist", HttpStatus.NOT_FOUND);
        }
        if (!competition.getOwnerUserId().equals(curUser.getId())) {
            return new ResponseEntity<>("Only owner can update route", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(daoFactory.getCompetitionDao().update(entity));
    }
}
