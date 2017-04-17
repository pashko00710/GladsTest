package me.uptop.gladstest.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.uptop.gladstest.R;
import me.uptop.gladstest.data.storage.realm.CategoriesRealm;

public class CategoriesSpinnerAdapter  extends ArrayAdapter {

    List<CategoriesRealm> categories;
    String nameCategory;
    Context context;

    public CategoriesSpinnerAdapter(Context context, List<CategoriesRealm> categories) {
        super(context, R.layout.spinner_categories, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoriesRealm category = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_categories, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.spinner_name_text);
        nameCategory = category.getName();
        name.setText(category.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CategoriesRealm category = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(android.R.id.text1);
        name.setText(category.getName());

        return convertView;
    }

    @Override
    public CategoriesRealm getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
