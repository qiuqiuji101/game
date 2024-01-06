package cn.tf.code.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HtmlUtil;
import cn.tf.code.utils.PdfUtil;
import cn.tf.code.utils.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tf.code.utils.TfPageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Description: pdf文件生成、下载
 */
@Slf4j
@Controller
@RequestMapping("/pdf")
public class PdfController {

    @Value("${cn.tf.store.path}")
    private String basePath;
    @Value("${cn.tf.proxy.host}")
    private String proxyHost;
    @Value("${cn.tf.proxy.port}")
    private int proxyPort;

    private static final String pdfRelPath = "/pdf";
    public static final String zipRelPath = "/zip/top10.zip";
    @Autowired
    private TfPageProcessor tfPageProcessor;

    /**
     * 获取pdf
     */
    @GetMapping("/create")
    @ResponseBody
    public R<String> createPdf() throws IOException {
        System.out.println("create begin");
        String pdfBasePath = basePath + pdfRelPath;
        tfPageProcessor.spider(proxyHost,proxyPort,pdfBasePath);
        //将装换后的页面文字转换为pdf
        //压缩生成zip
        String finalZipPath = basePath + zipRelPath;
        ZipUtil.zip(pdfBasePath, finalZipPath);
        return R.success("over");
    }


    /*
    *文件下载实现-1
    */
    @GetMapping("/download")
    public void filedownload(HttpServletResponse response) throws Exception {
        //指定要下载的文件路径
        String zipPath = basePath + zipRelPath;
        String filename = FilenameUtils.getName(zipPath);
        //创建文件对象
        File file = new File(zipPath);
        FileInputStream fis = new FileInputStream(file);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream out = response.getOutputStream();
        byte[] bs = new byte[1024];
        int len = 0;
        while ((len = fis.read(bs)) != -1) {
            out.write(bs, 0, len);
        }
        out.flush();
        out.close();
    }



    /*
    * 文件下载实现-2
    */
    @GetMapping("/download2")
    public ResponseEntity<byte[]> filedownload(HttpServletRequest request) throws Exception {
        //指定要下载的文件路径
        String zipPath = basePath + zipRelPath;
        String filename = FilenameUtils.getName(zipPath);
        //创建文件对象
        File file = new File(zipPath);
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的形式打开
        headers.setContentDispositionFormData("attachment",filename);
        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<byte[]>(e.getMessage().getBytes(),HttpStatus.EXPECTATION_FAILED);
        }
    }

}
