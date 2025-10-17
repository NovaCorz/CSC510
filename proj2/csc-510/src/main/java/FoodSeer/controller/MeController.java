package FoodSeer.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ( "/api" )
public class MeController {

    @GetMapping ( "/me" )
    public Object me ( final Authentication auth ) {
        return new Object() {
            public final String username = auth.getName();
        };
    }
}
