package top.me2geek.blog.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class LoginForm {
    private String username;
    private String password;
}
