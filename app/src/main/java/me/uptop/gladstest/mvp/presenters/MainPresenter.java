package me.uptop.gladstest.mvp.presenters;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmResults;
import me.uptop.gladstest.data.storage.realm.CategoriesRealm;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.scopes.MainScope;
import me.uptop.gladstest.mvp.models.MainModel;
import me.uptop.gladstest.mvp.views.IMainView;
import me.uptop.gladstest.ui.adapters.PostsAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter extends AbstractPresenter<IMainView> {

    @Inject
    MainModel mModel;

    protected CompositeSubscription mCompositeSubscription;
    Subscription allPosts;
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
        mCompositeSubscription.add(subscribeOnPostsRealmObs("tech"));
        mCompositeSubscription.add(mModel.allCategoriesSubscribe());
//        mCompositeSubscription.add(getCategories());
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

        super.dropView();
    }

//    private Subscription getCategories() {
//        return mModel.allCategories().subscribe();
//    }

    private Subscription subscribeOnPostsRealmObs(String category) {
        if(getView() != null) {
            getView().showLoad();
            Observable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                    getView().hideLoad();
                }

                @Override
                public void onError(Throwable e) {
                    getView().showError(e);
                }

                @Override
                public void onNext(Long aLong) {

                }
            });
        }

        allPosts = mModel.allQuotesObs(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RealmSubscriber());

        return allPosts;
    }

    public void changeCategory(CategoriesRealm item) {
        mCompositeSubscription.remove(allPosts);
        getView().getAdapter().clearData();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<PostsRealm> result = realm1.where(PostsRealm.class).findAll();
            result.deleteAllFromRealm();
        });
        mCompositeSubscription.add(subscribeOnPostsRealmObs(item.getSlug()));
    }


    private class RealmSubscriber extends Subscriber<PostsRealm> {
        PostsAdapter mAdapter = getView().getAdapter();

        @Override
        public void onCompleted() {
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
            getView().hideLoad();
            getView().initAdapter();
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
