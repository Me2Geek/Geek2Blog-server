package top.me2geek.blog.data.manager.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import top.me2geek.blog.controller.account.AccountController;
import top.me2geek.blog.controller.post.PostController;
import top.me2geek.blog.data.LoginForm;
import top.me2geek.blog.data.Post;

import java.io.File;
import java.util.ArrayList;

@Slf4j
public class JsonFileDataManager implements ManagerImpl {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File adminFile;
    private final File postsFile;

    public JsonFileDataManager(File adminFile, File postsFile) {
        this.adminFile = adminFile;
        this.postsFile = postsFile;
    }

    // 默认文件
    public JsonFileDataManager() {
        this.adminFile = new File("admin.json");
        this.postsFile = new File("posts.json");
    }

    @Override
    public void init() throws Exception {
        if (!adminFile.exists()) {
            if (!adminFile.createNewFile()) {
                log.error("创建管理员账户文件失败：{}", adminFile.getAbsolutePath());
            }
            mapper.writeValue(adminFile, new LoginForm("admin", "admin", ""));
            log.info("初始化管理员账户文件成功：{}", adminFile.getAbsolutePath());
        }

        if (!postsFile.exists()) {
            if (!postsFile.createNewFile()) {
                log.error("创建文章数据文件失败：{}", postsFile.getAbsolutePath());
            }
            mapper.writeValue(postsFile, new ArrayList<Post>());
            log.info("初始化文章数据文件成功：{}", postsFile.getAbsolutePath());
        }

        AccountController.adminAccount = mapper.readValue(adminFile, LoginForm.class);
        log.info("加载管理员账户成功：{}", AccountController.adminAccount);

        PostController.posts = mapper.readValue(postsFile, new TypeReference<>() {});
        log.info("加载文章列表成功 共 {} 篇", PostController.posts.size());
    }

    @Override
    public void destroy() throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(adminFile, AccountController.adminAccount);
        log.info("保存管理员账户成功：{}", AccountController.adminAccount);

        mapper.writerWithDefaultPrettyPrinter().writeValue(postsFile, PostController.posts);
        log.info("保存文章列表成功 共 {} 篇", PostController.posts.size());
    }
}
