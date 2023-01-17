package com.roman.gurdan.sudo.pro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.activity.GameActivity;
import com.roman.gurdan.sudo.pro.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.beginner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(GameSize.SIZE_FOUR, Difficulty.EASY);
            }
        });
        view.findViewById(R.id.easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(GameSize.SIZE_FOUR, Difficulty.RANDOM);
            }
        });
        view.findViewById(R.id.medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(GameSize.SIZE_SIX, Difficulty.RANDOM);
            }
        });
        view.findViewById(R.id.hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(GameSize.SIZE_EIGHT, Difficulty.RANDOM);
            }
        });
        view.findViewById(R.id.expert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(GameSize.SIZE_NINE, Difficulty.RANDOM);
            }
        });
    }

    private void startGame(GameSize size, Difficulty difficulty){
        Intent intent = new Intent(getContext(), GameActivity.class);
        intent.putExtra("gameSize", size.getValue());
        intent.putExtra("gameDiff", difficulty._difficulty);
        getContext().startActivity(intent);
    }




}
