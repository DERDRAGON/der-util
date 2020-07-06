package com.der.dertool.docs;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-06 10:11
 */
public class DocsGenerator {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("E:\\ideaworkspace2\\der-tool\\src\\main\\java"); // 项目根目录
        config.setProjectName("这是我的一个演示项目"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath("E:\\ideaworkspace2\\der-tool\\src\\main\\java\\com\\der\\dertool\\docs"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        config.addPlugin(new MarkdownDocPlugin());
        Docs.buildHtmlDocs(config); // 执行生成文档
    }

}
