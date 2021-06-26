package raya.cs.birzeit.simpleblogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public TextView comments;
    public ImageView comment;

    private RecyclerView blogList;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;

    private DatabaseReference mDatabaseLike;


    // private DatabaseReference mDatabaseComment;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean mProgressLike = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blogList = (RecyclerView) findViewById(R.id.blog_list);
//        blogList.setHasFixedSize(true);
//        blogList.setLayoutManager(new LinearLayoutManager(this));
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new Community()).commit();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Community()).commit();


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_community:
                            selectedFragment = new Community();
                            break;
                        case R.id.nav_profile:
                           selectedFragment = new Profile();

                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    public void OnClickPost(View view) {
    }

    public void OnClickFarmer(View view) {
    }

    public void OnClickAgronomists(View view) {
    }






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
////        int id=item.getItemId();
////            if(id==R.id.itemLogout){
////                FirebaseAuth.getInstance().signOut();
////                Intent ToWelcome = new Intent(MainActivity.this, WelcomeActivity.class);
////                startActivity(ToWelcome);
////            }
////            return true;
////        }
//
//        switch (item.getItemId()) {
//            case R.id.action_logout:
//                FirebaseAuth.getInstance().signOut();
//                Intent ToWelcome = new Intent(MainActivity.this, WelcomeActivity.class);
////                Intent ToWelcome = new Intent(MainActivity.this, SetupActivity.class);
//                startActivity(ToWelcome);
//                break;
//        }
//        return true;
//    }




