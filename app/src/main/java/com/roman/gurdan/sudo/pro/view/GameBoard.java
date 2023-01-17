package com.roman.gurdan.sudo.pro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.roman.gurdan.sudo.pro.R;

public class GameBoard extends ConstraintLayout {

    public GameBoard(@NonNull Context context) {
        super(context);
    }

    public GameBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameBoard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TextView[] textViews = new TextView[9];
    private IGameMenuListener listener = null;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        textViews[0] = findViewById(R.id.num1);
        textViews[0].setOnClickListener(clickListener);
        textViews[1] = findViewById(R.id.num2);
        textViews[1].setOnClickListener(clickListener);
        textViews[2] = findViewById(R.id.num3);
        textViews[2].setOnClickListener(clickListener);
        textViews[3] = findViewById(R.id.num4);
        textViews[3].setOnClickListener(clickListener);
        textViews[4] = findViewById(R.id.num5);
        textViews[4].setOnClickListener(clickListener);
        textViews[5] = findViewById(R.id.num6);
        textViews[5].setOnClickListener(clickListener);
        textViews[6] = findViewById(R.id.num7);
        textViews[6].setOnClickListener(clickListener);
        textViews[7] = findViewById(R.id.num8);
        textViews[7].setOnClickListener(clickListener);
        textViews[8] = findViewById(R.id.num9);
        textViews[8].setOnClickListener(clickListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.listener = null;
    }

    public void setListener(IGameMenuListener<Integer> listener) {
        this.listener = listener;
    }

    public void setGameSize(int size) {
        this.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < textViews.length; i++) {
                    textViews[i].setVisibility(i >= size ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

    private View.OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                int num = Integer.parseInt(((TextView) view).getText().toString());
                if (listener != null) listener.onMenuItem(num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
