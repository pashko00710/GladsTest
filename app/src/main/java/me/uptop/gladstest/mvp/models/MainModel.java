package me.uptop.gladstest.mvp.models;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import me.uptop.gladstest.data.storage.realm.PostsRealm;
import rx.Observable;

public class MainModel extends AbstractModel {
    @RxLogObservable
    public Observable<PostsRealm> allQuotesObs() {
        Observable<PostsRealm> disk = fromDisk();
        Observable<PostsRealm> network = fromNetwork();
        return Observable.mergeDelayError(disk, network)
                .distinct(PostsRealm::getId);
    }

    @RxLogObservable
    public Observable<PostsRealm> fromNetwork() {
        return mDataManager.getProductsObsFromNetwork("tech");
    }

    @RxLogObservable
    public Observable<PostsRealm> fromDisk() {
        return mDataManager.getPostsFromRealm();
    }
}
