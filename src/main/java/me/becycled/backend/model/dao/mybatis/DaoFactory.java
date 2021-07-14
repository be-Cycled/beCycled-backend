package me.becycled.backend.model.dao.mybatis;

import me.becycled.backend.model.dao.mybatis.community.CommunityDao;
import me.becycled.backend.model.dao.mybatis.event.EventDao;
import me.becycled.backend.model.dao.mybatis.image.ImageDao;
import me.becycled.backend.model.dao.mybatis.post.PostDao;
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
@SuppressWarnings("ClassDataAbstractionCoupling")
public class DaoFactory {

    private final UserDao userDao;
    private final UserAccountDao userAccountDao;
    private final RouteDao routeDao;
    private final RoutePhotoDao routePhotoDao;
    private final CommunityDao communityDao;
    private final TelemetryDao telemetryDao;
    private final TrackerDao trackerDao;
    private final EventDao eventDao;
    private final PostDao postDao;
    private final ImageDao imageDao;

    public DaoFactory(final SqlSessionFactory sqlSessionFactory) {
        userDao = new UserDao(sqlSessionFactory);
        userAccountDao = new UserAccountDao(sqlSessionFactory);
        routeDao = new RouteDao(sqlSessionFactory);
        routePhotoDao = new RoutePhotoDao(sqlSessionFactory);
        communityDao = new CommunityDao(sqlSessionFactory);
        telemetryDao = new TelemetryDao(sqlSessionFactory);
        trackerDao = new TrackerDao(sqlSessionFactory);
        eventDao = new EventDao(sqlSessionFactory);
        postDao = new PostDao(sqlSessionFactory);
        imageDao = new ImageDao(sqlSessionFactory);
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

    public EventDao getEventDao() {
        return eventDao;
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public ImageDao getImageDao() {
        return imageDao;
    }
}
