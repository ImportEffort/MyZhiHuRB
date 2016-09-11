package com.zheteng.wsj.myzhihurb.bean;

import java.util.List;

/**
 * Created by wsj20 on 2016/9/10.
 */
public class BeforeNewsBean {


    /**
     * type : 0
     * id : 4620623
     * title : 联觉与通感
     */

    private List<BaseBean> stories;

    public List<BaseBean> getStories() {
        return stories;
    }

    public void setStories(List<BaseBean> stories) {
        this.stories = stories;
    }

    /*public static class BaseBean {
        private int type;
        private int id;
        private String title;

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
    }*/
}
