package com.zheteng.wsj.myzhihurb.net;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zheteng.wsj.myzhihurb.R;
import com.zheteng.wsj.myzhihurb.global.GlobalApplication;
import com.zheteng.wsj.myzhihurb.util.LogUtil;
import com.zheteng.wsj.myzhihurb.util.StreamUtils;
import com.zheteng.wsj.myzhihurb.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by wsj20 on 2016/9/6.
 */
public class ImageUtil {

    public static String savePath = Environment.getExternalStorageDirectory().getPath()
            + File.separator + GlobalApplication.context.getPackageName()
            + File.separator + "Image";


    private static ImageUtil imageUtil = new ImageUtil();

    private ImageLoader mImageLoader;//UIL类


    public static ImageUtil getInstance() {
        return imageUtil;
    }

    private ImageUtil() {


        mImageLoader = ImageLoader.getInstance();
    }


    /**
     * 显示图片 默认渐变模式包含加载失败，加载中显示图片
     */
    public void displayImage(String url, ImageView view) {
        mImageLoader.displayImage(url, view, ImageLoaderOptions.fadein_options);
    }

    /**
     * @param url
     * @param view
     * @param listener 图片加载的监听器
     */
    public void displayImageDefault(String url, ImageView view, ImageLoadingListener listener) {

        mImageLoader.displayImage(url, view, ImageLoaderOptions.noLoading_opitions, listener);

    }

    /**
     * @param url
     * @param view
     */
    public void displayImageDefault(String url, ImageView view) {
        mImageLoader.displayImage(url, view, ImageLoaderOptions.noLoading_opitions);

    }

    //    取出缓存图片路径 该方法拿到ImageLoader缓存的图片
    public String getAbsolutePath(String url) {
        return mImageLoader.getDiskCache().get(url).getAbsolutePath();
    }

    //    判断是ImageLoader否有缓存
    public boolean isExist(String url) {
        return mImageLoader.getDiskCache().get(url).exists();
    }


    /**
     * @param activity 需要保存图片的Activity
     * @param url      要保存的图片的Url地址
     */
    public void saveImage(Activity activity, String url) {
        checkSDPremision(activity);

        //SD卡文件存储位置
        // File saveFile = new File(Environment.getExternalStorageDirectory() + File.separator + "ZHApp", "Image");
        //LogUtil.e("保存图片的位置：" + saveFile.getAbsolutePath());

        String name = URLEncoder.encode(url);
        File imageFile = new File(savePath, name);

        if (copyImage(imageFile.getPath(), url)) {
            //保存图片成功
            ToastUtil.show(activity.getString(R.string.where_image_save) + savePath);
        } else {
            ToastUtil.show(activity.getString(R.string.Image_save_error));
        }

    }

    private void checkSDPremision(Activity activity) {
        // 6.0 检查权限 6.0以上的手机会申请运行时权限，
        if (Build.VERSION.SDK_INT >= 23) {
            int write = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);


            if (write != PackageManager.PERMISSION_GRANTED || read != PackageManager
                    .PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 300);
            }
        }
    }

    /**
     * @param targetPath 保存图片的位置
     * @param url        要保存的图片的Url
     * @return
     */
    private boolean copyImage(String targetPath, String url) {

        FileInputStream fis = null;
        FileOutputStream fos = null;
        int readNum = 0;
        byte[] readBuf = new byte[1024]; //文件读取缓冲区大小


        //根据Url拿到ImagerLoader的缓存图片的路径
        String cachePath = ImageUtil.getInstance().getAbsolutePath(url);
        File imageCacheFile = new File(cachePath);

        //如果有缓存文件 则将缓存文件保存在SD卡中
        if (imageCacheFile.exists()) {
            try {
                fis = new FileInputStream(imageCacheFile);
                fos = new FileOutputStream(targetPath);
                while ((readNum = fis.read(readBuf)) > 0) {
                    fos.write(readBuf, 0, readNum);
                }
                //保存完成
                LogUtil.e("//保存完成");
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtils.close(fis);
                StreamUtils.close(fos);
            }

        }

        //如果没有缓存则返回false
        return false;
    }

    /**
     * ImageUtls的内部接口，提供图片加载的Options配置
     */
    private interface ImageLoaderOptions {

        DisplayImageOptions fadein_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image)// 设置加载中显示什么图片
                .showImageForEmptyUri(R.drawable.default_image)// 设置如果url为空中显示什么图片
                .showImageOnFail(R.drawable.default_image)// 设置加载失败显示什么图片
                .cacheInMemory(true)// 内存缓存
                .cacheOnDisk(true)// 在硬盘缓存
                .imageScaleType(ImageScaleType.EXACTLY)//内部会对图片进一步的压缩
                .bitmapConfig(Bitmap.Config.RGB_565)//使用比较节省内存的颜色模式
                .displayer(new FadeInBitmapDisplayer(1000)).build();// 设置渐渐显示的动画效果
        // .displayer(new RoundedBitmapDisplayer(100)).build();// 设置圆角显示

        DisplayImageOptions noLoading_opitions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)// 内存缓存
                .cacheOnDisk(true)// 在硬盘缓存
                .imageScaleType(ImageScaleType.EXACTLY)//内部会对图片进一步的压缩
                .bitmapConfig(Bitmap.Config.RGB_565)//使用比较节省内存的颜色模式
                .displayer(new FadeInBitmapDisplayer(1500)).build();// 设置渐渐显示的动画效果
    }

}
