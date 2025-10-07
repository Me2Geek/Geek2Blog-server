package top.me2geek.blog.data;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "无需客户端传入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String token;
}
