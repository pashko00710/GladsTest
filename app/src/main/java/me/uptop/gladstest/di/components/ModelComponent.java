package me.uptop.gladstest.di.components;

import javax.inject.Singleton;

import dagger.Component;
import me.uptop.gladstest.di.modules.ModelModule;
import me.uptop.gladstest.mvp.models.AbstractModel;

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel abstractModel);
}
