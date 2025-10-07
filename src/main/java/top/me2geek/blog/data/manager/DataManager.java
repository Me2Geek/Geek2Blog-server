package top.me2geek.blog.data.manager;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.me2geek.blog.data.manager.impl.JsonFileDataManager;
import top.me2geek.blog.data.manager.impl.ManagerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@Slf4j
@Data
@Component
public class DataManager {
    private final List<ManagerImpl> managers = new ArrayList<>();

    public DataManager() {
        managers.add(new JsonFileDataManager());
    }

    @PostConstruct
    public void init() {
        try {
            for (ManagerImpl manager : managers) {
                manager.init();
            }
        } catch (Exception e) {
            log.error("[数据管理系统] 初始化失败", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            for (ManagerImpl manager : managers) {
                manager.destroy();
            }
            log.info("[数据管理系统] 已保存全部数据");
        } catch (Exception e) {
            log.error("[数据管理系统] 保存异常", e);
        }
    }
}
