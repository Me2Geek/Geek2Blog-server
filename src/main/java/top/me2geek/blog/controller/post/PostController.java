package top.me2geek.blog.controller.post;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.me2geek.blog.common.ApiResponse;
import top.me2geek.blog.controller.account.AccountController;
import top.me2geek.blog.data.Post;
import top.me2geek.blog.data.PostRequest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
        var id = request.getId();

        if (posts.isEmpty()) {
            return ApiResponse.fail("没有文章");
        }

        var copiedPosts = posts.stream()
                .map(p -> {
                    Post copy = new Post();
                    BeanUtils.copyProperties(p, copy);
                    return copy;
                })
                .collect(Collectors.toList());

        if (id <= 0) {
            copiedPosts.forEach(p -> p.setContent(""));
            return ApiResponse.success("获取成功", copiedPosts);
        }

        if (id > copiedPosts.size()) {
            return ApiResponse.fail("请求数量超过文章总数");
        }

        var result = copiedPosts.stream()
                .filter(p -> p.getId() == id)
                .peek(p -> p.setContent(normalizeMarkdown(p.getContent())))
                .collect(Collectors.toList());

        return ApiResponse.success("获取成功", result);
    }


    @PostMapping("/add")
    public ApiResponse<Post> onAdd(@RequestBody PostRequest postRequest) {
        if (isNotAdmin(postRequest.getToken())) return ApiResponse.fail("没有权限");
        var post = postRequest.getPost();
        post.setId(posts.size() + 1);
        posts.add(post);
        return ApiResponse.success("添加成功", post);
    }

    @PostMapping("/delete")
    public ApiResponse<String> onDelete(@RequestBody PostRequest postRequest) {
        if (isNotAdmin(postRequest.getToken())) return ApiResponse.fail("没有权限");
        var post = postRequest.getPost();

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
        var post = postRequest.getPost();

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                return ApiResponse.success("更新成功", post);
            }
        }
        return ApiResponse.fail("更新失败");
    }

    @PostMapping("/search")
    public ApiResponse<CopyOnWriteArrayList<Object>> onSearch(@RequestBody Post request) {
        var keyword = request.getContent();

        if (keyword == null || keyword.isEmpty()) return ApiResponse.fail("搜索关键字不能为空");

        if (posts.isEmpty()) return ApiResponse.fail("没有文章");

        if (keyword.length() > 20) return ApiResponse.fail("搜索关键字过长");

        var result = new CopyOnWriteArrayList<>();
        for (Post post : posts) {
            if (post.getTitle().contains(keyword) || post.getContent().contains(keyword)) {
                result.add(post);
            }
        }
        if (result.isEmpty()) return ApiResponse.fail("没有搜索结果");
        return ApiResponse.success("搜索成功", result);
    }

    @PostMapping("/uploadImage")
    public ApiResponse<String> uploadImage(
            @RequestParam("token") String token,
            @RequestPart("file") MultipartFile file) {

        if (isNotAdmin(token)) return ApiResponse.fail("没有权限");

        if (file == null || file.isEmpty()) return ApiResponse.fail("文件为空");

        try {
            var originalFilename = file.getOriginalFilename();
            var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

            var allowedTypes = Arrays.asList("webp", "png", "jpg", "jpeg", "apng", "gif");
            if (!allowedTypes.contains(extension)) {
                return ApiResponse.fail("不支持的图片格式，只支持 webp/png/jpg/jpeg/apng/gif");
            }
            var basePath = System.getProperty("user.dir") + File.separator + "images";
            File imageDir = new File(basePath);
            if (!imageDir.exists() && !imageDir.mkdirs()) {
                return ApiResponse.fail("创建图片目录失败");
            }

            var newFileName = System.currentTimeMillis() + "." + extension;
            var dest = new File(imageDir, newFileName);

            file.transferTo(dest);

            var imagePath = "/images/" + newFileName;
            return ApiResponse.success("上传成功", imagePath);

        } catch (IOException e) {
            return ApiResponse.fail("上传失败：" + e.getMessage());
        }
    }

    private boolean isNotAdmin(String token) {
        return !AccountController.adminAccount.getToken().equals(token);
    }

    private String normalizeMarkdown(String content) {
        if (content == null) return "";

        content = content.replace("\r\n", "\n").replace("\\n", "\n");

        content = content.replaceAll("(?m)^-$", "");

        content = content.replaceAll("(#{1,6} .+)\\n(?!\\n|#)", "$1\n\n");

        content = content.replaceAll("(?m)^(---|\\*\\*\\*|___)$", "\n$1\n");

        content = content.replaceAll("\\n{3,}", "\n\n");

        return content.trim();
    }
}
