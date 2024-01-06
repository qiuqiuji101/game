package cn.tf.code.utils;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.List;

@Component("tfPageProcessor")
public class TfPageProcessor implements PageProcessor {
    private final Site site = Site.me().setDomain("medium.com");

    @Override
    public void process(Page page) {
        List<String> links = page.getHtml().links().regex("https://medium\\.com/@\\w+/.+").all();
        page.addTargetRequests(links);
        page.putField("title", page.getHtml().xpath("//title").toString());
        page.putField("htmlStr", page.getHtml().xpath("//html").toString());
        page.putField("content", page.getHtml().xpath("//div[@class='meteredContent]").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public  void spider(String proxyHost,int proxyPort,String pdfBasePath){
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(proxyHost,proxyPort)));
        Spider.create(new TfPageProcessor()).addUrl("https://medium.com/?tag=software-engineering")
                .setDownloader(httpClientDownloader)
                .addPipeline(new TfResPipeline(pdfBasePath)).run();
    }
}
