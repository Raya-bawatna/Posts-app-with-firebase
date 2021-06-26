package raya.cs.birzeit.simpleblogapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentModel> commentList;

    EditText addcomment;
    TextView post;

    ImageView image_profile;
    String postid;
    String publisherid;
    String username;
    Boolean Bfarmer;
    private static final String TAG = "Comment";
    FirebaseUser firebaseUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar=findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentList);
        recyclerView.setAdapter(commentAdapter);

        image_profile = findViewById(R.id.image_profile);
        addcomment=findViewById(R.id.add_comment);
        post=findViewById(R.id.post);

        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");
        Bfarmer=intent.getBooleanExtra("Farmer",false);
//        username=intent.getStringExtra("username");
        Log.i(TAG, "onCreate: "+postid.toString() +" ;"+publisherid.toString());
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(Comment.this,"You can't send empty comment",Toast.LENGTH_SHORT).show();
                }else{
                   addComment();
                }
            }
        });
        if (getIntent()!=null)
        readComments();

        if (Bfarmer){
            addcomment.setVisibility(View.GONE);
            post.setVisibility(View.GONE);
        }else {
            addcomment.setVisibility(View.VISIBLE);
            post.setVisibility(View.VISIBLE);
        }

    }
    private void addComment(){
         DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Comments");
       // DatabaseReference   mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("Comment",addcomment.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());



        reference.push().setValue(hashMap);
        addcomment.setText("");

    }
    private void readComments(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CommentModel comment = snapshot.getValue(CommentModel.class);
                    commentList.add(comment);

                }
                Log.i(TAG, "onDataChange: "+commentList.toString());
                commentAdapter = new CommentAdapter(Comment.this,commentList);
                recyclerView.setAdapter(commentAdapter);

            }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }


}