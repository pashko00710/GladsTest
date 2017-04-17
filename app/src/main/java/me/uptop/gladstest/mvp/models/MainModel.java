package me.uptop.gladstest.mvp.models;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import me.uptop.gladstest.data.storage.realm.CategoriesRealm;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import rx.Observable;
import rx.Subscription;

public class MainModel extends AbstractModel {
//    String category;
    @RxLogObservable
    public Observable<PostsRealm> allQuotesObs(String category) {
        Observable<PostsRealm> disk = fromDisk(category);
        Observable<PostsRealm> network = fromNetwork(category);
        return Observable.mergeDelayError(disk, network)
                .distinct(PostsRealm::getId);
    }

    public Subscription allCategoriesSubscribe() {
        return allCategories().subscribe();
    }

    @RxLogObservable
    public Observable<PostsRealm> fromNetwork(String category) {
        if(category == null) {
            return mDataManager.getPostsObsFromNetwork("tech");
        } else {
            return mDataManager.getPostsObsFromNetwork(category);
        }
    }

    @RxLogObservable
    public Observable<PostsRealm> fromDisk(String category) {
        return mDataManager.getPostsFromRealm(category);
    }

    @RxLogObservable
    public Observable<CategoriesRealm> allCategories() {
        Observable<CategoriesRealm> fromDisk = fromDiskCategories();
        Observable<CategoriesRealm> fromNetwork = fromNetworkCategories();
        return Observable.mergeDelayError(fromDisk, fromNetwork)
                .distinct(CategoriesRealm::getId);
    }

    @RxLogObservable
    private Observable<CategoriesRealm> fromNetworkCategories() {
        return mDataManager.getCategoriesObsFromNetwork();
    }

    @RxLogObservable
    private Observable<CategoriesRealm> fromDiskCategories() {
        return mDataManager.getCategoriesFromRealm();
    }

//
//    public void changeCategory(CategoriesRealm item) {
//        category = item.getSlug();
//        mDataManager.getPostsObsFromNetwork(category);
//    }
}
