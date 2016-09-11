package com.zheteng.wsj.myzhihurb.event;

/**
 * Created by wsj20 on 2016/9/12.
 */
public class HomeDataEvent {

    private String mMsg;

    public HomeDataEvent(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }

    public String getMsg(){
        return mMsg;
    }
}
