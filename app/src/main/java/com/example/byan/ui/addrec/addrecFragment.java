package com.example.byan.ui.addrec;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.byan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class addrecFragment extends Fragment implements View.OnClickListener {
    private TextView result;
    private EditText nameRec,linkVoc;
    public Button myAdd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_addrec, container, false);
        myAdd =(Button) view.findViewById(R.id.add);
        myAdd.setOnClickListener(this);

        Button button = (Button) view.findViewById(R.id.backfromURL);  // we go back to addRec_Options() page
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.frameLayout, new addRec_Options());
                fr.commit();
            }
        });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        result = view.findViewById(R.id.resultAdd); // success or failure for the uploading
        nameRec =view.findViewById(R.id.name_rec);
        linkVoc =view.findViewById(R.id.link_voice);
    }

    public void onClick(@NonNull View v) {
        if(TextUtils.isEmpty(nameRec.getText())||TextUtils.isEmpty(linkVoc.getText())){
            result.setText("خطأ في الاضافه ,اسم القارئ او رابط الصوت فارغ");
        }else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(nameRec.getText().toString());
            myRef.push().setValue(linkVoc.getText().toString());
            result.setText("تمت الاضافة بنجاح");
        }
    }

    }




