package com.example.familyplanning;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<CardItems> methodList;
    private selectListener listener;
    public Adapter(List<CardItems>methodList,selectListener listener){
        this.methodList=methodList;
        this.listener=listener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
String method_name=methodList.get(position).getMethodname();
String desc=methodList.get(position).getUsage();
String imgUrl=methodList.get(position).getImgurl();

holder.setData(method_name,desc,imgUrl);
    }

    @Override
    public int getItemCount() {
        return methodList.size();

    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<CardItems>searchTextList){
        methodList=searchTextList;
        notifyDataSetChanged();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
          private TextView textView1;
          private TextView textView2;
          private ImageFilterView imgFilter;

        public ViewHolder(@NonNull View itemView,selectListener listener) {
            super(itemView);

         textView1=itemView.findViewById(R.id.textView1);
         textView2=itemView.findViewById(R.id.textView2);
         imgFilter=itemView.findViewById(R.id.imgNotify);
         itemView.setOnClickListener(view -> {
             if(listener!=null){
                 int pos=getAdapterPosition();
                  if(pos!=RecyclerView.NO_POSITION){
                      listener.onItemClick(pos);

                 }


             }

         });


        }

        public void setData(String method_name, String desc,String imgUrl) {
          textView1.setText(method_name);
          textView2.setText(desc);
            Glide.with(imgFilter.getContext()).load(imgUrl)
                    .circleCrop().into(imgFilter);

        }
    }
}
