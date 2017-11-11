package org.honeypretty.honeyprettykidslove;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 50137436 on 2017-11-10.
 */

/**
 * Created by user on 2016-08-10.
 */
public class PraiseStickerItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView imageView;

    public PraiseStickerItemView(Context context) {
        super(context);

        init(context);
    }

    public PraiseStickerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.praise_sticker_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void setName(String name) {
        textView.setText(name);
    }

    public void setMobile(String mobile) {
        textView2.setText(mobile);
    }

    public void setAge(int age) {
        textView3.setText(String.valueOf(age));
    }

    public void setImage(int resId) {
        imageView.setImageResource(resId);
    }
}

