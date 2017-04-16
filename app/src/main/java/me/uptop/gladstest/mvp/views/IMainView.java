package me.uptop.gladstest.mvp.views;

import me.uptop.gladstest.ui.adapters.PostsAdapter;

public interface IMainView extends IView {
    PostsAdapter getAdapter();

    void initAdapter();

    void hideLoad();

    void showError(Throwable e);

    void showLoad();

    void showMessage(String message);
}
