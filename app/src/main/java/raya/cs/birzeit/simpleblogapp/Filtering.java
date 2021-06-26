package raya.cs.birzeit.simpleblogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Filtering extends AppCompatActivity {
    private TextView typetxt;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);
        Intent intent = getIntent();
        String type = (String) intent.getExtras().get("Type");
        typetxt = findViewById(R.id.type);
        typetxt.setText(type);
        if (type.equals("Farmers")) {
            Intent intent1 = new Intent(Filtering.this,
                    MainActivity.class);
            startActivity(intent1);
        }
        //run

        ref = FirebaseDatabase.getInstance().getReference().child("Blog");



        ref.orderByChild("Type").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                   dataSnapshot.child("name").getValue();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

