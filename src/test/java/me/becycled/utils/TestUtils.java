package me.becycled.utils;

import me.becycled.backend.model.entity.community.Community;
import me.becycled.backend.model.entity.community.CommunityType;
import me.becycled.backend.model.entity.event.Event;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.entity.image.Image;
import me.becycled.backend.model.entity.post.Post;
import me.becycled.backend.model.entity.route.Route;
import me.becycled.backend.model.entity.route.RoutePhoto;
import me.becycled.backend.model.entity.route.SportType;
import me.becycled.backend.model.entity.telemetry.Telemetry;
import me.becycled.backend.model.entity.telemetry.Tracker;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * @author I1yi4
 */
public enum TestUtils {;

    public static User getTestUser() {
        final User user = new User();
        user.setLogin("login");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@gmail.com");
        user.setPhone("88005553535");
        user.setAbout("about");
        return user;
    }

    public static Route getTestRoute() {
        final Route route = new Route();
        route.setUserId(1);
        route.setName("name");
        route.setRouteGeoData("{\n" +
            "  \"type\": \"FeatureCollection\",\n" +
            "  \"features\": [\n" +
            "    {\n" +
            "      \"type\": \"Feature\",\n" +
            "      \"properties\": {\n" +
            "        \"creator\": \"BRouter-1.1\",\n" +
            "        \"name\": \"brouter_trekking-ignore-cr_0\",\n" +
            "        \"track-length\": \"2750\",\n" +
            "        \"filtered ascend\": \"4\",\n" +
            "        \"plain-ascend\": \"3\",\n" +
            "        \"total-time\": \"497\",\n" +
            "        \"total-energy\": \"47059\",\n" +
            "        \"cost\": \"4205\",\n" +
            "        \"messages\": [\n" +
            "          [\"Longitude\", \"Latitude\", \"Elevation\", \"Distance\", \"CostPerKm\", \"ElevCost\", \"TurnCost\", \"NodeCost\", \"InitialCost\", \"WayTags\", \"NodeTags\", \"Time\", \"Energy\"],\n" +
            "          [\"38982083\", \"45088754\", \"29\", \"396\", \"1450\", \"0\", \"0\", \"0\", \"0\", \"reversedirection=yes highway=tertiary\", \"\", \"59\", \"5921\"],\n" +
            "          [\"38979098\", \"45075600\", \"31\", \"1488\", \"1150\", \"0\", \"89\", \"0\", \"0\", \"highway=residential\", \"\", \"325\", \"32541\"],\n" +
            "          [\"38978992\", \"45075603\", \"31\", \"9\", \"1450\", \"0\", \"77\", \"0\", \"0\", \"highway=tertiary\", \"highway=crossing crossing=uncontrolled\", \"326\", \"32688\"],\n" +
            "          [\"38976986\", \"45075639\", \"30\", \"159\", \"1450\", \"0\", \"1\", \"0\", \"0\", \"highway=tertiary\", \"\", \"350\", \"35030\"],\n" +
            "          [\"38973123\", \"45072197\", \"32\", \"643\", \"1450\", \"0\", \"71\", \"0\", \"0\", \"reversedirection=yes highway=tertiary\", \"\", \"463\", \"46392\"],\n" +
            "          [\"38973123\", \"45071884\", \"32\", \"36\", \"5050\", \"0\", \"70\", \"0\", \"0\", \"reversedirection=yes highway=footway\", \"\", \"489\", \"46782\"],\n" +
            "          [\"38973037\", \"45071888\", \"32\", \"7\", \"5050\", \"0\", \"96\", \"0\", \"0\", \"reversedirection=yes highway=footway surface=asphalt\", \"highway=crossing crossing=traffic_signals\", \"494\", \"46853\"],\n" +
            "          [\"38973043\", \"45072190\", \"32\", \"12\", \"3050\", \"0\", \"85\", \"0\", \"0\", \"highway=primary surface=asphalt oneway=yes\", \"\", \"496\", \"47059\"]\n" +
            "        ],\n" +
            "        \"times\": [0,14.309,15.07,27.649,46.586,59.211,74.383,90.868,100.961,111.422,121.049,130.727,156.531,168.852,180.652,203.313,216.589,231.986,262.129,272.061,281.971,300.937,311.495,324.062,325.359,326.824,349.015,350.25,350.832,351.418,352.155,352.896,359.433,371.735,376.082,390.466,399.292,400.27,401.435,403.489,404.298,405.302,417.356,424.923,425.987,427.93,428.81,430.213,434.152,435.305,438.292,440.157,441.82,443.389,445.516,453.264,461.616,463.861,469.659,489.752,494.772,496.836]\n" +
            "      },\n" +
            "      \"geometry\": {\n" +
            "        \"type\": \"LineString\",\n" +
            "        \"coordinates\": [\n" +
            "          [38.977148, 45.089295, 29.5],\n" +
            "          [38.978921, 45.089100, 28.75],\n" +
            "          [38.978976, 45.089094, 28.75],\n" +
            "          [38.979883, 45.088996, 29.25],\n" +
            "          [38.981199, 45.088855, 29.5],\n" +
            "          [38.982083, 45.088754, 29.75],\n" +
            "          [38.981948, 45.088115, 30.75],\n" +
            "          [38.981795, 45.087477, 31.75],\n" +
            "          [38.981648, 45.086872, 30.25],\n" +
            "          [38.981493, 45.086230, 29.75],\n" +
            "          [38.981343, 45.085628, 29.25],\n" +
            "          [38.981199, 45.085045, 29.0],\n" +
            "          [38.980970, 45.083889, 30.25],\n" +
            "          [38.980827, 45.083314, 30.75],\n" +
            "          [38.980687, 45.082737, 30.75],\n" +
            "          [38.980450, 45.081522, 30.5],\n" +
            "          [38.980337, 45.080988, 31.75],\n" +
            "          [38.980190, 45.080417, 32.75],\n" +
            "          [38.979907, 45.079250, 33.5],\n" +
            "          [38.979788, 45.078691, 33.0],\n" +
            "          [38.979654, 45.078098, 32.5],\n" +
            "          [38.979404, 45.076933, 32.0],\n" +
            "          [38.979259, 45.076367, 32.0],\n" +
            "          [38.979115, 45.075666, 31.75],\n" +
            "          [38.979098, 45.075600, 31.75],\n" +
            "          [38.978992, 45.075603, 31.75],\n" +
            "          [38.977077, 45.075646, 31.0],\n" +
            "          [38.976986, 45.075639, 30.75],\n" +
            "          [38.976943, 45.075620, 30.5],\n" +
            "          [38.976913, 45.075597, 30.5],\n" +
            "          [38.976891, 45.075556, 30.5],\n" +
            "          [38.976884, 45.075512, 30.5],\n" +
            "          [38.976874, 45.075134, 30.5],\n" +
            "          [38.976846, 45.074534, 31.25],\n" +
            "          [38.976805, 45.074332, 31.5],\n" +
            "          [38.976792, 45.073479, 30.5],\n" +
            "          [38.976758, 45.073061, 31.25],\n" +
            "          [38.976737, 45.073021, 31.5],\n" +
            "          [38.976691, 45.072989, 31.5],\n" +
            "          [38.976592, 45.072944, 31.75],\n" +
            "          [38.976552, 45.072931, 31.75],\n" +
            "          [38.976494, 45.072924, 31.75],\n" +
            "          [38.975616, 45.072929, 31.25],\n" +
            "          [38.975095, 45.072934, 31.5],\n" +
            "          [38.975029, 45.072927, 31.5],\n" +
            "          [38.974907, 45.072888, 31.5],\n" +
            "          [38.974870, 45.072860, 31.5],\n" +
            "          [38.974814, 45.072800, 31.5],\n" +
            "          [38.974719, 45.072600, 31.25],\n" +
            "          [38.974698, 45.072544, 31.25],\n" +
            "          [38.974647, 45.072403, 31.5],\n" +
            "          [38.974583, 45.072335, 31.75],\n" +
            "          [38.974490, 45.072288, 31.75],\n" +
            "          [38.974399, 45.072258, 32.0],\n" +
            "          [38.974269, 45.072243, 32.0],\n" +
            "          [38.973787, 45.072221, 32.25],\n" +
            "          [38.973258, 45.072217, 32.5],\n" +
            "          [38.973123, 45.072197, 32.75],\n" +
            "          [38.973122, 45.072132, 32.5],\n" +
            "          [38.973123, 45.071884, 32.25],\n" +
            "          [38.973037, 45.071888, 32.25],\n" +
            "          [38.973039, 45.071988, 32.25]\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}\n");
        route.setRoutePreview("route preview");
        route.setSportTypes(List.of(SportType.BICYCLE, SportType.RUN));
        route.setDisposable(false);
        route.setDescription("description");
        route.setPopularity(100);
        return route;
    }

