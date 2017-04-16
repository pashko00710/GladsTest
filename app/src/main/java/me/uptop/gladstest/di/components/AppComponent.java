package me.uptop.gladstest.di.components;

import android.content.Context;

import dagger.Component;
import me.uptop.gladstest.di.modules.AppModule;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
