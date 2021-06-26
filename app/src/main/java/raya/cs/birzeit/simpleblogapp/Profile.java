package raya.cs.birzeit.simpleblogapp;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

//import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    private CircleImageView mSetupImageButton;
    TextView username,type_user;

    StorageTask uploadTask;
    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseUsers;

    private StorageReference mStorageImage;

    private ProgressDialog mProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
     View  view = inflater.inflate(R.layout.activity_profile, container, false);

        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_image");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(getActivity());

        mSetupImageButton =  view.findViewById(R.id.setupImageButton);
        username =  view.findViewById(R.id.username_profile);
        type_user =  view.findViewById(R.id.type_profile);

        Log.d("Raya","Open");

        loadingDataProfile();

        mSetupImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Raya","Click");

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.
                            READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , 1);
                    } else {
                        CropImage.activity().setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .start(getContext(),Profile.this);
                    }
                }

            }
        });
       // startSetupAccount();
//
//
//
//        mStorageImage= FirebaseStorage.getInstance().getReference().child("Profile_image");
//
//        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
//
//        mProgress=new ProgressDialog(getActivity());
//
//        mSetupImageButton=view.findViewById(R.id.setupImageButton);
//
//
//        mSetupImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent galleryIntent=new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent,GALLERY_REQUEST);
//            }
//        });
        return view;

    }

    private void loadingDataProfile() {
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isAdded()){
                            if (snapshot.exists()){
                                User user=snapshot.getValue(User.class);
                                Glide.with(getContext()).load(user.image).into(mSetupImageButton);
                                   username.setText(user.getUserName());
                                   type_user.setText(user.getType());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void startSetupAccount() {
        Log.d("Raya","Bisan");
        final String user_id = mAuth.getCurrentUser().getUid();

        if (mImageUri != null) {

            mProgress.setMessage("Finishing setup");
            mProgress.show();

            StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);

                    mProgress.dismiss();

                    Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                }
            });

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            mImageUri=result.getUri();
            Toast.makeText(getContext(), "Loading imnage done", Toast.LENGTH_SHORT).show();
            Uploadimage();

        }else {
            Toast.makeText(getActivity(), "Error,Try Again !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(),MainActivity.class));

        }
    }

    private String getfileExension(Uri uri)
    {
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap map=MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void Uploadimage() {

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (mImageUri!=null)
        {
            final StorageReference filereference=mStorageImage.child(System.currentTimeMillis()+"."+getfileExension(mImageUri));
            uploadTask=filereference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloaduri=  task.getResult();
                        String myuri=downloaduri.toString();
                        Glide.with(getContext()).load(myuri).into(mSetupImageButton);
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("image",""+myuri);
                        reference.updateChildren(hashMap);
                        progressDialog.dismiss();

                    }else {
                        Toast.makeText(getContext(), "Failed ! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getContext(), "No Image Select", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CropImage.activity().setAspectRatio(1,1)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .start(getContext(),this);

                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "Can't change image profile..", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

//    private void startSetupAccount() {
//        final String user_id=mAuth.getCurrentUser().getUid();
//
//        if (mImageUri!=null){
//
//            mProgress.setMessage("Finishing setup");
//            mProgress.show();
//
//            StorageReference filepath=mStorageImage.child(mImageUri.getLastPathSegment());
//
//            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    String downloadUri=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//
//                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);
//
//                    mProgress.dismiss();
//
//                    Intent loginIntent=new Intent(getActivity(),Profile.class);
//                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(loginIntent);
//
//                }
//            });
//
//        }
//
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
//
//            Uri imageuri=data.getData();
//
//            CropImage.activity(imageuri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(getActivity());
//
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if (resultCode == RESULT_OK) {
//
//                mImageUri = result.getUri();
//                mSetupImageButton.setImageURI(mImageUri);
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//
//                Exception error = result.getError();
//            }
//        }
//    }
}
