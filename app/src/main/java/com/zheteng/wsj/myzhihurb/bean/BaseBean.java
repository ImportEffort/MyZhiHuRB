package com.zheteng.wsj.myzhihurb.bean;

import java.util.List;

/**
 * Created by wsj20 on 2016/9/10.
 */
public class BaseBean {

    private String title;
    private List<String> images;
    private int type;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

}
