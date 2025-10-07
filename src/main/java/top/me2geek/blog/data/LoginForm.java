package top.me2geek.blog.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author LangYa466
 * @date 2025/10/7
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class LoginForm {
    private String username;
    private String password;
    private String token;
}
