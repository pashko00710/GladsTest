package me.uptop.gladstest.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.uptop.gladstest.data.managers.DataManager;

@Module
public class ModelModule {
    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new DataManager();
    }
}
