package me.uptop.gladstest.data.managers;

import android.content.Context;

import javax.inject.Inject;

import me.uptop.gladstest.App;
import me.uptop.gladstest.data.network.RestCallTransformer;
import me.uptop.gladstest.data.network.RestService;
import me.uptop.gladstest.data.storage.realm.CategoriesRealm;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.components.DaggerDataManagerComponent;
import me.uptop.gladstest.di.components.DataManagerComponent;
import me.uptop.gladstest.di.modules.LocalModule;
import me.uptop.gladstest.di.modules.NetworkModule;
import me.uptop.gladstest.utils.AppConfig;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataManager {
    private Context mContext;

    @Inject
    RestService mRestService;
    @Inject
    RealmManager mRealmManager;

    public DataManager() {
        this.mContext = App.getContext();

        DataManagerComponent component = DaggerService.getComponent(DataManagerComponent.class);
        if(component == null) {
            component = DaggerDataManagerComponent.builder()
                    .appComponent(App.getAppComponent())
                    .localModule(new LocalModule())
                    .networkModule(new NetworkModule())
                    .build();
            DaggerService.registerComponent(DataManagerComponent.class, component);
        }
        component.inject(this);
    }


    public Observable<PostsRealm> getPostsObsFromNetwork(String category) {
        return mRestService.getPosts(category, AppConfig.ACCESS_TOKEN)
                .compose(new RestCallTransformer<>())
                .flatMap(Observable::just) // преобразуем список List в последовательность
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(postsResponse -> {
                    mRealmManager.savePostsResponseToRealm(postsResponse);
                })
                .flatMap(productRes -> Observable.empty());
    }

    public Observable<CategoriesRealm> getCategoriesObsFromNetwork() {
        return mRestService.getCategories(AppConfig.ACCESS_TOKEN)
//                .compose(new RestCallTransformer<>())
                .flatMap(Observable::just) // преобразуем список List в последовательность
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(postsResponse -> mRealmManager.saveCategoriesToRealm(postsResponse))
                .flatMap(productRes -> Observable.empty());
    }



    public Context getContext(){return mContext;}

    public RealmManager getRealmManager() {
        return mRealmManager;
    }

    public Observable<PostsRealm> getPostsFromRealm(String category) {
        return getRealmManager().getPostsFromRealm(category);
    }

    public Observable<CategoriesRealm> getCategoriesFromRealm() {
        return getRealmManager().getCategoriesFromRealm();
    }
}
