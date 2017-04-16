package me.uptop.gladstest.mvp.presenters;

import android.support.annotation.Nullable;

import me.uptop.gladstest.mvp.views.IView;

public abstract class AbstractPresenter<T extends IView> {
    public static String TAG = "AbstractPresenter";

    private T mView;

    public void takeView(T view) {
        mView = view;
    }

    public void dropView() {
        mView = null;
    }

    public abstract void initView();

    @Nullable
    public T getView() {
        return mView;
    }
}
