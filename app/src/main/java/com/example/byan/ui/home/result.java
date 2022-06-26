package com.example.byan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.byan.R;

public class result extends Fragment {

    private ImageButton back;
    private TextView resultRec;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.endresult, container, false);
        back = view.findViewById(R.id.back);
        resultRec = view.findViewById(R.id.resultR);
        Bundle b =this.getArguments();
        String data =b.getString("key");
        resultRec.setText(data);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                homeFragment f=new homeFragment();
                f.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frameLayout,f).commit();
            }
        });

        return view;
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }



}
