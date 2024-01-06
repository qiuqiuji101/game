package cn.tf.code.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HtmlUtil;
import cn.tf.code.consts.BaseConsts;
import lombok.Getter;
import lombok.Setter;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
public class TfResPipeline extends ConsolePipeline {

    private String pdfbasePath;
    public TfResPipeline(String pdfbasePath){
        pdfbasePath = pdfbasePath;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String htmlStr = resultItems.get("htmlStr");

        String htmlContent = HtmlUtil.removeHtmlTag(htmlStr,true,"script","style","title","a");
        htmlContent = HtmlUtil.cleanHtmlTag(htmlContent);
        String[] lines = htmlContent.split("\\r?\\n");
        htmlContent = Arrays.stream(lines)
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.joining("\n"));

        String pdfFullPath = BaseConsts.PDF_BASE_PATH + "/" + IdUtil.fastSimpleUUID() + ".pdf";
        PdfUtil.genPdf(htmlContent, pdfFullPath);
    }
}
