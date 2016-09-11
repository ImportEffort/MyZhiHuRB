package com.zheteng.wsj.myzhihurb.net;

/**
 * Created by wsj20 on 2016/9/6.
 */
public interface UrlConstants {

    String BaseUrl = "http://news-at.zhihu.com";
    String SplashUrl = BaseUrl + "/api/4/start-image/1080*1776";
//    http://news-at.zhihu.com/api/4/themes
    String ThemeUrl = BaseUrl + "/api/4/themes";//侧边栏数据 能拿到新闻也的url地址 通过其中id
    String LastUrl = BaseUrl + "/api/4/news/latest";//首页数据
//    http://news-at.zhihu.com/api/4/theme/
    String NewsUrl = BaseUrl + "/api/4/theme/";


    String NewsDetailUrl = BaseUrl + "/api/4/news/";

    //http://news.at.zhihu.com/api/4/news/before/
    String BeforeUrl = BaseUrl + "/api/4/news/before/";//首页数据旧数据

    //   现在我需要拿到新闻页的老数据 拿到新闻页老数据的Id（不会变） 那么如何获取新闻页的老数据
    String NewsBeforeUrl = BaseUrl + "/api/4/theme/%s/before/%s";
    String NEWS_ID = "news_id";//新闻详细信息的Id
    //http://news-at.zhihu.com/api/4/story-extra/8778250
    String NewExtraUrl = BaseUrl +"/api/4/story-extra/";//%s使用对应的新闻id
    String NEWS_DETAIL = "news_detail";//首页viewpager请求完成的详情页json字符串
}
