package raya.cs.birzeit.simpleblogapp;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

@SuppressLint("WrongViewCast")
public class Community extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private RecyclerView blogList;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    public TextView comments;
    private boolean mProgressLike = false;
    private static final String TAG = "Community";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView menuItemView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate((R.layout.activity_community), container, false);

        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //create an Intent object
                Intent intent = new Intent(getActivity(), AskCommunity.class);
                startActivity(intent);
            }

        });
setHasOptionsMenu(true);
        menuItemView=rootView.findViewById(R.id.tv);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }

            }
        };


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
         //mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Comments");


        mDatabase.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        // mDatabaseComment.keepSynced(true);

        blogList = (RecyclerView) rootView.findViewById(R.id.blog_list);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager(new LinearLayoutManager(getActivity()));


        Bundle intent = getActivity().getIntent().getExtras();
        if(intent != null){
            String publisher = intent.getString("publisherid");
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid",publisher);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply();
//               getSupportFragmentManager().beginTransaction().replace(R.id.)

            }
        }else{

        }
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth.addAuthStateListener(mAuthListener);

        LoadData();
    }

    //
private void LoadData() {
    FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(


            Blog.class,
            R.layout.blog_row,
            BlogViewHolder.class,
            mDatabase
    ) {
        @Override
        protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

            final String post_key = getRef(position).getKey();

            viewHolder.setTitle(model.getTitle());
            viewHolder.setDesc(model.getDesc());
            viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
            viewHolder.setUsername(model.getUsername());


            viewHolder.setLikeBtn(post_key);
if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
    FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getType().equals( "Farmer" ) && model.getType().equals("Agronomists")) {
                            viewHolder.comment.setVisibility(View.GONE);
                            viewHolder.seeAllComments.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.comment.setVisibility(View.VISIBLE);
                            viewHolder.seeAllComments.setVisibility(View.GONE);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
}
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast.makeText(MainActivity.this,post_key, Toast.LENGTH_SHORT).show();

                    Intent singleBlogIntent = new Intent(getActivity(), BlogSingleActivity.class);
                    singleBlogIntent.putExtra("blog_id", post_key);
                    startActivity(singleBlogIntent);
                }
            });
           viewHolder. comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        postid=intent.getStringExtra("postid");
//                        publisherid=intent.getStringExtra("publisherid");
                    Log.i(TAG, "onClick: ");
                    Intent intent=new Intent(requireContext(),Comment.class);
                    intent.putExtra("postid",post_key);
                    intent.putExtra("publisherid",model.uid);
                    intent.putExtra("Farmer",false);
                    getContext().startActivity(intent);
                }
            });
            viewHolder. seeAllComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        postid=intent.getStringExtra("postid");
