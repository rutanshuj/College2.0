package com.example.admin.college20;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Date;

public class CreateEvent extends android.support.v4.app.Fragment {

    private Button uButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_event, container, false);

        uButton = (Button) view.findViewById(R.id.getStarted);
        uButton.setVisibility(View.VISIBLE);
        uButton.setBackgroundColor(Color.TRANSPARENT);

        uButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getContext(), CreateEvent1.class);
                startActivity(i);
            }
        });
        return view;
    }
}
