package top.me2geek.blog.service.impl;

import org.springframework.stereotype.Service;
import top.me2geek.blog.controller.account.AccountController;
import top.me2geek.blog.data.LoginForm;
import top.me2geek.blog.service.UserService;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public String login(LoginForm loginForm) {
        LoginForm adminAccount = AccountController.adminAccount;

        if (loginForm.getUsername().equals(adminAccount.getUsername()) && loginForm.getPassword().equals(adminAccount.getPassword())) {
            return "success";
        }
        return "failed";
    }
}
