
package com.example.regicard.FRAGMENT;


import android.os.Bundle;

import android.text.SpannableString;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;



import com.example.regicard.R;
import static com.example.regicard.MainActivity.fragmentStack;

public class RegistartFragment extends Fragment {

    View view;

    TextView textStart;

    String word = "THE TEL";
    String content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registart, container, false);

      return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textStart = view.findViewById(R.id.textStart);
        content = textStart.getText().toString();

        SpannableString spannableString = new SpannableString(content);

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#2e74b6")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textStart.setText(spannableString);

    }
}

