package me.uptop.gladstest.mvp.presenters;

import android.util.Log;

import javax.inject.Inject;

import dagger.Provides;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.scopes.MainScope;
import me.uptop.gladstest.mvp.models.MainModel;
import me.uptop.gladstest.mvp.views.IMainView;
import me.uptop.gladstest.ui.adapters.PostsAdapter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter extends AbstractPresenter<IMainView> {

    @Inject
    MainModel mModel;

    protected CompositeSubscription mCompositeSubscription;
//    private RealmChangeListener mListener;

    public MainPresenter() {
        Component component = DaggerService.getComponent(Component.class);
        if(component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(Component.class, component);
        }
        component.inject(this);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initView() {
        mCompositeSubscription.add(subscribeOnQuotesRealmObs());
//        if(getView() != null && getView().getQuote() != null) {
//
//            mListener = element -> {
//                if(getView() != null) {
//                    getView().showQuotesView();
//                }};
//
//            getView().getQuote().addChangeListener(mListener);
//        }
    }

    @Override
    public void dropView() {
        if(mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
//        getView().getQuote().removeChangeListener(mListener);
        super.dropView();
    }

    private Subscription subscribeOnQuotesRealmObs() {
        if(getView() != null) {
            getView().showLoad();
        }

        Log.e("AllQuotesPresenter", "initView: ");
        Subscription allQuotes = mModel.allQuotesObs()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RealmSubscriber());

        return allQuotes;
    }


    private class RealmSubscriber extends Subscriber<PostsRealm> {
        PostsAdapter mAdapter = getView().getAdapter();

        @Override
        public void onCompleted() {
            getView().hideLoad();
            getView().initAdapter();
        }

        @Override
        public void onError(Throwable e) {
            getView().hideLoad();
            if(getView() != null) {
                getView().showError(e);
            }
        }

        @Override
        public void onNext(PostsRealm postsRealm) {
            mAdapter.addItem(postsRealm);
//            getView().i();
        }
    }


    //region --------------------DI--------------------------------

    @dagger.Module
    public class Module {
        @Provides
        @MainScope
        MainModel provideMainModel() {
            return new MainModel();
        }
    }


    @dagger.Component(modules = Module.class)
    @MainScope
    interface Component {
        void inject(MainPresenter mainPresenter);
    }

    private Component createDaggerComponent() {
        return DaggerMainPresenter_Component.builder()
                .module(new Module())
                .build();
    }

    //endregion
}
