package com.yeecloud.adplus.gateway.controller.form;


public enum ScannerType {

    NORMAL("advanced_general", 0),
    ANIMAL("animal", 1),
    PLANT("plant", 2),
    FOOD("ingredient", 3),
    CURRENCY("currency", 4),
    LANDMARK("landmark", 5);

    private String type;

    private int index;

    ScannerType(String type, int index) {
        this.type = type;
        this.index = index;
    }

    public static String getType(int index) {
        for (ScannerType type : ScannerType.values()) {
            if (type.getIndex() == index) {
                return type.getType();
            }
        }
        return "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
