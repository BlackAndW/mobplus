package com.yeecloud.adplus.gateway.controller.form;

public enum JpushMsg {

    HOT("The phone is hot!", "The problem has been found, click to solve >>", 0),
    BATTERY("Phone is draining battery!", "The problem has been found, click to solve >>", 1),
    JUNK("Too many junk files!", "Click to process now >>", 2),
    FASTER("Want to make your iPhone faster?", "Come and clear some storage space!", 3),
    SPACE("Increase phone space", "Fix iPhone storage shortage now", 4),
    SCREENSHOTS("Screenshots are free today", "Delete similar screenshots in seconds", 5),
    DIMAGE("Duplicate image removal", "Delete the same pictures to free up space!", 6),
    DVIDEO("Duplicate video removal", "Delete the same video to free up space!", 7),
    FAST("Is your phone draining battery fast?", "Immediately improve battery life", 8),
    CPU("high temperature cpu", "check now", 9),
    HINT("important hint", "It's been a long time since you cleaned your iPhone", 10),
    ACE("network acceleration", "Please do smart detection", 11);

    private String title;

    private String content;

    private int index;

    JpushMsg(String title, String content, int index) {
        this.title = title;
        this.content = content;
        this.index = index;
    }

    public static String getTitle(int index) {
        for (JpushMsg msg : JpushMsg.values()) {
            if (msg.getIndex() == index) {
                return msg.getTitle();
            }
        }
        return "";
    }

    public static String getContent(int index) {
        for (JpushMsg msg : JpushMsg.values()) {
            if (msg.getIndex() == index) {
                return msg.getContent();
            }
        }
        return "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
