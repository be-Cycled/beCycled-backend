package me.becycled.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PingController {

    @RequestMapping(path = "/ping", method = GET, produces = "application/json")
    public String ping() {
        return "pong";
    }
}
