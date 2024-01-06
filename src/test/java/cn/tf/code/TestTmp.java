package cn.tf.code;

import cn.hutool.http.HtmlUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class TestTmp {
    public static void main(String[] args) throws IOException {
        String comHtmlStr = FileUtils.readFileToString(new File("D:/temp/com.html"), "UTF-8");
        String res = HtmlUtil.removeHtmlTag(comHtmlStr,true,"script","style","title","a");
        res = HtmlUtil.cleanHtmlTag(res);
        String[] lines = res.split("\\r?\\n");
        res = Arrays.stream(lines)
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.joining("\n"));
        System.out.println(res);
    }
}
