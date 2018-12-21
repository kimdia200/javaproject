package net.skhu.javaproject_address;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView1, textView2,textView3,textView4;

        public ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            textView3 = view.findViewById(R.id.textView3);
            textView4 = view.findViewById(R.id.textView4);
            view.setOnClickListener(this);
        }

        public void setData() {
            Person person = arrayList.get(getAdapterPosition());
            textView1.setText(person.getName());
            textView2.setText(person.getAge());
            textView3.setText(person.getNumber());
            textView4.setText(person.getEmail());

        }

        @Override
        public void onClick(View v) {
            Person person = arrayList.get(super.getAdapterPosition());
            String name = person.getName();
            String age = person.getAge();
            String email = person.getEmail();
            String number = person.getNumber();
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
//            v.getContext().startActivity(intent);

            }
    }

    LayoutInflater layoutInflater;
    List<Person> arrayList;

    public MyRecyclerViewAdapter(Context context, List<Person> arrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.person, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.setData();
    }
}