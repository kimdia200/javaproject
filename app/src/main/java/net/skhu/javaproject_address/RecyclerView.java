package net.skhu.javaproject_address;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerView extends AppCompatActivity {
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    List<Person> arrayList;
    DatabaseReference fireBaseRef;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Intent intent = getIntent();
        id = (String)intent.getStringExtra("googleID");

        arrayList = new ArrayList<Person>();

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, arrayList);
        android.support.v7.widget.RecyclerView recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myRecyclerViewAdapter);

        fireBaseRef = FirebaseDatabase.getInstance().getReference(id);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Person>> t = new GenericTypeIndicator<ArrayList<Person>>() {};
                ArrayList<Person> firebaseData  = dataSnapshot.getValue(t);
                if (firebaseData != null) {
                    arrayList.clear();
                    arrayList.addAll(firebaseData);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                    Log.e("내태그", arrayList.toString());
                }
                else
                    Toast.makeText(RecyclerView.this, "현재 표시할 데이터가 없습니다", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        fireBaseRef.addValueEventListener(listener);

    }
}