//                        publisherid=intent.getStringExtra("publisherid");
                    Log.i(TAG, "onClick: ");
                    Intent intent=new Intent(requireContext(),Comment.class);
                    intent.putExtra("postid",post_key);
                    intent.putExtra("publisherid",model.uid);
                    intent.putExtra("Farmer",true);

                    getContext().startActivity(intent);
                }
            });
            viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mProgressLike = true;

                    mDatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (mProgressLike) {

                                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                    mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                    mProgressLike = false;
                                } else {

                                    mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                    mProgressLike = false;
                                }


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
            });


        }
    };

    blogList.setAdapter(firebaseRecyclerAdapter);
}




    public void getComments(String user_id, final TextView comments) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText("View All " + snapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

public static class BlogViewHolder extends RecyclerView.ViewHolder {

    View mView;

    private ImageButton mLikeBtn;
    private ImageView comment;
    private TextView seeAllComments;

    DatabaseReference mDatabaseLike;
    FirebaseAuth mAuth;

    public BlogViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        comment=itemView.findViewById(R.id.comment);
        seeAllComments=itemView.findViewById(R.id.swwAllComments);

        mLikeBtn = (ImageButton) mView.findViewById(R.id.post_like);

        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();

        mDatabaseLike.keepSynced(true);
    }

    public void setLikeBtn(final String post_key) {

        mDatabaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(post_key).hasChild(mAuth.getUid())) {

                        mLikeBtn.setImageResource(R.drawable.likeheart);

                    } else {


                        mLikeBtn.setImageResource(R.drawable.defaultheart);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setTitle(String title) {

        TextView post_title = (TextView) mView.findViewById(R.id.singleBlogTitle);
        post_title.setText(title);
    }

    public void setDesc(String desc) {

        TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
        post_desc.setText(desc);
    }

    public void setUsername(String username) {

        TextView post_username = (TextView) mView.findViewById(R.id.post_username);
        post_username.setText(username);

    }

    public void setImage(Context ctx, String image) {

        ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
        if (!image.isEmpty())
        Picasso.with(ctx).load(image).into(post_image);
        else  post_image.setVisibility(View.GONE);


    }

//    public void getComments(String user_id, final TextView comments) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(user_id);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                comments.setText("View All " + snapshot.getChildrenCount() + " Comments");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
    public void showMenu() {


            Log.d("Raya", "run");
            PopupMenu popupMenu = new PopupMenu(getContext(), menuItemView, Gravity.CENTER);
            popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
            popupMenu.inflate(R.menu.filter_menu);
            popupMenu.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_filter) {
            showMenu();

        }
            if(item.getItemId()==R.id.action_logout){
                FirebaseAuth.getInstance().signOut();
                Intent ToWelcome = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(ToWelcome);
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //
       @Override
    public boolean onMenuItemClick(MenuItem item) {
           //super.onMenuItemClick(item);

        switch (item.getItemId()) {
            case R.id.item1:
                  LoadData();
               // LoadForRequest("Farmer");
                return true;
            case R.id.item2:

                LoadForRequest("Agronomists");

                return true;
            case R.id.item3:

                LoadForRequest("Only me");

                return true;
            default:
                return false;
        }


    }


    private void LoadForRequest(String request) {
        Query query;
        if (request!="Only me")
            query=  mDatabase.orderByChild("Type").equalTo(request);
        else
            query= mDatabase.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,query
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());

                viewHolder.setLikeBtn(post_key);
           //    getComments(post_key,viewHolder.);


                if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        User user = snapshot.getValue(User.class);
                                        if (user.getType().equals( "Farmer" ) && model.getType().equals("Agronomists")) {
                                            viewHolder.comment.setVisibility(View.GONE);
                                            viewHolder.seeAllComments.setVisibility(View.VISIBLE);
                                        } else {
                                            viewHolder.comment.setVisibility(View.VISIBLE);
                                            viewHolder.seeAllComments.setVisibility(View.GONE);

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(MainActivity.this,post_key, Toast.LENGTH_SHORT).show();

                        Intent singleBlogIntent = new Intent(getActivity(), BlogSingleActivity.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                    }
                });

                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProgressLike = true;

                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (mProgressLike) {

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                        mProgressLike = false;
                                    } else {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                        mProgressLike = false;
                                    }


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        postid=intent.getStringExtra("postid");
//                        publisherid=intent.getStringExtra("publisherid");
                        Log.i(TAG, "onClick: ");
                        Intent intent=new Intent(requireContext(),Comment.class);
                        intent.putExtra("postid",post_key);
                        intent.putExtra("publisherid",model.uid);
                        getContext().startActivity(intent);
                    }
                });
                viewHolder.seeAllComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        postid=intent.getStringExtra("postid");
//                        publisherid=intent.getStringExtra("publisherid");
                        Log.i(TAG, "onClick: ");
                        Intent intent=new Intent(requireContext(),Comment.class);
                        intent.putExtra("postid",post_key);
                        intent.putExtra("publisherid",model.uid);
                        intent.putExtra("Farmer",true);
                        getContext().startActivity(intent);
                    }
                });
            }
        };

        blogList.setAdapter(firebaseRecyclerAdapter);

    }




}


