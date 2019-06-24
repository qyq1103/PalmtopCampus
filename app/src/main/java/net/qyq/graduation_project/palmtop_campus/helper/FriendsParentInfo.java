package net.qyq.graduation_project.palmtop_campus.helper;

import java.util.List;

/**
 * Author：钱
 * Date: 2019/4/18 0018 13:32
 * Des:
 */
public class FriendsParentInfo {
    private String title;
    private List<FriendsChildInfo> info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FriendsChildInfo> getInfo() {
        return info;
    }

    public void setInfo(List<FriendsChildInfo> info) {
        this.info = info;
    }
}
