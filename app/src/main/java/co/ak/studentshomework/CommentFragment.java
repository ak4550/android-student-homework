package co.ak.studentshomework;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CommentFragment extends Fragment {

    private static final String TAG = "CommentFragment";

    private ImageView imageView;
    private Context context;
    private TextView dateTxtView, descTxtView;
    private EditText commmentEdt;
    private Button commentBtn;
    private RecyclerView commentRecyclerView;
    private PostModel model;


    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.comment_image_view);
        dateTxtView = view.findViewById(R.id.comment_date_text);
        descTxtView = view.findViewById(R.id.comment_desc_text);
        commmentEdt = view.findViewById(R.id.comment_txt_field);
        commentBtn = view.findViewById(R.id.comment_btn);
        commentRecyclerView = view.findViewById(R.id.comment_recycler_view);

        if (getArguments() != null) {
            CommentFragmentArgs args = CommentFragmentArgs.fromBundle(getArguments());
            model = args.getHomeWorkExtra();
            Log.d(TAG, "onViewCreated: " + model.getImageUrl());
            Glide.with(context)
                    .load(model.getImageUrl())
                    .into(imageView);
            descTxtView.setText(String.valueOf(model.getDescription()));
            dateTxtView.setText(String.valueOf(model.getDate()));

        }

        FirebaseRecyclerOptions<CommentModel> options =
                new FirebaseRecyclerOptions.Builder<CommentModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Comments/" + model.getPostId()), CommentModel.class)
                        .build();

        FirebaseRecyclerAdapter adapter =
                new FirebaseRecyclerAdapter<CommentModel, CommentRecyclerViewHolder>(options) {


                    @NonNull
                    @Override
                    public CommentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
                        return new CommentRecyclerViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull CommentRecyclerViewHolder holder, int position, @NonNull CommentModel model) {
                        holder.commentTextView.setText(model.getComment());
                        Log.d(TAG, "onBindViewHolder: comment " + model.getComment());
                    }
                };

        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentRecyclerView.setAdapter(adapter);
        adapter.startListening();


        commentBtn.setOnClickListener((v) -> {
            Toast.makeText(context, "Comment Clicked", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(commmentEdt.getText().toString())) {
                Toast.makeText(context, "Please Comment First", Toast.LENGTH_SHORT).show();
            } else {
                updateCommentFirebase();
            }
        });
    }

    private void updateCommentFirebase() {

        // testing
        CommentModel comment = new CommentModel();
        comment.setComment(String.valueOf(commmentEdt.getText()));
        DatabaseReference commentDatabase = FirebaseDatabase.getInstance().getReference("Comments");
        Log.d(TAG, "updateCommentFirebase: id " + model.getPostId());
        commentDatabase.child(String.valueOf(model.getPostId())).child(commentDatabase.push().getKey()).setValue(comment);
        Toast.makeText(context, "Commented", Toast.LENGTH_SHORT).show();
        commmentEdt.setText("");
    }
}