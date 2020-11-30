package co.ak.studentshomework;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView commentTextView;

    public CommentRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        commentTextView = itemView.findViewById(R.id.comment_text_view);

    }
}
