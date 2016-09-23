package com.zheteng.wsj.myzhihurb.global;

/**
 * Created by wsj20 on 2016/9/6.
 */
public interface Constants {
    /**
     * 网络请求的前缀
     */
    String BaseUrl = "http://news-at.zhihu.com/api/4/";
    /**
     * Splash界面的url地址 http://news-at.zhihu.com/api/4/start-image/1080*1776
     */
    String SplashUrl = BaseUrl + "start-image/1080*1776";
    /**
     * http://news-at.zhihu.com/api/4/themes
     *
     * 侧边栏数据 能拿到新闻也的url地址 通过其中id
     */
    String ThemeUrl = BaseUrl + "themes";

    /**
     * 首页数据
     */

    String LastUrl = BaseUrl + "news/latest";//首页数据
    /**
     *  示例： http://news-at.zhihu.com/api/4/theme/13
     * 除首页外数据，其他栏目数据的Url地址前缀，应与对应的栏目的id组合请求网络数据
     */
    String NewsPrefixUrl = BaseUrl + "theme/";

    /**
     * 各个页面展示数据条目的详细信息数据的url地址，应拼接对应条目的id,该id为对应条目展示数据内容BaseBean中Id
     */
    String NewsDetailUrl = BaseUrl + "news/";

    /**
     * http://news.at.zhihu.com/api/4/news/before/20160908 后面跟上最新数据中的date
     * 首页数据旧数据
     */
    String BeforeUrl = BaseUrl + "news/before/";
    /**
     * 其他栏目旧数据
     * 第一%s应为栏目的id
     * 第二个%s为该当前这一页数据最后一个条目的id，而不是像首页旧数据一样的日期后缀
     *
     * 如 http://news-at.zhihu.com/api/4/theme/13/before/4738657
     */
    String NewsBeforeUrl = BaseUrl + "theme/%s/before/%s";

    /**
     * 新闻详情界面的Id 用来和NewsDetailUrl拼接为详情页面数据内容的url
     * http://news-at.zhihu.com/api/4/news/7399166
     */
    String NEWS_ID = "news_id";//新闻详细信息的Id
    /**
     * 详情界面的额外信息，评论点赞数量  http://news-at.zhihu.com/api/4/story-extra/8778250
     */
    String NewExtraUrl = BaseUrl + "story-extra/";//%s使用对应的新闻id



    /**
     * 用来保存RecycleView已被点击条目的 sp 的key值
     */
    String SP_NAME = "save_clicked_item";

}
