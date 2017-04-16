package me.uptop.gladstest.di.components;

import javax.inject.Singleton;

import dagger.Component;
import me.uptop.gladstest.data.managers.DataManager;
import me.uptop.gladstest.di.modules.LocalModule;
import me.uptop.gladstest.di.modules.NetworkModule;

@Component(dependencies = AppComponent.class ,modules = {LocalModule.class, NetworkModule.class})
@Singleton
public interface DataManagerComponent {
    void inject(DataManager dataManager);
}