//        comment=findViewById(R.id.comment);
//        comments=findViewById(R.id.comments_post);
//
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                if (firebaseAuth.getCurrentUser() == null) {
//
//                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(loginIntent);
//                }
//
//            }
//        };
//
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
//       // mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Comments");
//
//
//        mDatabase.keepSynced(true);
//        mDatabaseUsers.keepSynced(true);
//        mDatabaseLike.keepSynced(true);
//       // mDatabaseComment.keepSynced(true);
//
//        blogList = (RecyclerView) findViewById(R.id.blog_list);
//        blogList.setHasFixedSize(true);
//        blogList.setLayoutManager(new LinearLayoutManager(this));
//
//        checkUserExist();
//
//        Bundle intent = getIntent().getExtras();
//        if(intent != null){
//            String publisher = intent.getString("publisherid");
//            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
//            editor.putString("profileid",publisher);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//                editor.apply();
////               getSupportFragmentManager().beginTransaction().replace(R.id.)
//
//            }
//        }else{
//
//        }
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        checkUserExist();
//
//        mAuth.addAuthStateListener(mAuthListener);
//
//        LoadData();
//
//    }
//
//    private void LoadData() {
//        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
//
//
//                Blog.class,
//                R.layout.blog_row,
//                BlogViewHolder.class,
//                mDatabase
//        ) {
//            @Override
//            protected void populateViewHolder(final BlogViewHolder viewHolder, Blog model, int position) {
//
//                final String post_key = getRef(position).getKey();
//
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDesc(model.getDesc());
//                viewHolder.setImage(getApplicationContext(), model.getImage());
//                viewHolder.setUsername(model.getUsername());
//
//
//                viewHolder.setLikeBtn(post_key);
//
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // Toast.makeText(MainActivity.this,post_key, Toast.LENGTH_SHORT).show();
//
//                        Intent singleBlogIntent = new Intent(MainActivity.this, BlogSingleActivity.class);
//                        singleBlogIntent.putExtra("blog_id", post_key);
//                        startActivity(singleBlogIntent);
//                    }
//                });
//
//                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        mProgressLike = true;
//
//                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                if (mProgressLike) {
//
//                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//
//                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
//
//                                        mProgressLike = false;
//                                    } else {
//
//                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
//
//                                        mProgressLike = false;
//                                    }
//
//
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//
//                    }
//                });
//
//
//            }
//        };
//
//        blogList.setAdapter(firebaseRecyclerAdapter);
//    }
//
//    private void checkUserExist() {
//
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null) {
//
//            Intent userlogin = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(userlogin);
//
//        } else {
//
//            final String user_id = mAuth.getCurrentUser().getUid();
//            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    if (!dataSnapshot.hasChild(user_id)) {
//
//                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
//                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(setupIntent);
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//    }
//
//
//    public static class BlogViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        private ImageButton mLikeBtn;
//
//        DatabaseReference mDatabaseLike;
//        FirebaseAuth mAuth;
//
//        public BlogViewHolder(View itemView) {
//            super(itemView);
//
//            mView = itemView;
//
//            mLikeBtn = (ImageButton) mView.findViewById(R.id.post_like);
//
//            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
//            mAuth = FirebaseAuth.getInstance();
//
//            mDatabaseLike.keepSynced(true);
//        }
//
//        public void setLikeBtn(final String post_key) {
//
//            mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
////                    if (dataSnapshot.child(post_key).hasChild(mAuth.getUid())) {
////
////                        mLikeBtn.setImageResource(R.drawable.likeheart);
////
////                    } else {
////
////
////                        mLikeBtn.setImageResource(R.drawable.defaultheart);
////                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//
//        public void setTitle(String title) {
//
//            TextView post_title = (TextView) mView.findViewById(R.id.singleBlogTitle);
//            post_title.setText(title);
//        }
//
//        public void setDesc(String desc) {
//
//            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
//            post_desc.setText(desc);
//        }
//
//        public void setUsername(String username) {
//
//            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
//            post_username.setText(username);
//
//        }
//
//        public void setImage(Context ctx, String image) {
//
//            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
//            Picasso.with(ctx).load(image).into(post_image);
//
//        }
//    }
//    public void getComments(String user_id,final TextView comments){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(user_id);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                comments.setText("View All " + snapshot.getChildrenCount()+" Comments");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.action_add) {
//
//            startActivity(new Intent(MainActivity.this, PostActivity.class));
//
//        }
//        if (item.getItemId() == R.id.action_logout) {
//
//            logout();
//
//
//        }
//        if (item.getItemId() == R.id.menu_filter) {
//            showMenu();
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void logout() {
//
//        mAuth.signOut();
//    }
//
    public void showMenu() {
        View menuItemView = findViewById(R.id.menu_filter); // SAME ID AS MENU ID
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.filter_menu);
        popupMenu.show();
    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.item1:
////                intent.putExtra("Type", "Farmers");
////                startActivity(intent);
//      LoadData();
//                return true;
//            case R.id.item2:
////                intent.putExtra("Type", "Agronomists");
////                startActivity(intent);
//                LoadForRequest("Agronomists");
//
//                return true;
//            case R.id.item3:
////                intent.putExtra("Type", "Only me");
////                startActivity(intent);
//                LoadForRequest("Only me");
//
//                return true;
//            default:
//                return false;
//        }
//
////
////    }
//    }
//
//    private void LoadForRequest(String request) {
//        Query query;
//        if (request!="Only me")
//             query=  mDatabase.orderByChild("Type").equalTo(request);
//        else
//             query= mDatabase.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
//
//                Blog.class,
//                R.layout.blog_row,
//                BlogViewHolder.class,query
//        ) {
//            @Override
//            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
//
//                final String post_key = getRef(position).getKey();
//
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDesc(model.getDesc());
//                viewHolder.setImage(getApplicationContext(), model.getImage());
//                viewHolder.setUsername(model.getUsername());
//
//                viewHolder.setLikeBtn(post_key);
////                getComments(post_key,viewHolder.);
//
//
//
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // Toast.makeText(MainActivity.this,post_key, Toast.LENGTH_SHORT).show();
//
//                        Intent singleBlogIntent = new Intent(MainActivity.this, BlogSingleActivity.class);
//                        singleBlogIntent.putExtra("blog_id", post_key);
//                        startActivity(singleBlogIntent);
//                    }
//                });
//
//                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        mProgressLike = true;
//
//                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                if (mProgressLike) {
//
//                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//
//                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
//
//                                        mProgressLike = false;
//                                    } else {
//
//                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
//
//                                        mProgressLike = false;
//                                    }
//
//
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//                });
//
//
//
////                comment.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent=new Intent(MainActivity.this,Comment.class);
////                        startActivity(intent);
////                    }
////                });
//
//
//
//
//
//            }
//        };
//
//       blogList.setAdapter(firebaseRecyclerAdapter);
//
//    }
//
//    public  void add_comment(View view){
//        Intent intent=new Intent(MainActivity.this,Comment.class);
//        startActivity(intent);
//    }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
}

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
