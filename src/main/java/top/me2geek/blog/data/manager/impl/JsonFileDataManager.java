package top.me2geek.blog.data.manager.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import top.me2geek.blog.controller.account.AccountController;

import java.io.File;
import java.util.List;

/**
 * @author LangYa466
 * @date 2025/10/6
 */
@Slf4j
public class JsonFileDataManager implements ManagerImpl {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File dataFile;

    public JsonFileDataManager(File dataFile) {
        this.dataFile = dataFile;
    }

    // 默认
    public JsonFileDataManager() {
        this.dataFile = new File("data.json");
    }

    @Override
    public void init() throws Exception {
        if (!dataFile.exists()) {
            if (!dataFile.getParentFile().mkdirs() || !dataFile.createNewFile()) {
                log.error("创建数据文件失败：{}", dataFile.getAbsolutePath());
            }
            // 空数组
            mapper.writeValue(dataFile, List.of());
            log.info("初始化数据文件成功：{}", dataFile.getAbsolutePath());
        }

        AccountController.adminAccount = mapper.readValue(dataFile, new TypeReference<>() {});
    }

    @Override
    public void destroy() throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, AccountController.adminAccount);
    }
}
