package com.zheteng.wsj.myzhihurb.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by wsj20 on 2016/9/6.
 */
public class StreamUtils {

    public static void close(Closeable stream) {
        if(stream!=null){
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
