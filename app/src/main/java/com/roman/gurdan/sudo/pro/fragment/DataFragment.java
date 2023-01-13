package com.roman.gurdan.sudo.pro.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roman.gurdan.sudo.pro.App;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseFragment;
import com.roman.gurdan.sudo.pro.data.db.GameData;
import com.roman.gurdan.sudo.pro.view.DataView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataFragment extends BaseFragment {

    private DataView total;
    private DataView totalWin;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        total = view.findViewById(R.id.total);
        totalWin = view.findViewById(R.id.totalWin);
        setup();
    }

    @Override
    public void onDestroyView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
        super.onDestroyView();
    }

    private void setup() {
        disposables.add(Observable.just(1)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        return GameData.of(App.instance)
                                .gameDao().getCount();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        if (total != null) total.setMsg(String.valueOf(integer));
                    }
                }));
        disposables.add(Observable.just(1)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        return GameData.of(App.instance)
                                .gameDao().getGamesResultCount(1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        if (totalWin != null) totalWin.setMsg(String.valueOf(integer));
                    }
                }));
    }

}
