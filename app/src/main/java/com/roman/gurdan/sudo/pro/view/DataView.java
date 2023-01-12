package com.roman.gurdan.sudo.pro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.roman.gurdan.sudo.pro.R;

public class DataView extends CardView {

    private TextView title;
    private TextView msg;

    private int titleRes;
    private int msgRes;

    public DataView(@NonNull Context context) {
        this(context, null);
    }

    public DataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DataView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DataView);
        titleRes = array.getResourceId(R.styleable.DataView_vTitle, R.string.nan);
        msgRes = array.getColor(R.styleable.DataView_vMsgColor, context.getResources().getColor(R.color.teal_200));
        array.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            View.inflate(getContext(),R.layout.view_data_item, this);
            title = findViewById(R.id.title);
            msg = findViewById(R.id.msg);
            title.setText(titleRes);
            msg.setTextColor(msgRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMsg(String data) {
        if (this.msg != null && data != null)
            this.msg.setText(data);
    }


}
