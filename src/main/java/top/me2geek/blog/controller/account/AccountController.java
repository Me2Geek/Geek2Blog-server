package top.me2geek.blog.controller.account;

import org.springframework.web.bind.annotation.*;
import top.me2geek.blog.common.ApiResponse;
import top.me2geek.blog.data.LoginForm;
import top.me2geek.blog.service.UserService;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    public static LoginForm adminAccount =  new LoginForm("admin", "admin", "adminToken");
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<String> onLogin(@RequestBody LoginForm loginForm) {
        if (loginForm.toString().length() > 40) {
            return ApiResponse.fail("用户名或密码过长");
        }
        String[] result = userService.login(loginForm);
        if (result[0].equals("success")) {
            return ApiResponse.success("登录成功", result[1]);
        }
        return ApiResponse.fail("错误的用户名或密码");
    }
}
