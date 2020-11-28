package co.ak.studentshomework;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FloatingActionButton floatingAddBtn;
    private RecyclerView recyclerView;
    private Context context;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingAddBtn = view.findViewById(R.id.floating_add_btn);
        recyclerView = view.findViewById(R.id.home_recycler_view);
        floatingAddBtn.setOnClickListener((v) ->{
            Toast.makeText(context, "fuck you", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_homeFragment_to_addingProblemFragment);
        });
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){

        
        FirebaseRecyclerOptions<HomeworkModel> options =
                new FirebaseRecyclerOptions.Builder<HomeworkModel>()
                .setQuery(FirebaseDatabase.getInstance()
                        .getReference("users"), HomeworkModel.class)
                .build();


        FirebaseRecyclerAdapter adapter =
                new FirebaseRecyclerAdapter<HomeworkModel, HomeRecyclerViewHolder>(options){

                    @NonNull
                    @Override
                    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(context)
                                .inflate(R.layout.home_recycler_view_item, null, false);
                        return new HomeRecyclerViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position, @NonNull HomeworkModel model) {
                        Glide.with(context)
                                .load(model.getImageUrl())
                                .into(holder.homeImage);
                        holder.descriptionText.setText(String.valueOf(model.getDescription()));
                        holder.dateText.setText(String.valueOf(model.getDate() + " " + model.getTimeStamp()));
                    }
                };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}