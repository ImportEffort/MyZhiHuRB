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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public void setImages(List<String> images) {
        this.images = images;
    }
}
