package me.uptop.gladstest.data.managers;

import io.realm.Realm;
import io.realm.RealmResults;
import me.uptop.gladstest.data.network.res.CategoriesResponse;
import me.uptop.gladstest.data.network.res.PostsResponse;
import me.uptop.gladstest.data.network.res.model.Category;
import me.uptop.gladstest.data.network.res.model.Post;
import me.uptop.gladstest.data.storage.realm.CategoriesRealm;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import rx.Observable;

public class RealmManager {
    private Realm mRealmInstance;
//    private int orderId;

    public void savePostsResponseToRealm(PostsResponse postsRes) {
        Realm realm = Realm.getDefaultInstance();

        for(Post post: postsRes.posts) {
            PostsRealm postRealm =  new PostsRealm(post);
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(postRealm)); //добавляем или обновляем продукт в транзакцию
        }

        realm.close();
    }

    public void saveCategoriesToRealm(CategoriesResponse categoriesRes) {
        Realm realm = Realm.getDefaultInstance();


        for(Category category: categoriesRes.categories) {
            CategoriesRealm postRealm =  new CategoriesRealm(category);
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(postRealm)); //добавляем или обновляем продукт в транзакцию
        }
        realm.close();
    }

    public Observable<PostsRealm> getPostsFromRealm(String category) {
        int catId = 0;
        if(category.contains("tech")) {
            catId = 1;
        } else if(category.contains("games")) {
            catId = 2;
        } else if(category.contains("podcasts")) {
            catId = 3;
        } else if(category.contains("books")) {
            catId = 4;
        }
        RealmResults<PostsRealm> manageProduct = getQueryRealmInstance().where(PostsRealm.class).equalTo("categoryId", catId).findAllAsync();
        return manageProduct
                .asObservable() //получаем RealmResult как Observable
                .filter(RealmResults::isLoaded) //получаем только загруженные результаты (hotObservable)
                //.first() //hack, if need cold observable
                .flatMap(Observable::from); //преобразуем в Observable<ProductRealm>
    }

    public Observable<CategoriesRealm> getCategoriesFromRealm() {
        RealmResults<CategoriesRealm> manageProduct = getQueryRealmInstance().where(CategoriesRealm.class).findAllAsync();
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
}
