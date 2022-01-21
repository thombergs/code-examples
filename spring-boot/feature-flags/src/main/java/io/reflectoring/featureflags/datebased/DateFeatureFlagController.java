package io.reflectoring.featureflags.datebased;

import io.reflectoring.featureflags.FeatureFlagService;
import io.reflectoring.featureflags.web.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Controller
public class DateFeatureFlagController {

    private final UserSession userSession;
    private final FeatureFlagService featureFlagService;

    DateFeatureFlagController(UserSession userSession, FeatureFlagService featureFlagService) {
        this.userSession = userSession;
        this.featureFlagService = featureFlagService;
    }

    @GetMapping(path = {"/welcome"})
    ModelAndView welcome() {
        userSession.login("ben");

        Optional<LocalDateTime> date = featureFlagService.currentDateForWelcomeMessage();

        if (date.isEmpty()) {
            return new ModelAndView("/welcome-page-without-message.html");
        }

        LocalTime time = date.get().toLocalTime();
        String welcomeMessage = "";

        if (time.isBefore(LocalTime.NOON)) {
            welcomeMessage = "Good Morning!";
        } else if (time.isBefore(LocalTime.of(17, 0))) {
            welcomeMessage = "Good Day!";
        } else {
            welcomeMessage = "Good Evening!";
        }

        return new ModelAndView("/welcome-page.html", Map.of("welcomeMessage", welcomeMessage));
    }

}
