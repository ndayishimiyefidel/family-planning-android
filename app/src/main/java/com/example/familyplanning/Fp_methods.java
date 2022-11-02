package com.example.familyplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fp_methods extends AppCompatActivity implements selectListener{
    RecyclerView recycleView;
    LinearLayoutManager layoutManager;
    List<CardItems> methodList;
    DatabaseReference dbref;
    Adapter adapter;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_methods);
        backImg=findViewById(R.id.backImg);
        backImg.setOnClickListener(view -> onBackPressed());
        dbref= FirebaseDatabase.getInstance().getReference("MethodsList");
        dbref.keepSynced(true);
        initData();
        initRecylerView();


        SearchView searchField = findViewById(R.id.searchField);
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }

            @SuppressLint("NotifyDataSetChanged")
            private void filter(String newText) {
                List<CardItems> searchTextList=new ArrayList<>();
                for(CardItems item: methodList){
                    if(item.getMethodname().toLowerCase().contains(newText.toLowerCase())||item.getUsage().toLowerCase().contains(newText.toLowerCase())){
                        searchTextList.add(item);
                    }
                }
                adapter.filterList(searchTextList);

            }
        });

    }

    private void initData() {
     methodList=new ArrayList<>();
     dbref.addValueEventListener(new ValueEventListener() {
         @SuppressLint("NotifyDataSetChanged")
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot dataSnapshot:snapshot.getChildren())
            {

                CardItems cardData=dataSnapshot.getValue(CardItems.class);
                methodList.add(cardData);
            }
             adapter.notifyDataSetChanged();
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecylerView() {
        recycleView=findViewById(R.id.recycleView);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        adapter=new Adapter(methodList,this);
        recycleView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
//        startActivity(new Intent(getApplicationContext(), SingleMethodActivity.class));//        Toast.makeText(this,"item clicked",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,SingleMethodActivity.class);
        intent.putExtra("methodname",methodList.get(position).getMethodname());
        intent.putExtra("advantages",methodList.get(position).getAdvantages());
        intent.putExtra("disadvantages",methodList.get(position).getDisadvantages());
        intent.putExtra("period",methodList.get(position).getPeriod());
        intent.putExtra("usage",methodList.get(position).getUsage());
        intent.putExtra("category",methodList.get(position).getCategory());
        intent.putExtra("imgurl",methodList.get(position).getImgurl());

        startActivity(intent);

    }
}