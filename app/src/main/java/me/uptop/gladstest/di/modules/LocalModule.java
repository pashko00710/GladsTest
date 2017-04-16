package me.uptop.gladstest.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.uptop.gladstest.data.managers.RealmManager;

@Module
public class LocalModule {
    @Provides
    @Singleton
    RealmManager provideRealmManager() {
        return new RealmManager();
    }
}
