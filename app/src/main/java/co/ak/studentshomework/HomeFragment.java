package co.ak.studentshomework;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class HomeFragment extends Fragment {

    private FloatingActionButton floatingAddBtn;
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
        floatingAddBtn.setOnClickListener((v) ->{
            Toast.makeText(context, "fuck you", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_homeFragment_to_addingProblemFragment);
        });
    }
}