package me.uptop.gladstest.data.managers;

import android.content.Context;

import javax.inject.Inject;

import me.uptop.gladstest.App;
import me.uptop.gladstest.data.network.RestService;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.components.DaggerDataManagerComponent;
import me.uptop.gladstest.di.components.DataManagerComponent;
import me.uptop.gladstest.di.modules.LocalModule;
import me.uptop.gladstest.di.modules.NetworkModule;
import me.uptop.gladstest.utils.AppConfig;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DataManager {
    private Context mContext;

    @Inject
    RestService mRestService;
    @Inject
    RealmManager mRealmManager;
//    @Inject
//    PreferencesManager mPreferencesManager;

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


    public Observable<PostsRealm> getProductsObsFromNetwork(String category) {
        return mRestService.getPosts(category, AppConfig.ACCESS_TOKEN)
//                .compose(new RestCallTransformer<>())
                .flatMap(Observable::from) // преобразуем список List в последовательность
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
//                .doOnNext(productRes -> {
//                    if(!productRes.isActive()) {
//                        mRealmManager.deleteFromRealm(ProductRealm.class, productRes.getId());
//                    }
//                })
//                .filter(QuotesResponse::isActive) //пропускаем дальше только активные(неактивные не нужно показывать, они же пустые)
                .doOnNext(quotesResponse -> {
                    mRealmManager.savePostsResponseToRealm(quotesResponse);
//                    saveOnDisk(productRes); //сохраняем на диск только активные
                })
                .flatMap(productRes -> Observable.empty());
    }



    public Context getContext(){return mContext;}

//    public Observable<QuotesRealm> getQuotesFromRealm() {
//        return mRealmManager.getAllQuotesFromRealm();
//    }

    public RealmManager getRealmManager() {
        return mRealmManager;
    }

    public Observable<PostsRealm> getPostsFromRealm() {
        return mRealmManager.getPostsFromRealm();
    }
}
