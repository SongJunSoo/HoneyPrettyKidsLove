package org.honeypretty.honeyprettykidslove;

/**
 * Created by 50137436 on 2017-11-10.
 */

public class PraiseStickerItem {

    String name;
    String mobile;
    int age;
    int resId;

    public PraiseStickerItem(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

