package me.uptop.gladstest;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import me.uptop.gladstest.di.components.AppComponent;
import me.uptop.gladstest.di.components.DaggerAppComponent;
import me.uptop.gladstest.di.modules.AppModule;

public class App extends Application {
//    public static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build());


        sContext = getApplicationContext();
        createDaggerComponent();
//        sSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
    }

//    public static SharedPreferences getSharedPreferences() {
//        return sSharedPreferences;
//    }

    public static Context getContext() {return sContext;}

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void createDaggerComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(sContext))
                .build();
    }
}
