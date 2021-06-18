package me.becycled.backend.model.dao.mybatis;

import me.becycled.backend.model.dao.mybatis.route.RouteDao;
import me.becycled.backend.model.dao.mybatis.routephoto.RoutePhotoDao;
import me.becycled.backend.model.dao.mybatis.telemetry.TelemetryDao;
import me.becycled.backend.model.dao.mybatis.tracker.TrackerDao;
import me.becycled.backend.model.dao.mybatis.user.UserDao;
import me.becycled.backend.model.dao.mybatis.useraccount.UserAccountDao;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author I1yi4
 */
public class DaoFactory {

    private final UserDao userDao;
    private final UserAccountDao userAccountDao;
    private final RouteDao routeDao;
    private final RoutePhotoDao routePhotoDao;
    private final TelemetryDao telemetryDao;
    private final TrackerDao trackerDao;

    public DaoFactory(final SqlSessionFactory sqlSessionFactory) {
        userDao = new UserDao(sqlSessionFactory);
        userAccountDao = new UserAccountDao(sqlSessionFactory);
        routeDao = new RouteDao(sqlSessionFactory);
        routePhotoDao = new RoutePhotoDao(sqlSessionFactory);
        telemetryDao = new TelemetryDao(sqlSessionFactory);
        trackerDao = new TrackerDao(sqlSessionFactory);
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
    public TelemetryDao getTelemetryDao() {
        return telemetryDao;
    }
    public TrackerDao getTrackerDao() {
        return trackerDao;
    }
}
