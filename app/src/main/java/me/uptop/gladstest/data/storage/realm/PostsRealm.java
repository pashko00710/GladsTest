package me.uptop.gladstest.data.storage.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import me.uptop.gladstest.data.network.res.model.Post;

public class PostsRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private int categoryId;
    private String name;
    private String redirectUrl;
    private String screenshotUrl;
    private String thumbnail;
    private int votesCount;
    private String desc;

    public PostsRealm() {

    }

    public PostsRealm(Post postsRes) {
        id = postsRes.getId();
        categoryId = postsRes.getCategoryId();

        name = postsRes.getName();
        redirectUrl = postsRes.getRedirectUrl();
        screenshotUrl = postsRes.getScreenshotThreeHundredImage();
        thumbnail = postsRes.getThumbnailImageUrl();
        votesCount = postsRes.getVotesCount();
        desc = postsRes.getTagLine();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }
}
