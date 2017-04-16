package me.uptop.gladstest.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }
}
