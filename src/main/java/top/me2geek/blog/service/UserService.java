package top.me2geek.blog.service;

import top.me2geek.blog.data.LoginForm;

public interface UserService {

    /**
     * Login with username and password.
     * @param loginForm LoginForm object
     * @return token
     */
    String login(LoginForm loginForm);

}
