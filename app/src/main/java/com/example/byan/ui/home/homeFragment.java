package com.example.byan.ui.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.byan.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class  homeFragment extends Fragment implements View.OnClickListener {

    public ImageButton recordBtn;
    private TextView filenameText;
    private boolean isRecording = false;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21 ; //random
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private ProgressDialog mProgress;
    private TextView timer;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = 15000;
    private String recordPath="";


    public boolean getIsRecording(){
        return isRecording;
    }

    public void setIsRecording(boolean bol){
        isRecording =bol;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recordBtn = view.findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){


                    case R.id.record_btn:
                        if (isRecording) {
                            //stop recording
                            stopRecording();
                            recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_logo));
                            isRecording = false;
                            filenameText.setText("توقف التسجيل الرجاء تسجيل مقطع صوتي مدتة (15 ثانية) ليظهر اسم القارئ ");

                        }
                        else{
                            //start recording
                            if(checkPremissions()) {
                                startRecording();
                                recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.start_recording_logo));
                                isRecording = true;
                            }
                        }
                        break;
                }
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timer = view.findViewById(R.id.record_timer);
        filenameText = view.findViewById(R.id.record_filename);
        mProgress= new ProgressDialog(homeFragment.this.getActivity());

    }

    @Override
    public void onClick(View v) {
        // We don't do anything here
    }


    public void stopRecording() {
        mCountDownTimer.cancel();
        mTimeLeftInMillis = 15000;
        filenameText.setText("توقف التسجيل");
        mediaRecorder.stop();
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.release();
        mediaRecorder = null;
    }




    private void startRecording() {
        timer.setVisibility(View.VISIBLE);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_ss", Locale.CANADA); //check this
        Date now = new Date();
        recordFile = "Recroding" + formatter.format(now) + ".mp3"; // name of the file if the record did not complete 15s

        filenameText.setText("بدء التسجيل" );


        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile); // all the records stored in internal storage
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(16 * 22050);
        mediaRecorder.setAudioSamplingRate(22050);
        mediaRecorder.setMaxDuration(15000);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

             mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                 @Override
                 public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                     if (i == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                         stopRecording();
                         recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_logo));
                         isRecording = false;

                         mProgress= new ProgressDialog(homeFragment.this.getActivity());
                         mProgress.setMessage("جار التعرف على القارئ...");
                         mProgress.show();
                         // upload the audio to the model
                         uploadFile(recordPath, recordFile);
                     }
                 }
             });
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.start();
    }

    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted =  String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        timer.setText(timeLeftFormatted);
    }

    private boolean checkPremissions() {
        if(ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    private void uploadFile(String path, String fileName){

        // type of the uploaded file
        MediaType MEDIA_TYPE = MediaType.parse("audio/mp3");
        OkHttpClient client = new OkHttpClient();

        // path of the recorded file
        System.out.print(path + "/" + fileName);
        File file = new File(path + "/" + fileName);

        // create the request
        Request request1 = new Request.Builder()
                .url("http://bayanGP.pythonanywhere.com") //url to the server
                .post(RequestBody.create(file, MEDIA_TYPE))
                .build();

        // execute the request
        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                rename(recordFile, "غير معروف"); // rename the recorded file based on the result
                action("حدث خطأ");
                mProgress.dismiss();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                rename(recordFile, res);
                action(res);
                mProgress.dismiss();

            }
        });
    }

    private void action(String message){
        // show the result in a new page based on the response or failure
        Bundle bundle = new Bundle();
        bundle.putString("key", message);
        result f = new result();
        f.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, f).commit();
    }

    private void rename( String fileName, String reciterName){
        File sdcard = getActivity().getExternalFilesDir("/");
        File record = new File(sdcard,  fileName);
        File to = new File(sdcard,reciterName + "-" + fileName);
        record.renameTo(to);
    }

}