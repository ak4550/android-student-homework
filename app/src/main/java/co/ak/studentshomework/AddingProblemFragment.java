package co.ak.studentshomework;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class AddingProblemFragment extends Fragment {

    private static final String TAG = "AddingProblemFragment";

    private final int IMAGE_CHOOSER_CODE = 33;

    private Context context;
    private TextView userProblemTxt;
    private Button chooseImageBtn, submitBtn;
    private ImageView problemImageView;
    private Uri mImageUri;

    private StorageReference storageReference;
    private StorageReference fileReference;

    public AddingProblemFragment() {
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
        return inflater.inflate(R.layout.fragment_adding_problem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userProblemTxt = view.findViewById(R.id.add_problem_text);
        chooseImageBtn = view.findViewById(R.id.add_problem_choose_image_btn);
        submitBtn = view.findViewById(R.id.add_problem_submit_btn);
        problemImageView = view.findViewById(R.id.add_problem_imageview);

        storageReference = FirebaseStorage.getInstance().getReference("images");

        chooseImageBtn.setOnClickListener((v) ->{
            getImage();
        });

        submitBtn.setOnClickListener((v) ->{
             uploadToFirebase();
            Toast.makeText(context, "submit button", Toast.LENGTH_SHORT).show();
        });
    }

    private void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CHOOSER_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_CHOOSER_CODE && resultCode == RESULT_OK
            && data != null && data.getData() != null){

            mImageUri = data.getData();
            Glide.with(context)
                    .load(mImageUri)
                    .into(problemImageView);

            chooseImageBtn.setVisibility(View.INVISIBLE);
            problemImageView.setVisibility(View.VISIBLE);
        }
    }

    private String getFileExtension(Uri uri){
//        ContentResolver cr = context.getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getMimeTypeFromExtension(cr.getType(uri));
        String imageUri = uri.toString();
        Log.d(TAG, "getFileExtension: " + imageUri);
        return imageUri.substring(imageUri.lastIndexOf('.'));
    }

    private void uploadToFirebase(){
        if(mImageUri == null){
            Toast.makeText(context, "Please choose an image", Toast.LENGTH_SHORT).show();
        }/*else if(userProblemTxt.getText().toString().trim().equals("")){
            Toast.makeText(context, "Please enter the problem description", Toast.LENGTH_SHORT).show();
        }*/else{
            // TODO: 25-11-2020 upload file to firebase


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
            String stringDate = date.format(calendar.getTime());
            String stringTime = time.format(calendar.getTime());

            HomeworkModel homework = new HomeworkModel();
            homework.setDate(stringDate); // set date
            homework.setTimeStamp(stringTime);  // set time
            homework.setDescription(userProblemTxt.getText().toString());  // set description
//            if(getImageUrl().equals("")){
//                Toast.makeText(context, "Empty image url", Toast.LENGTH_SHORT).show();
//            }else{
//                homework.setImageUrl(getImageUrl());
//            }
            homework.setImageUrl(getImageUrl());


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        // this isn't pointing to user id could be a error check out
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(databaseReference.push().getKey())).setValue(homework)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "victory", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(String.valueOf(databaseReference.push().getKey())).setValue(homework);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    private String getImageUrl(){

        final String[] imageUrl = new String[1];

        storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri))
        .putFile(mImageUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl[0] = uri.toString();
                        Log.d(TAG, "onSuccess: " + uri.toString());
                    }
                });
//                imageUrl[0] = String.valueOf(taskSnapshot.getMetadata().getReference().getDownloadUrl());
//                Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "fuck you not uploaded", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(context, imageUrl[0], Toast.LENGTH_SHORT).show();
        
        return imageUrl[0];
    }
}