    public static RoutePhoto getTestRoutePhoto() {
        final RoutePhoto routePhoto = new RoutePhoto();
        routePhoto.setRouteId(1);
        routePhoto.setPhoto("photo");
        return routePhoto;
    }

    public static Community getTestCommunity() {
        final Community community = new Community();
        community.setOwnerUserId(1);
        community.setName("name");
        community.setNickname("nickname");
        community.setAvatar("avatar");
        community.setCommunityType(CommunityType.ORGANIZATION);
        community.setSportTypes(List.of(SportType.BICYCLE, SportType.RUN));
        community.setUserIds(List.of(1));
        community.setUrl("url");
        community.setDescription("description");
        return community;
    }

    public static Telemetry getTestTelemetry() {
        return Telemetry.builder()
            .withTrackerId(1)
            .withFixTime(Instant.parse("2021-06-18T12:00:00Z"))
            .withServerTime(Instant.parse("2021-06-18T12:01:30Z"))
            .withLatitude(43.414414)
            .withLongitude(39.949160)
            .withAltitude(10.0)
            .withSpeed(60)
            .withCourse(180)
            .build();
    }

    public static Tracker getTestTracker() {
        final Tracker tracker = new Tracker();
        tracker.setId(1);
        tracker.setUserId(1);
        tracker.setImei("100000000000000");
        return tracker;
    }

    public static Event getTestEvent() {
        final Event event = new Event();
        event.setOwnerUserId(1);
        event.setPrivate(false);
        event.setEventType(EventType.WORKOUT);
        event.setRouteId(1);
        event.setSportType(SportType.BICYCLE);
        event.setUserIds(Collections.emptyList());
        event.setStartDate(Instant.parse("2021-06-19T00:00:00Z"));
        event.setDescription("description");
        event.setVenueGeoData("venueGeoData");
        event.setDuration(100500);
        return event;
    }

    public static Post getTestPost() {
        Post post = new Post();
        post.setUserId(1);
        post.setTitle("title");
        post.setContent("content");
        post.setPoster("poster");
        return post;
    }

    public static Image getTestImage() {
        Image image = new Image();
        image.setFileName("test.jpg");
        image.setData(new byte[]{0x00, 0x55});
        return image;
    }

    public static UserAccount getTestUserAccount() {
        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setPassword("password");
        userAccount.setLastAuthTime(Instant.parse("2021-06-18T12:00:00Z"));
        return userAccount;
    }
}
