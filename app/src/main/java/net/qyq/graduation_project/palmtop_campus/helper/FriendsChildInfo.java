package net.qyq.graduation_project.palmtop_campus.helper;

/**
 * Author：钱
 * Date: 2019/4/18 0018 13:33
 * Des:
 */
public class FriendsChildInfo {
    private String remark;//名字
    private String userID;//老师或学生的工号或学号
    private String friendsPortrait;//好友头像

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFriendsPortrait() {
        return friendsPortrait;
    }

    public void setFriendsPortrait(String friendsPortrait) {
        this.friendsPortrait = friendsPortrait;
    }
}
