package com.roman.gurdan.sudo.pro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.roman.gurdan.sudo.pro.R;

public class GameMenu extends ConstraintLayout {
    public GameMenu(@NonNull Context context) {
        super(context);
    }

    public GameMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private IGameMenuListener listener = null;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewById(R.id.menu1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onMenuItem(0);
            }
        });
        findViewById(R.id.menu2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onMenuItem(1);
            }
        });
        findViewById(R.id.menu3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onMenuItem(2);
            }
        });
        findViewById(R.id.menu4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onMenuItem(3);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.listener = null;
    }

    public void setListener(IGameMenuListener<Integer> listener) {
        this.listener = listener;
    }

    public void updateNoteStatus(boolean isNoteOn){
        findViewById(R.id.menu3).setBackgroundColor(isNoteOn ? getContext().getResources().getColor(R.color.teal_200) : getContext().getResources().getColor(R.color.secondColor));
    }


}
