package com.zheteng.wsj.myzhihurb.net;

import android.os.Environment;

import com.zheteng.wsj.myzhihurb.global.GlobalApplication;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.util.StreamUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 网络请求缓存管理类
 * Created by wsj20 on 2016/9/9.
 */
public class CacheMananger {
    //缓存的存放地点
    public static String cacheDir = Environment.getExternalStorageDirectory().getPath()
            + File.separator + GlobalApplication.context.getPackageName()
            + File.separator + "Cache";


    private static int saveTime = 7 * 24 * 1000 * 60 * 60; //默认七天


    private static CacheMananger mCacheMananger = new CacheMananger();

    private CacheMananger() {
        File file = new File(cacheDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static CacheMananger getInstance() {
        return mCacheMananger;
    }

    /**
     * 保存网络数据 保存内容，请求的json字符串
     *
     * @param url  请求的的地址，用来作为保存文件的名称
     * @param data 要保存的数据内容
     */
    public void saveCache(String url, String data) {
        //1.构建缓存文件
        File file = buildCacheFile(url);
        FileWriter writer = null;
        //2.开始将缓存数据存入文件
        try {
            //第一次文件不存在所以需要创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //写入文件
            writer = new FileWriter(file);
            writer.write(data);
            writer.close();
            LogUtil.e("缓存成功");
        } catch (Exception e) {
            LogUtil.e(this, e.getMessage());
        } finally {
            StreamUtils.close(writer);
        }
    }


    /**
     * 取出相应url的json字符串
     *
     * @param url 缓存地址
     * @return data 没有缓存文件的情况下返回null ， 有缓存文件的但超过保存时长的情况下返回""
     */
    public String loadCahce(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        //缓存文件名称
        File file = buildCacheFile(url);

        if (!file.exists()) {
            return null;
        }
        if (isVaild(file)) {
            try {
                String line;
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtils.close(bufferedReader);
            }

        } else {
            //如果文件已经无效则删除文件
            file.delete();
        }
        return stringBuilder.toString();
    }

    /**
     * 设置数据缓存时长
     *
     * @param saveTime 数据缓存时长
     */
    public void setSaveTime(int saveTime) {
        CacheMananger.saveTime = saveTime;
    }

    /**
     * 初始化保存文件名称
     *
     * @param url 保存的内容的url
     * @return 保存文件的File名称
     */
    public File buildCacheFile(String url) {

        return new File(cacheDir, URLEncoder.encode(url));
    }

    /**
     * 判断缓存文件是否有效，可以动态设置某个文件的缓存保存时间？？？
     *
     * @param file 需要检查的缓存文件名称
     * @return 是否有效
     */
    public boolean isVaild(File file) {
        //1.获取文件上次修改时间
        long lastModified = file.lastModified();
        //2.计算文件存在多长时间
        long fileExistDuration = System.currentTimeMillis() - lastModified;
        return fileExistDuration < saveTime;
    }
}
