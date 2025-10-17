package FoodSeer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FoodSeer.service.UserService;

@CrossOrigin ( "*" )
@RequestMapping ( "/auth" )
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // @GetMapping ( "/me" )
    // public ResponseEntity<User> getCurrentUser () {
    // return new ResponseEntity<User>( userService.fetchCurrentUser(),
    // HttpStatus.CREATED );
    // }

}
