package me.uptop.gladstest.data.managers;

import io.realm.Realm;
import io.realm.RealmResults;
import me.uptop.gladstest.data.network.res.PostsResponse;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import rx.Observable;

public class RealmManager {
    private Realm mRealmInstance;
//    private int orderId;

    public void savePostsResponseToRealm(PostsResponse postsRes) {
        Realm realm = Realm.getDefaultInstance();

        PostsRealm productRealm =  new PostsRealm(postsRes);

//        if(!postsRes.getComments().isEmpty()) {
//            Observable.from(postsRes.getComments())
//                    .doOnNext(comments -> {
//                        if(!comments.isActive()) {
//                            deleteFromRealm(CommentRealm.class, comments.getId()); //удаляем комментарии из Realm если не активны
//                        }
//                    })
//                    .filter(Comments::isActive)
//                    .map(CommentRealm::new) //преобразовываем в RealmObject
//                    .subscribe(commentRealm -> productRealm.getCommentRealm().add(commentRealm)); //добавляем в ProductRealm
//        }

//        PostsRealm entity = realm.where(PostsRealm.class)
//                .equalTo("id", productRes.getId())
//                .findFirst();
//
//        if (entity != null) {
//            int count = entity.getCount();
//            boolean isFavorite = entity.isFavorite();
//
//            productRealm.setCount(count);
//            productRealm.setFavorite(isFavorite);
//        }

        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(productRealm)); //добавляем или обновляем продукт в транзакцию

        realm.close();
    }

//    public void deleteFromRealm(Class<? extends RealmObject> entityRealmClass, String id) {
//        Realm realm = Realm.getDefaultInstance();
//
//        RealmObject entity = realm.where(entityRealmClass).equalTo("id", id).findFirst(); //находим запись по id
//
//        if(entity != null) {
//            realm.executeTransaction(realm1 -> entity.deleteFromRealm()); //удаляем из базы Realm
//        }
//        realm.close();
//    }

    public Observable<PostsRealm> getPostsFromRealm() {
        RealmResults<PostsRealm> manageProduct = getQueryRealmInstance().where(PostsRealm.class).findAllAsync();
        return manageProduct
                .asObservable() //получаем RealmResult как Observable
                .filter(RealmResults::isLoaded) //получаем только загруженные результаты (hotObservable)
                //.first() //hack, if need cold observable
                .flatMap(Observable::from); //преобразуем в Observable<ProductRealm>
    }

//    public RealmResults<PostsRealm> getAllFavoriteProducts() {
//        RealmResults<PostsRealm> likeQuotes = getQueryRealmInstance().where(PostsRealm.class).equalTo("favorite", true).findAll();
//        Log.e("here", "getAllFavoriteProducts: "+likeQuotes);
//        return likeQuotes;
//    }



    private Realm getQueryRealmInstance() {
        if(mRealmInstance == null || mRealmInstance.isClosed()) {
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }

//    public void addOrder(ProductRealm productRealm) {
//        Realm realm = Realm.getDefaultInstance();
//
//        OrdersRealm order = new OrdersRealm(productRealm, getOrderId());
//
//        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(order)); //добавляем или обновляем продукт в корзине
//
//        realm.close();
//    }
//
//    public RealmResults<OrdersRealm> getAllOrders() {
//        RealmResults<OrdersRealm> orders = getQueryRealmInstance().where(OrdersRealm.class).findAllSorted("id");
//        return orders;
//    }
//
//    public int getOrderId() {
//        Realm realm = Realm.getDefaultInstance();
//        try {
//            orderId = realm.where(OrdersRealm.class).max("id").intValue() + 1;
//        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
//            orderId = 0;
//        }
//        realm.close();
//        return orderId;
//    }
}
