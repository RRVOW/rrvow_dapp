package com.rrvow.rrchain.common.event;

/**
 * @author by lpc on 2018/2/7.
 */
public class ModifyUserInfoEvent extends BaseEvent {

    private String nickName;
    private String phone;

    private boolean isFromProtocol;

    public ModifyUserInfoEvent() {
    }

    public ModifyUserInfoEvent(boolean isFromProtocol) {
        this.isFromProtocol = isFromProtocol;
    }

    public ModifyUserInfoEvent(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isFromProtocol() {
        return isFromProtocol;
    }

    public void setFromProtocol(boolean isFromProtocol) {
        isFromProtocol = isFromProtocol;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
