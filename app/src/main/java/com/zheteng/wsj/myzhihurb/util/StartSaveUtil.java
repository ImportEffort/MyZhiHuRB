package com.zheteng.wsj.myzhihurb.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.zheteng.wsj.myzhihurb.global.GlobalApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wsj20 on 2016/9/7.
 */
public class StartSaveUtil {

    private static String savePath = Environment.getExternalStorageDirectory().getPath()
            + File.separator + GlobalApplication.context.getPackageName()
            + File.separator + "Start";

    private static StartSaveUtil instance = new StartSaveUtil();

    private StartSaveUtil() {

        LogUtil.e(savePath);
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
    }

    public static StartSaveUtil getInstance() {
        return instance;
    }

    public void saveStartData(String des, byte[] imageBytes, Activity activity) {
        checkSDPremision(activity);

        File imageFile = new File(savePath, "start.jpg");
        //保存图片
        copyImage(imageBytes, imageFile);

        File desFile = new File(savePath, "startDes.txt");
        //保存图片描述
        copyDes(des, desFile);

    }

    /**
     * 保存图片描述信息的方法
     *
     * @param des     描述信息内容
     * @param desFile 描述信息的存储位置
     */
    private void copyDes(String des, File desFile) {
        if (des != null && des.length() > 0) {
         /*   if (desFile.exists()) {
                desFile.delete();
            }*/
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(desFile));
                bw.write(des, 0, des.length() - 1);
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtils.close(bw);
            }
        }
    }

    /**
     * 保存图片
     *
     * @param imageBytes
     * @param imageFile
     */
    private void copyImage(byte[] imageBytes, File imageFile) {
        //将图片存储在SD卡
        FileOutputStream fos = null;

        if (imageBytes != null && imageBytes.length > 0) {
         /*   if (imageFile.exists()) {
                imageFile.delete();
            }*/
            try {
                fos = new FileOutputStream(imageFile);
                fos.write(imageBytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtils.close(fos);
            }
        }
    }


    /**
     *获取图片描述
     * @return string des
     */
    public String getStartDes() {
        String result = null;
        BufferedReader br = null;
        File desFile = new File(savePath, "startDes.txt");
        if (desFile.exists() && desFile.length() > 0) {

            try {
                br = new BufferedReader(new FileReader(desFile));
                result = br.readLine();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                StreamUtils.close(br);
            }


        }

        return result;
    }

    /**
     * 返回开始图片存储位置
     * @return imageFile
     */
    public String getImageDispalyFile() {
        File imageFile = new File(savePath, "start.jpg");
        String dispalyUrl = null;

        if (imageFile.exists() && imageFile.length() > 0){
            dispalyUrl =  "file://" + imageFile.getAbsolutePath();
        }

        return dispalyUrl;
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

}
