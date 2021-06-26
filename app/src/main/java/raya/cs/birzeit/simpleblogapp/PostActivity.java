package raya.cs.birzeit.simpleblogapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
//import com.google.firebase.firestore.FieldValue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private EditText titleEditText;
    private EditText descEditText;
    private Button submitButton;
    private RadioButton Farm,Agro;
    public final   HashMap<String,Object> hashMap=new HashMap<>();



    private static final int PICK = 2;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    private FirebaseUser mCurrentUser;

    private DatabaseReference mDatabaseUser;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descEditText = (EditText) findViewById(R.id.descEditText);
        Farm=(RadioButton)findViewById(R.id.Farm);
        Agro=(RadioButton)findViewById(R.id.Agro);

        submitButton = (Button) findViewById(R.id.submitButton);

        progressDialog = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(PostActivity.this, Manifest.permission.
                            READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , 1);
                    } else {
                        opengallery();


                    }
                }
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }
    private void opengallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
        Log.i("here", "opengallery: ");
    }

    private void startPosting() {

        progressDialog.setMessage("Posting to Blog...");
        progressDialog.show();
        Log.i("here", "startPosting: ");
        final String title_val = titleEditText.getText().toString().trim();
        final String desc_val = descEditText.getText().toString().trim();
        final String type_farm=Farm.getText().toString();
        final String type_agro=Agro.getText().toString();




        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) ) {

            Log.i("here", "startPosting: Starting");
//
            final StorageReference filepath=storageReference.child(System.currentTimeMillis()+"."+getfileextensions(resultUri));
//            uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);
//            Picasso.with(PostActivity.this).load(imageUri).into(post_image);
            filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> data = new HashMap<>();
                            final String newUri = uri.toString();
                            Log.i("here", "onSuccess: "+newUri);
//                            data.put("myImage",newUri);
//                            mDatabaseUser.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(getApplicationContext(),"image store",Toast.LENGTH_SHORT).show();
//                                }
//                            });


                           // final DatabaseReference newPost = databaseReference.push();
                            //final   HashMap<String,Object> hashMap=new HashMap<>();
                            if (Farm.isChecked()) {

                                // newPost.child("Type").setValue(type_farm);
                                hashMap.put("Type",type_farm);

                            } else {
                                // newPost.child("Type").setValue(type_agro);
                                hashMap.put("Type",type_agro);

                            }
                            hashMap.put("title",title_val);
                            hashMap.put("desc",desc_val);
                            if(newUri != null){

                                hashMap.put("image",newUri);

                            }else if (newUri == null){
                                hashMap.put("image",null);}

                            hashMap.put("uid",mCurrentUser.getUid());
                            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


//                                    newPost.child("title").setValue(title_val);
//                                    newPost.child("desc").setValue(desc_val);
//                                    newPost.child("image").setValue(newUri);
//                                    newPost.child("uid").setValue(mCurrentUser.getUid());


                                     hashMap.put("username",dataSnapshot.child("name").getValue());




                                    databaseReference.push().setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(PostActivity.this, "Add done..", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(PostActivity.this, MainActivity.class));
                                            }
                                        }

                                    });

                                    // run

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            progressDialog.dismiss();

                        }
                    });

                }
            });
        }else {
            Toast.makeText(this, "Title or des is empty", Toast.LENGTH_SHORT).show();
            Log.i("here", "startPosting: is empty");
        }
    }

    private String getfileextensions(Uri resultUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(resultUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1&&resultCode== RESULT_OK && data!=null)
        {
            resultUri = data.getData();
//               imageButton.setImageURI(resultUri);
            Log.i("here", "onActivityResult: "+resultUri.toString());
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    opengallery();

                } else {
                    // Permission Denied
                    Toast.makeText(PostActivity.this, "Can't change image profile..", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void OnClickFarmer(View view){

    }
    public  void OnClickAgronomists(View view){

    }

}