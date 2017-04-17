package me.uptop.gladstest.data.network.res.model;

import com.squareup.moshi.Json;

public class Category {
    public int id;
    public String slug;
    public String name;
    public String color;
    @Json(name = "item_name")
    public String itemName;

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getItemName() {
        return itemName;
    }
}
