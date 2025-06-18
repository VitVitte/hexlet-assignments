package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
public class WelcomeController {

    private final Daytime daytime;

    @Autowired
    public WelcomeController(Daytime daytime) {
        this.daytime = daytime;
    }

    @GetMapping("/welcome")
    public String welcome() {
        String greeting;
        if ("night".equals(daytime.getName())) {
            greeting = "It is night now! Welcome to Spring!";
        } else {
            greeting = "Good day! Welcome to Spring!";
        }
        return greeting;
    }
}
// END
