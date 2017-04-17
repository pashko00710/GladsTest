package me.uptop.gladstest.data.network;

import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import me.uptop.gladstest.data.network.error.ErrorUtils;
import me.uptop.gladstest.data.network.error.ForbiddenApiError;
import me.uptop.gladstest.data.network.error.NetworkAvailableError;
import me.uptop.gladstest.utils.NetworkStatusChecker;
import retrofit2.Response;
import rx.Observable;

import static me.uptop.gladstest.mvp.presenters.AbstractPresenter.TAG;


public class RestCallTransformer<R> implements Observable.Transformer<Response<R>, R> {

    @Override
    @RxLogObservable
    public Observable<R> call(Observable<Response<R>> responseObservable) {
        return NetworkStatusChecker.isInternetAvialable()
                .flatMap(aBoolean -> aBoolean ? responseObservable : Observable.error(new NetworkAvailableError()))
                .flatMap(rResponse -> {
                    Log.e(TAG, "callRest: "+rResponse.code());
                    switch (rResponse.code()) {
                        case 200:
                            return Observable.just(rResponse.body());
                        case 304:
                            return Observable.empty();
                        case 403:
                            return Observable.error(new ForbiddenApiError());
                        default:
                            return Observable.error(ErrorUtils.parseError(rResponse));
                    }
                });
    }
}
