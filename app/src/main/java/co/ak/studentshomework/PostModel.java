package co.ak.studentshomework;

import android.os.Parcel;
import android.os.Parcelable;

public class PostModel implements Parcelable {
    private String date;
    private String imageUrl;
    private String status = "unsolved";
    private String description;
    private String timeStamp;
    private String userId;
    private String postId;

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    protected PostModel(Parcel in) {
        date = in.readString();
        imageUrl = in.readString();
        status = in.readString();
        description = in.readString();
        timeStamp = in.readString();
        userId = in.readString();
        postId = in.readString();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public PostModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(imageUrl);
        dest.writeString(status);
        dest.writeString(description);
        dest.writeString(timeStamp);
        dest.writeString(userId);
        dest.writeString(postId);
    }
}
