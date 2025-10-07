package top.me2geek.blog.service.impl;

import org.springframework.stereotype.Service;
import top.me2geek.blog.controller.account.AccountController;
import top.me2geek.blog.data.LoginForm;
import top.me2geek.blog.service.UserService;

import java.util.UUID;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public String[] login(LoginForm loginForm) {
        String[] result = new String[3];

        LoginForm adminAccount = AccountController.adminAccount;

        if (adminAccount == null) {
            result[0] = "error";
            result[1] = "[系统错误] 管理员账户未初始化";
            return result;
        }

        if (loginForm.getUsername().equals(adminAccount.getUsername()) &&
                loginForm.getPassword().equals(adminAccount.getPassword())) {
            result[0] = "success";
            AccountController.adminAccount.setToken(randomAPIToken());
            result[1] = AccountController.adminAccount.getToken();
        } else {
            result[0] = "failed";
            result[1] = "null";
        }

        return result;
    }

    private String randomAPIToken() {
        return (UUID.randomUUID().toString() + UUID.randomUUID()).replace("-", "");
    }
}
