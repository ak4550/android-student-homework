package co.ak.studentshomework;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder{
    ImageView homeImage;
    TextView dateText, descriptionText, commentText;
    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        homeImage = itemView.findViewById(R.id.home_item_image_view);
        dateText = itemView.findViewById(R.id.home_item_date_time);
        descriptionText = itemView.findViewById(R.id.home_item_comment);
        commentText = itemView.findViewById(R.id.home_item_description);

    }
}
