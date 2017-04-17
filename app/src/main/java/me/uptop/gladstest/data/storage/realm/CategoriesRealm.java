package me.uptop.gladstest.data.storage.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import me.uptop.gladstest.data.network.res.model.Category;

public class CategoriesRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String slug;
    private String name;
    private String color;
    private String itemName;

    public CategoriesRealm() {
    }

    public CategoriesRealm(Category category) {
        id = category.getId();
        name = category.getName();
        slug = category.getSlug();
        color = category.getColor();
        itemName = category.getItemName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
