package com.example.familyplanning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MethodFragment extends Fragment implements selectListener{
    private RecyclerView fpRecycleView;
    private LinearLayoutManager layoutManager;
    private List<CardItems> methodList;
    private DatabaseReference dbref;
    private Adapter adapter;
    private SearchView searchField;
    private TextView fpText;
    public MethodFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.method_fragment, container, false);
        fpRecycleView=view.findViewById(R.id.fpRecycleView);
        fpText=view.findViewById(R.id.fpText);
        dbref= FirebaseDatabase.getInstance().getReference("MethodsList");
        dbref.keepSynced(true);
        initData();
        searchField = view.findViewById(R.id.searchField);
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
        layoutManager=new LinearLayoutManager( getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(true);
        fpRecycleView.setLayoutManager(layoutManager);
        adapter=new Adapter(methodList, this);
        fpRecycleView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    private void initData() {
        methodList=new ArrayList<>();
        dbref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0){
                fpText.setText("No Available methods now");
                }
                else{

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    CardItems cardData=dataSnapshot.getValue(CardItems.class);
                    methodList.add(cardData);
                }
                adapter.notifyDataSetChanged();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(int position) {

        Intent intent=new Intent(getActivity(),
                SingleMethodActivity.class);
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