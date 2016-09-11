package com.zheteng.wsj.myzhihurb.bean;

import java.util.List;

/**
 * Created by wsj20 on 2016/9/8.
 */
public class SlidingMenuBean {

    private List<OthersBean> others;


    public List<OthersBean> getOthers() {
        return others;
    }


    public static class OthersBean {
        private int color;
        private int id;
        private String name;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
