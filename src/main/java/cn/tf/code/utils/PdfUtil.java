package cn.tf.code.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtil{


    public static void genPdf(String content,String pdfFullPath) {

        FileOutputStream fileOutputStream = null;
        Document document = null;

        try {
            // 创建PDF文档对象 com.itextpdf.text.Document
            document = new Document();

            // 创建文件输出流，注入输出路径位置
            fileOutputStream = new FileOutputStream(pdfFullPath);

            // 由 com.itextpdf.text.pdf.PdfWriter类的获取实例方法得到PDF文档对象和文件输出流对象
            PdfWriter.getInstance(document, fileOutputStream);

            // open方法似乎是表示允许写入内容
            document.open();

            // 添加文本内容 com.itextpdf.text.Paragraph表示文本段落实例
            document.add(new Paragraph(content));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 资源释放
            document.close();
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}