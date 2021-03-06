package raya.cs.birzeit.simpleblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle;
    private TextView mBlogSingleDesc;
    private Button mBlogSingleRemoveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth=FirebaseAuth.getInstance();

        mPost_key=getIntent().getExtras().getString("blog_id");

        mBlogSingleDesc=(TextView)findViewById(R.id.singleBlogDesc);
        mBlogSingleTitle=(TextView)findViewById(R.id.singleBlogTitle);
        mBlogSingleImage=(ImageView)findViewById(R.id.singleBlogImage);
        mBlogSingleRemoveBtn=(Button)findViewById(R.id.mSingleRemoveBtn);


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title=(String)dataSnapshot.child("title").getValue();
                String post_desc=(String)dataSnapshot.child("desc").getValue();
                String post_image=(String)dataSnapshot.child("image").getValue();
                String post_uid=(String)dataSnapshot.child("uid").getValue();

                mBlogSingleTitle.setText(post_title);
                mBlogSingleDesc.setText(post_desc);

                if (post_image.isEmpty())
                    mBlogSingleImage.setVisibility(View.GONE);
             else    Picasso.with(BlogSingleActivity.this).load(post_image).into(mBlogSingleImage);

                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    mBlogSingleRemoveBtn.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBlogSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mPost_key).removeValue();

                Intent mainIntent=new Intent(BlogSingleActivity.this,Community.class);
                startActivity(mainIntent);
            }
        });

    }
}
