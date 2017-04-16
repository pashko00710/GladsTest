package me.uptop.gladstest.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.uptop.gladstest.R;
import me.uptop.gladstest.data.storage.realm.PostsRealm;
import me.uptop.gladstest.utils.CircularTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    Picasso mPicasso;
    String defaultThumbnail;
    List<PostsRealm> mData = new ArrayList<>();

    public PostsAdapter() {
    }

    public PostsAdapter(List<PostsRealm> data) {
        mData = data;
    }

    public void addItem(PostsRealm post) {
        mData.add(post);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView thumbnail;
        public TextView votesCount;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.item_textview_title);
            desc = (TextView) v.findViewById(R.id.item_textview_desc);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            votesCount = (TextView) v.findViewById(R.id.votes_count);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostsRealm post = mData.get(position);
        holder.title.setText(post.getName());
        holder.desc.setText(post.getDesc());
        defaultThumbnail = post.getThumbnail();
        if(defaultThumbnail == null || defaultThumbnail.isEmpty()) {
            defaultThumbnail = "https://ph-files.imgix.net/14f82064-da81-4142-b8be-7f772525a18d?auto=format&fit=crop&h=570&w=430";
        }
        mPicasso.load(defaultThumbnail)
                .transform(new CircularTransformation())
                .into(holder.thumbnail);

        holder.votesCount.setText(post.getVotesCount());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
