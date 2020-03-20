package com.example.myapplicationlifesource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplicationlifesource.R;
import com.example.myapplicationlifesource.model.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<com.example.myapplicationlifesource.model.User> {
    private List<User> list;

    private View v;

    public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView name = v.findViewById(R.id.item_text);
        ImageView bloodTye = v.findViewById(R.id.item_blood);

        if (list.size() != 0) {
            name.setText(list.get(position).getName());


            try {
                switch (list.get(position).getBloodType()) {
                    case "AB-":
                        bloodTye.setImageResource(R.drawable.bloodab);
                        break;
                    case "AB+":
                        bloodTye.setImageResource(R.drawable.bloodabplus);
                        break;
                    case "A":
                        bloodTye.setImageResource(R.drawable.bloodaplus);
                        break;
                    case "A-":
                        bloodTye.setImageResource(R.drawable.blooda);
                        break;
                    case "B+":
                        bloodTye.setImageResource(R.drawable.bloodbplus);
                        break;
                    case "B-":
                        bloodTye.setImageResource(R.drawable.bloodb);
                        break;
                    case "O-":
                        bloodTye.setImageResource(R.drawable.bloodo);
                        break;
                    case "O+":
                        bloodTye.setImageResource(R.drawable.bloodoplus);
                        break;

                }
            } catch (RuntimeException e) {
                Toast.makeText(getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return v;
    }


    public void addElement(User element) {

        list.add(element);
    }
}
