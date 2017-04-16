package me.uptop.gladstest.mvp.models;

import javax.inject.Inject;

import me.uptop.gladstest.data.managers.DataManager;
import me.uptop.gladstest.di.DaggerService;
import me.uptop.gladstest.di.components.DaggerModelComponent;
import me.uptop.gladstest.di.components.ModelComponent;
import me.uptop.gladstest.di.modules.ModelModule;

public abstract class AbstractModel {
    @Inject
    DataManager mDataManager;

    public AbstractModel() {
        ModelComponent component = DaggerService.getComponent(ModelComponent.class);
        if(component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(ModelComponent.class, component);
            //DataManagerComponent
        }
        component.inject(this);
    }

    private ModelComponent createDaggerComponent() {
        return DaggerModelComponent.builder()
                .modelModule(new ModelModule())
                .build();
    }
}
