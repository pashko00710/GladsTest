package me.uptop.gladstest.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;
import me.uptop.gladstest.BuildConfig;
import me.uptop.gladstest.R;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.scopes.MainScope;
import me.uptop.gladstest.mvp.presenters.MainPresenter;
import me.uptop.gladstest.mvp.views.IMainView;
import me.uptop.gladstest.ui.adapters.PostsAdapter;

import static me.uptop.gladstest.App.getContext;

public class MainActivity extends AppCompatActivity implements IMainView {
    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Inject
    MainPresenter mMainPresenter;

    PostsAdapter mPostsAdapter;
    protected static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Component component = DaggerService.getComponent(Component.class);
        if(component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);

        mPostsAdapter = new PostsAdapter();

        mMainPresenter.takeView(this);
        mMainPresenter.initView();

        setupActionBar();
        setTitle("Tech");
//        initAdapter();
    }

    @Override
    protected void onStop() {
        mMainPresenter.dropView();
        if(isFinishing()) {
            DaggerService.unregisterScope(MainScope.class);
        }
        super.onStop();
    }

    private void setupActionBar() {
        setSupportActionBar(toolBar);
    }


    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mPostsAdapter);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideLoad() {
        if (mProgressDialog!=null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }
    }

    @Override
    public void showError(Throwable e) {
        if (BuildConfig.DEBUG) {
            showMessage(e.getMessage());
            e.printStackTrace();
        } else {
            showMessage(getString(R.string.unknown_error));
            //todo:send error stacktrace to crashlytics
        }
    }

    @Override
    public void showLoad() {
        if (mProgressDialog == null) {
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mProgressDialog.setContentView(R.layout.progress_root);
        } else {
            mProgressDialog.show();
            mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mProgressDialog.setContentView(R.layout.progress_root);
        }
    }

    @Override
    public PostsAdapter getAdapter() {
        return mPostsAdapter;
    }


    //region --------------------DI--------------------------------

    @dagger.Module
    public class Module {
        @Provides
        @MainScope
        MainPresenter provideProductPresenter() {
            return new MainPresenter();
        }
    }


    @dagger.Component(modules = Module.class)
    @MainScope
    interface Component {
        void inject(MainActivity mainActivity);
    }

    private Component createDaggerComponent() {
        return DaggerMainActivity_Component.builder()
                .module(new Module())
                .build();
    }

    //endregion
}
