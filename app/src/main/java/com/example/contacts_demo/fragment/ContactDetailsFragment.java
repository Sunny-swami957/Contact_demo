package com.example.contacts_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.contacts_demo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetailsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout contactButton;
    private TextView nameTV,mobTV,emailTV,editTV;
    private CircleImageView msgIV,callIV,emailIV,favIV;
    private NavController navController;
    private Bundle bundle;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.contact_detail_fragment, container, false);

        return v;
    }

    @SuppressLint({"NewApi", "FragmentBackPressedCallback"})
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactButton=(LinearLayout)view.findViewById(R.id.contact_button);
        nameTV=(TextView)view.findViewById(R.id.edit_name);
        mobTV=(TextView)view.findViewById(R.id.mobile_detail_text);
        emailTV=(TextView)view.findViewById(R.id.email_detail_text);
        editTV=(TextView)view.findViewById(R.id.edit_button);
        msgIV=(CircleImageView)view.findViewById(R.id.image_msg);
        callIV=(CircleImageView)view.findViewById(R.id.image_call);
        emailIV=(CircleImageView)view.findViewById(R.id.image_email);
        favIV=(CircleImageView)view.findViewById(R.id.image_fav);
        navController= NavHostFragment.findNavController(this);
        contactButton.setOnClickListener(this);
        editTV.setOnClickListener(this);
        bundle=getArguments();
        nameTV.setText(bundle.getString("first_name")+"  "+bundle.getString("last_name"));
        emailTV.setText(bundle.getString("email"));
        mobTV.setText(bundle.getString("phone"));
        if(mobTV.getText().length()==0)
        {
            mobTV.setVisibility(View.GONE);
            msgIV.setImageResource(R.drawable.msg);
            callIV.setImageResource(R.drawable.phone);
        }else{
            mobTV.setVisibility(View.VISIBLE);
            msgIV.setImageResource(R.drawable.select_msg);
            callIV.setImageResource(R.drawable.select_phone);
        }
        if(emailTV.getText().length()==0)
        {
            emailTV.setVisibility(View.GONE);
            emailIV.setImageResource(R.drawable.email);
        }else{
            emailTV.setVisibility(View.VISIBLE);
            emailIV.setImageResource(R.drawable.select_email);
        }
        String s=bundle.getString("fav");
        int x=Integer.parseInt(s);
        if(x==1)
            favIV.setImageResource(R.drawable.select_icon);
        else
            favIV.setImageResource(R.drawable.icon);

        favIV.setOnClickListener(this);
        msgIV.setOnClickListener(this);
        callIV.setOnClickListener(this);
        emailIV.setOnClickListener(this);
        navController= NavHostFragment.findNavController(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                navController.navigate(R.id.listFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.contact_button:
                navController.navigate(R.id.listFragment);
                break;
            case R.id.edit_button:
                navController.navigate(R.id.addFragment,bundle);
                break;
            case R.id.image_msg:
                if(!mobTV.getText().toString().isEmpty()) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    sendIntent.putExtra("address", mobTV.getText().toString());
                    startActivity(sendIntent);
                }
                break;
            case R.id.image_call:
                if(!mobTV.getText().toString().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobTV.getText().toString(), null));
                    startActivity(intent);
                }
                break;
            case R.id.image_email:
                if(!emailTV.getText().toString().isEmpty()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", emailTV.getText().toString(), null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                break;
            case R.id.image_fav:
                if(favIV.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.select_icon).getConstantState())
                {
                    favIV.setImageResource(R.drawable.icon);
                }else{
                    favIV.setImageResource(R.drawable.select_icon);
                }
                break;
            default:
                break;
        }
    }
}
