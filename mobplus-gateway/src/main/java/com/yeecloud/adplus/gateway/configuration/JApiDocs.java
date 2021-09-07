package com.yeecloud.adplus.gateway.configuration;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * @author: Leonard
 * @create: 2021/9/7
 */
public class JApiDocs {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("D:\\1-project\\1.MobPlus-backend\\git-repo\\mobplus\\mobplus-gateway"); // 项目根目录
        config.setProjectName("gateway"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath("D:\\1-project\\1.MobPlus-backend\\git-repo\\jApiDocs"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.FALSE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }
}
