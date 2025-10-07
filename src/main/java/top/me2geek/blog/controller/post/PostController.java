package top.me2geek.blog.controller.post;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.me2geek.blog.common.ApiResponse;
import top.me2geek.blog.controller.account.AccountController;
import top.me2geek.blog.data.Post;
import top.me2geek.blog.data.PostRequest;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@RestController
@RequestMapping("/post")
public class PostController {
    public static List<Post> posts = new CopyOnWriteArrayList<>();

    @PostMapping("/get")
    public ApiResponse<List<Post>> onGet(@RequestBody Post request) {
        int id = request.getId();

        if (posts.isEmpty()) return ApiResponse.fail("没有文章");

        if (id <= 0) return ApiResponse.success("获取成功", posts);

        if (id > posts.size()) {
            return ApiResponse.fail("请求数量超过文章总数");
        }

        List<Post> result = posts.subList(0, id);
        return ApiResponse.success("获取成功", result);
    }
    @PostMapping("/add")
    public ApiResponse<Post> onAdd(@RequestBody PostRequest postRequest) {
        if (isNotAdmin(postRequest.getToken())) return ApiResponse.fail("没有权限");
        Post post = postRequest.getPost();
        post.setId(posts.size() + 1);
        posts.add(post);
        return ApiResponse.success("添加成功", post);
    }

    @PostMapping("/delete")
    public ApiResponse<String> onDelete(@RequestBody PostRequest postRequest) {
        if (isNotAdmin(postRequest.getToken())) return ApiResponse.fail("没有权限");
        Post post = postRequest.getPost();

        if (posts.isEmpty()) return ApiResponse.fail("没有可删除的文章");

        boolean removed = posts.removeIf(p -> p.getId() == post.getId());

        if (!removed) return ApiResponse.fail("未找到要删除的文章 ID: " + post.getId());

        for (int i = 0; i < posts.size(); i++) {
            posts.get(i).setId(i + 1);
        }

        return ApiResponse.success("删除成功", null);
    }

    @PostMapping("/update")
    public ApiResponse<Post> onUpdate(@RequestBody PostRequest postRequest) {
        if (isNotAdmin(postRequest.getToken())) return ApiResponse.fail("没有权限");
        Post post = postRequest.getPost();

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                return ApiResponse.success("更新成功", post);
            }
        }
        return ApiResponse.fail("更新失败");
    }

    @PostMapping("/search")
    public ApiResponse<List<Post>> onSearch(@RequestBody Post request) {
        String keyword = request.getContent();

        if (keyword == null || keyword.isEmpty()) return ApiResponse.fail("搜索关键字不能为空");

        if (posts.isEmpty()) return ApiResponse.fail("没有文章");

        if (keyword.length() > 20) return ApiResponse.fail("搜索关键字过长");

        List<Post> result = new CopyOnWriteArrayList<>();
        for (Post post : posts) {
            if (post.getTitle().contains(keyword) || post.getContent().contains(keyword)) {
                result.add(post);
            }
        }
        if (result.isEmpty()) return ApiResponse.fail("没有搜索结果");
        return ApiResponse.success("搜索成功", result);
    }

    private boolean isNotAdmin(String token) {
        return !AccountController.adminAccount.getToken().equals(token);
    }
}
