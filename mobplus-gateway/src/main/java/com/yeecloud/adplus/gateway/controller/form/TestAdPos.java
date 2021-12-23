package com.yeecloud.adplus.gateway.controller.form;

public enum TestAdPos {

    /** 谷歌测试广告id */
    GOOGLE_SPLASH("google",1, "ca-app-pub-3940256099942544/3419835294"),
    GOOGLE_INTERSTITIAL("google",3, "ca-app-pub-3940256099942544/1033173712"),
    GOOGLE_NATIVE("google",5, "ca-app-pub-3940256099942544/2247696110"),
    GOOGLE_REWARD("google",6, "ca-app-pub-3940256099942544/5224354917");

    private String adv;
    private int adType;
    private String code;

    TestAdPos(String adv, int adType, String code) {
        this.adv = adv;
        this.adType = adType;
        this.code = code;
    }

    public static String getTestPosId(String adv, int adType) {
        for (TestAdPos adPos: TestAdPos.values()) {
            if (adPos.getAdv().equals(adv) && adPos.getAdType() == adType) {
                return adPos.getCode();
            }
        }
        return "";
    }

    public String getAdv() {
        return adv;
    }

    public void setAdv(String adv) {
        this.adv = adv;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
