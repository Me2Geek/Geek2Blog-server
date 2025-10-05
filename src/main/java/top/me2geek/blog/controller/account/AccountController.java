package top.me2geek.blog.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.me2geek.blog.data.LoginForm;
import top.me2geek.blog.service.UserService;

@RestController
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String onLogin(@RequestBody LoginForm loginForm) {
        return userService.login(loginForm);
    }
}
