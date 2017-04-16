package me.uptop.gladstest.data.network.res;

import com.squareup.moshi.Json;

public class PostsResponse {
    @Json(name = "category_id")
    public int categoryId;
    public String day;
    public int id;
    public String name;
    @Json(name = "redirect_url")
    public String redirectUrl;
    @Json(name = "screenshot_url")
    public ScreenshotUrl screenshotUrl;
    public Thumbnail thumbnail;
    @Json(name = "votes_count")
    public int votesCount;
    @Json(name = "tagline")
    public String tagLine;

    public String getTagLine() {
        return tagLine;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDay() {
        return day;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public ScreenshotUrl getScreenshotUrl() {
        return screenshotUrl;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public String getThumbnailImageUrl() {
        return thumbnail.getImageUrl();
    }

    public String getScreenshotThreeHundredImage() {
        return screenshotUrl.getThreeHundredImage();
    }

    public int getVotesCount() {
        return votesCount;
    }

    //region =========================== Inner Classes =====================
    private class ScreenshotUrl {
        @Json(name = "_300px")
        public String threeHundredImage;

        public String getThreeHundredImage() {
            return threeHundredImage;
        }
    }

    private class Thumbnail {
        public int id;
        @Json(name = "media_type")
        public String mediaType;
        @Json(name = "image_url")
        public String imageUrl;
        public Metadata metadata;

        public int getId() {
            return id;
        }

        public String getMediaType() {
            return mediaType;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        private class Metadata {
        }
    }

    //endregion
}
