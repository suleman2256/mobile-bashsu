package ru.bsu.application.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ru.bsu.application.adapter.FAQAdapter;
import ru.bsu.application.R;
import ru.bsu.application.dto.Constants;

public class FAQFragment extends Fragment {

    public FAQFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        ExpandableListView listView = view.findViewById(R.id.expListView);

        ArrayList<ArrayList<String>> groups = new ArrayList<>();
        ArrayList<String> children1 = new ArrayList<>();
        ArrayList<String> children2 = new ArrayList<>();
        ArrayList<String> children3 = new ArrayList<>();
        ArrayList<String> children4 = new ArrayList<>();
        ArrayList<String> children5 = new ArrayList<>();
        children1.add(Constants.text1);
        groups.add(children1);
        children2.add(Constants.text2);
        groups.add(children2);
        children3.add(Constants.text3);
        groups.add(children3);
        children4.add(Constants.text4);
        groups.add(children4);
        children5.add(Constants.text5);
        groups.add(children5);

        FAQAdapter adapter = new FAQAdapter(view.getContext(), groups);
        listView.setAdapter(adapter);

        return view;
    }
}