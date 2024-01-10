package org.lekitech.gafalag.controller.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {

    @GetMapping("/user")
    public String helloUser() {
        return "Hello User";
    }

    @GetMapping("/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }

}
