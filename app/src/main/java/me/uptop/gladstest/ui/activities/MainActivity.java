package me.uptop.gladstest.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmResults;
import me.uptop.gladstest.BuildConfig;
import me.uptop.gladstest.R;
import me.uptop.gladstest.data.storage.realm.CategoriesRealm;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.scopes.MainScope;
import me.uptop.gladstest.mvp.presenters.MainPresenter;
import me.uptop.gladstest.mvp.views.IMainView;
import me.uptop.gladstest.ui.adapters.CategoriesSpinnerAdapter;
import me.uptop.gladstest.ui.adapters.PostsAdapter;

import static me.uptop.gladstest.App.getContext;

public class MainActivity extends AppCompatActivity implements IMainView {
    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.main_spinner)
    AppCompatSpinner spinner;

    @Inject
    MainPresenter mMainPresenter;

    PostsAdapter mPostsAdapter;
    CategoriesSpinnerAdapter spinnerAdapter;
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

        mPostsAdapter = new PostsAdapter(getContext(), listener);

        mMainPresenter.takeView(this);
        mMainPresenter.initView();

        setupActionBar();
        setTitle("");
        initAdapterSpinner();
    }



    @Override
    protected void onStop() {
        mMainPresenter.dropView();
        if(isFinishing()) {
            DaggerService.unregisterScope(MainScope.class);
        }
        super.onStop();
    }

    PostsAdapter.ViewHolder.OnClickListener listener = product -> onCardClick(product);

    private void onCardClick(PostsRealm product) {
        startDetailsActivtiy(product);
    }

    private void startDetailsActivtiy(PostsRealm product) {
        final Intent intent;
        intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("redirectUrl", product.getRedirectUrl());
//        intent.putExtra("date", product.get());
        intent.putExtra("title", product.getName());
        intent.putExtra("desc", product.getDesc());
        intent.putExtra("screenshot", product.getScreenshotUrl());
        intent.putExtra("votesCount", product.getVotesCount());
        startActivity(intent);
        finish();
    }

    private void setupActionBar() {
        setSupportActionBar(toolBar);
    }


    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mPostsAdapter);
    }

    private void initAdapterSpinner() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CategoriesRealm> categories = realm.where(CategoriesRealm.class).findAll();
        realm.close();
        spinnerAdapter = new CategoriesSpinnerAdapter(this, categories);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CategoriesRealm item = (CategoriesRealm) parent.getSelectedItem();
                mMainPresenter.changeCategory(item);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
