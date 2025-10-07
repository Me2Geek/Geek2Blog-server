package top.me2geek.blog.controller.account;

import org.springframework.web.bind.annotation.*;
import top.me2geek.blog.common.ApiResponse;
import top.me2geek.blog.data.LoginForm;

import java.util.UUID;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    public static LoginForm adminAccount = new LoginForm("admin", "admin", "adminToken");

    @PostMapping("/login")
    public ApiResponse<String> onLogin(@RequestBody LoginForm loginForm) {
        if (loginForm.getUsername().length() > 20 || loginForm.getPassword().length() > 20) {
            return ApiResponse.fail("用户名或密码过长");
        }
        String token = handleLogin(loginForm);
        if (token != null) {
            return ApiResponse.success("登录成功", token);
        }
        return ApiResponse.fail("错误的用户名或密码");
    }

    private String handleLogin(LoginForm loginForm) {
        if (adminAccount == null) return null;

        // 防止demo服务器被爆破
        if (adminAccount.getUsername().equals("DEMODEMODEMODEMDOEMODE"))  {
            adminAccount.setToken(generateAPIToken());
            return null;
        }
        if (loginForm.getUsername().equals(adminAccount.getUsername()) &&
                loginForm.getPassword().equals(adminAccount.getPassword())) {
            adminAccount.setToken(generateAPIToken());
            return adminAccount.getToken();
        }

        return null;
    }

    private String generateAPIToken() {
        return (UUID.randomUUID().toString() + UUID.randomUUID()).replace("-", "");
    }
}
