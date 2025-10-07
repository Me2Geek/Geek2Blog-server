package top.me2geek.blog.data;

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
public class PostRequest {
    private String token;
    private Post post;
}
