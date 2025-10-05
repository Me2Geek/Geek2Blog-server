package top.me2geek.blog.service;

import org.springframework.web.bind.annotation.RequestBody;
import top.me2geek.blog.data.LoginForm;

public interface UserService {

    /**
     * Login with username and password.
     * @param loginForm LoginForm object
     * @return token
     */
    String login(@RequestBody LoginForm loginForm);

}
