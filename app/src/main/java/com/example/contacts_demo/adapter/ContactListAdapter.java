package com.example.contacts_demo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts_demo.R;
import com.example.contacts_demo.entity.Contact;
import com.example.contacts_demo.entity.ContactList;


public class ContactListAdapter extends
        RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView favImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            favImageView=(ImageView) itemView.findViewById(R.id.list_item_fav);
        }
    }

    private NavController navController;
    public ContactListAdapter(NavController navController) {
        this.navController=navController;
    }
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.contact_list_adapter_item, parent, false);

        ContactListAdapter.ViewHolder viewHolder = new ContactListAdapter.ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ContactListAdapter.ViewHolder viewHolder, final int position) {
        final Contact cont= ContactList.getInstance().getContacts().getValue().get(position);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navController.navigate();
                Bundle bundle=new Bundle();
                bundle.putString("first_name",cont.getFirst_name());
                bundle.putString("last_name",cont.getLast_name());
                bundle.putString("email",cont.getEmail());
                bundle.putString("phone",cont.getPhone());
                bundle.putString("fav", String.valueOf(cont.getFav()));
                navController.navigate(R.id.detailFragment,bundle);
            }
        });
        TextView textView = viewHolder.nameTextView;
        textView.setText(cont.getFirst_name()+" "+cont.getLast_name());
        ImageView imageView=viewHolder.favImageView;
        if(cont.getFav()==1)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return ContactList.getInstance().getContacts().getValue().size();
    }
}
