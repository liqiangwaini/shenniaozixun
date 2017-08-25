package com.xingbo.live.controller;

import com.xingbo.live.entity.msg.MsgFUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/9/2.
 */
public class AdminControl {
    private List<MsgFUser> data;
    public List<MsgFUser> adminList = new ArrayList<MsgFUser>();

    public AdminControl() {
    }

    public AdminControl(List<MsgFUser> data, String uid) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getAdmin().equals("1") && data.get(i).getLogintype().equals("login") && !data.get(i).getId().equals(uid)) {  //管理员权限
                adminList.add(data.get(i));
            }
        }
    }

    public void addAdmin(MsgFUser msgFUser, String uid) {
        boolean isContains = false;
        for (int i = 0; i < adminList.size(); i++) {
            if (adminList.get(i).getId().equals(msgFUser.getId())) {
                isContains = true;
                break;
            }
        }
        if (!isContains && !msgFUser.getId().equals(uid)&&msgFUser.getLogintype().equals("login")) {
            adminList.add(msgFUser);
        }
    }

    public void addAdminById(String adminId, String uid) {
        MsgFUser msgFUser = null;
        boolean isContains = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(adminId)&&data.get(i).getLogintype().equals("login")) {
                msgFUser = data.get(i);
                break;
            }
        }
        for (int i = 0; i < adminList.size(); i++) {
            if (adminList.get(i).getId().equals(msgFUser.getId())) {
                isContains = true;
                break;
            }
        }
        if (!isContains&&msgFUser != null && adminId != uid) {
            adminList.add(msgFUser);
        }
    }

    public void deleteAdminById(String adminId, String uid) {
        MsgFUser msgFUser = null;
        for (int i = 0; i < adminList.size(); i++) {
            if (adminList.get(i).getId().equals(adminId)&&data.get(i).getLogintype().equals("login")) {
                msgFUser = adminList.get(i);
                break;
            }
        }
        if (msgFUser != null) {
            adminList.remove(msgFUser);
        }
    }

    public void removeAdmin(MsgFUser msgFUser) {
        boolean isContains = false;
        for (int i = 0; i < adminList.size(); i++) {
            if (adminList.get(i).getId().equals(msgFUser.getId())&&msgFUser.getLogintype().equals("login")) {
                isContains = true;
                break;
            }
        }
        if (isContains) {
            adminList.remove(msgFUser);
        }
    }
}
