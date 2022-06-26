package com.example.byan;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.byan.databinding.ActivityMainBinding;
import com.example.byan.ui.about.about_fragment;
import com.example.byan.ui.addrec.addRec_Options;
import com.example.byan.ui.history.historyFragment;
import com.example.byan.ui.home.homeFragment;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private @NonNull ActivityMainBinding bottonvi;
    private homeFragment homeFragment=new homeFragment();
    historyFragment historyFragment = new historyFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottonvi=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bottonvi.getRoot());
        reqFragment(homeFragment);

        bottonvi.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        if(historyFragment.getIsPlaying()){
                            historyFragment.onStop();
                            reqFragment(homeFragment);

                        }else {
                            reqFragment(homeFragment);
                        }
                        return true;


                      case R.id.nav_addrec:
                            if (homeFragment.getIsRecording()) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(homeFragment.getActivity());
                                alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        homeFragment.stopRecording();
                                        homeFragment.recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_logo));
                                        homeFragment.setIsRecording(false);
                                        reqFragment(new addRec_Options());
                                    }
                                });
                                alertDialog.setNegativeButton("الغاء" , null);
                                alertDialog.setTitle("لايزال الصوت يسجل");
                                alertDialog.setMessage("هل تريد ايقاف التسجيل وانتقال إلى المساهمة");
                                alertDialog.create().show();
                            }
                            else if(historyFragment.getIsPlaying()){
                                historyFragment.onStop();
                                reqFragment(new addRec_Options());
                            }
                            else  {
                                reqFragment(new addRec_Options());
                            }
                        return true;

                    case R.id.nav_history:
                        if (homeFragment.getIsRecording()) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(homeFragment.getActivity());
                            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    homeFragment.stopRecording();
                                    homeFragment.recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_logo));
                                    homeFragment.setIsRecording(false);
                                    reqFragment(new historyFragment());
                                }
                            });
                            alertDialog.setNegativeButton("الغاء" , null);
                            alertDialog.setTitle("لايزال الصوت يسجل");
                            alertDialog.setMessage("هل تريد ايقاف التسجيل والانتقال إلى السجل");
                            alertDialog.create().show();
                        }
                        else{
                            reqFragment(historyFragment);
                        }
                        return true;

                    case R.id.nav_about:
                        if (homeFragment.getIsRecording()) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(homeFragment.getActivity());
                            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    homeFragment.stopRecording();
                                    homeFragment.recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_logo));
                                    homeFragment.setIsRecording(false);
                                    reqFragment(new about_fragment());
                                }
                            });
                            alertDialog.setNegativeButton("الغاء" , null);
                            alertDialog.setTitle("لايزال الصوت يسجل");
                            alertDialog.setMessage("هل تريد ايقاف التسجيل والانتقال إلى عن التطبيق");
                            alertDialog.create().show();
                        }
                        else if(historyFragment.getIsPlaying()){
                        historyFragment.onStop();
                        reqFragment(new about_fragment());

                    }else {
                            reqFragment(new about_fragment());
                        }
                        return true;
                }
                return false;
            }
        });

    }
    private void reqFragment(Fragment fragment){
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}