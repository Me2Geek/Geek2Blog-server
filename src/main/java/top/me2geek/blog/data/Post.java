package top.me2geek.blog.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author LangYa466
 * @date 2025/10/7
 */
@AllArgsConstructor
@Data
@ToString
public class Post {
    private int id;
    @Schema(description = "无需用户权限传入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String title;
    @Schema(description = "无需用户权限传入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String content;
}
