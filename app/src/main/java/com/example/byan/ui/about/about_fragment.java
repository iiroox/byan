package com.example.byan.ui.about;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.byan.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class about_fragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.screenshot__505_)
                .setDescription("بيان هو تطبيق مخصص للتعرف على قراء القرآن. بإمكانه التعرف على هوية القارئ من خلال تسجيل صوتي لقرائته. يسجل البرنامج مقطع صوتي قصير للقارئ أثناء قراءته للقرآن ومن ثم التعرف على اسم القارئ باستخدام بعض خوارزميات التعلم الآلي.  \n" )
                .addItem(new Element().setTitle("النسخة 1.0"))
                .addGroup("تواصل معنا ")
                .addEmail("ahkdrggd@gmail.com", "البريد الإلكتروني")
                //.addTwitter("")
                .addGroup(getString(R.string.application_information_group))
                .addItem(getCopyRightsElement())
                .create();
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

}