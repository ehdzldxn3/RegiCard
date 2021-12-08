
package com.example.regicard.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.regicard.R;
import static com.example.regicard.MainActivity.fragmentStack;

public class RegistartFragment extends Fragment {

    View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registart, container, false);


        final TextView content2 = (TextView) view.findViewById(R.id.content2);      // marquee 적용 으로 설정 함
        content2.setSelected(true);



      return view;
    }


}

