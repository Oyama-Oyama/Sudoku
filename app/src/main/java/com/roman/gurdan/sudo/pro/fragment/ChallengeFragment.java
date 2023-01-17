package com.roman.gurdan.sudo.pro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.roman.gurdan.sudo.pro.App;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.activity.SettingActivity;
import com.roman.gurdan.sudo.pro.base.BaseFragment;
import com.roman.gurdan.sudo.pro.data.db.GameData;
import com.roman.gurdan.sudo.pro.util.DateUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChallengeFragment extends BaseFragment {

    private TextView subtitle;
    private TextView[] weekDates = new TextView[7];
    private String[] dates = new String[7];
    private CompositeDisposable disposable = new CompositeDisposable();

    private MaterialButton start;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_challenge;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subtitle = view.findViewById(R.id.subtitle);
        weekDates[0] = view.findViewById(R.id.sunDate);
        weekDates[1] = view.findViewById(R.id.monDate);
        weekDates[2] = view.findViewById(R.id.tuesDate);
        weekDates[3] = view.findViewById(R.id.wedDate);
        weekDates[4] = view.findViewById(R.id.thurDate);
        weekDates[5] = view.findViewById(R.id.friDate);
        weekDates[6] = view.findViewById(R.id.satDate);
        start = view.findViewById(R.id.start);
        setupStatus();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingActivity.class));
                Boolean tag = false;
                try {
                    tag = (Boolean) view.getTag();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tag) {

                } else {

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        super.onDestroyView();
    }

    private void setupStatus() {
        String currentDate = DateUtil.getDate();
        String[][] date = DateUtil.getWeek();
        for (int i = 0; i < date.length; i++) {
            String[] item = date[i];
            dates[i] = item[0];
            weekDates[i].setText(item[1]);
            weekDates[i].setTag(i);
            weekDates[i].setSelected(item[0].equals(currentDate));
            weekDates[i].setOnClickListener(clickListener);
        }
        disposable.add(Observable.just(dates)
                .map(new Function<String[], Integer>() {
                    @Override
                    public Integer apply(String[] strings) throws Throwable {
                        int count = 0;
                        for (String s : strings) {
                            int _count = GameData.of(App.instance)
                                    .weekDao()
                                    .getWeekly(s);
                            if (_count > 0) count++;
                        }
                        return count;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer count) throws Throwable {
                        if (subtitle != null)
                            subtitle.setText(String.format(getString(R.string.leftChange), count));
                    }
                }));
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int tag = (int) view.getTag();
            try {
                String date = dates[tag];
                disposable.add(Observable.just(date)
                        .map(new Function<String, Boolean>() {
                            @Override
                            public Boolean apply(String s) throws Throwable {
                                int count = GameData.of(App.instance)
                                        .weekDao()
                                        .getWeekly(s);
                                return count > 0;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Throwable {
                                if (weekDates != null) {
                                    for (TextView textView : weekDates) {
                                        if (textView != null) textView.setSelected(false);
                                    }
                                }
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Throwable {
                                if (view != null) view.setSelected(true);
                            }
                        })
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Throwable {
                                if (start != null) {
                                    start.setText(aBoolean ? R.string.restartChange : R.string.startChallenge);
                                    start.setTag(aBoolean);
                                }
                            }
                        }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
