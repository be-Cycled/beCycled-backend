package me.becycled.backend.model.dao.mybatis;

import me.becycled.backend.model.dao.mybatis.community.CommunityDao;
import me.becycled.backend.model.dao.mybatis.route.RouteDao;
import me.becycled.backend.model.dao.mybatis.routephoto.RoutePhotoDao;
import me.becycled.backend.model.dao.mybatis.telemetry.TelemetryDao;
import me.becycled.backend.model.dao.mybatis.tracker.TrackerDao;
import me.becycled.backend.model.dao.mybatis.user.UserDao;
import me.becycled.backend.model.dao.mybatis.useraccount.UserAccountDao;
import me.becycled.backend.model.dao.mybatis.workout.WorkoutDao;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author I1yi4
 */
@SuppressWarnings("ClassDataAbstractionCoupling")
public class DaoFactory {

    private final UserDao userDao;
    private final UserAccountDao userAccountDao;
    private final RouteDao routeDao;
    private final RoutePhotoDao routePhotoDao;
    private final CommunityDao communityDao;
    private final TelemetryDao telemetryDao;
    private final TrackerDao trackerDao;
    private final WorkoutDao workoutDao;

    public DaoFactory(final SqlSessionFactory sqlSessionFactory) {
        userDao = new UserDao(sqlSessionFactory);
        userAccountDao = new UserAccountDao(sqlSessionFactory);
        routeDao = new RouteDao(sqlSessionFactory);
        routePhotoDao = new RoutePhotoDao(sqlSessionFactory);
        communityDao = new CommunityDao(sqlSessionFactory);
        telemetryDao = new TelemetryDao(sqlSessionFactory);
        trackerDao = new TrackerDao(sqlSessionFactory);
        workoutDao = new WorkoutDao(sqlSessionFactory);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserAccountDao getUserAccountDao() {
        return userAccountDao;
    }

    public RouteDao getRouteDao() {
        return routeDao;
    }

    public RoutePhotoDao getRoutePhotoDao() {
        return routePhotoDao;
    }

    public CommunityDao getCommunityDao() {
        return communityDao;
    }

    public TelemetryDao getTelemetryDao() {
        return telemetryDao;
    }

    public TrackerDao getTrackerDao() {
        return trackerDao;
    }

    public WorkoutDao getWorkoutDao() {
        return workoutDao;
    }
}
