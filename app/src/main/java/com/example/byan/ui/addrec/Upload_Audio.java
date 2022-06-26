package com.example.byan.ui.addrec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.byan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import androidx.annotation.Nullable;


public class Upload_Audio extends Fragment {

    private TextView filename;
    private Button buttonVirw; // this button will not be clickable if the user did not pick the audio file to upload
    public static final int PICK_FILE=99;
    private ProgressDialog mProgress;
    private StorageReference mStorage;
    private EditText nameRec;
    private String fileName = "";
    private TextView result;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload__audio, container, false);
        Button button = (Button) view.findViewById(R.id.backfromUpload); // we go back to addRec_Options() page
        buttonVirw = (Button) view.findViewById(R.id.Explor);
        filename = view.findViewById(R.id.nameFile);
        nameRec = view.findViewById(R.id.reciterName);
        result = view.findViewById(R.id.res); // result of the uploading process

        mProgress= new ProgressDialog(Upload_Audio.this.getActivity());
        mStorage= FirebaseStorage.getInstance().getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.frameLayout, new addRec_Options());
                fr.commit();
            }
        });
        buttonVirw.setEnabled(false);
        filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAudio();
            }
        });

        return view;
    }

    private void selectAudio() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_FILE);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == Activity.RESULT_OK){
            if( data!=null && data.getData()!=null) {
                buttonVirw.setEnabled(true);
                filename.setText(getNameFromUri(data.getData())); // name the audio file chosen by the user

                buttonVirw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(nameRec.getText())){
                            result.setText("خطأ في الاضافه ,اسم القارئ فارغ");
                        }else {

                            Uri uri = data.getData();
                            upAudio(uri);
                        }
                    }
                });
            }
        }
    }


    @SuppressLint("Range")
    public String getNameFromUri(Uri uri){

        Cursor cursor  = getActivity().getContentResolver().query(uri, new String[]{
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
        }
        if (cursor != null) {
            cursor.close();
        }

        return fileName;
    }

    private void upAudio(Uri uri){
        mProgress.setMessage("رفع الملف لقاعدة البيانات...");
        mProgress.show();

        StorageReference filePath = mStorage.child("Audio").child(fileName+"__"+nameRec.getText().toString());

        filePath.putFile(uri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                            // message for error
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        result.setText("تمت الاضافة بنجاح");
                    }
                });



    }


}