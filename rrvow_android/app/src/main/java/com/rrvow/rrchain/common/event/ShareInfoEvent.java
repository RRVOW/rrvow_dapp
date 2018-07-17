package com.rrvow.rrchain.common.event;

/**
 * Created by david on 2018/3/6.
 */

public class ShareInfoEvent extends BaseEvent {

    private String title;
    private String description;
    private String iconURL;
    private String detailURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getDetailURL() {
        return detailURL;
    }

    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }
}
