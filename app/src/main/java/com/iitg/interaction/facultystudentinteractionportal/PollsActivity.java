package com.iitg.interaction.facultystudentinteractionportal;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class PollsActivity extends Fragment  {
    ListView lv;
    FloatingActionButton addbtn;
    String currentcourse;
    String pollkey;
    ArrayList<Polls> polls;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Courses");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_polls, container, false);
        currentcourse = CourseMainPageStudent.courseID;
        return rootView;


    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        lv = getView().findViewById(R.id.lv_pollsmainlist);
        addbtn = getView().findViewById(R.id.fab_addpoll);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CreatePollActivity.class);
                startActivity(intent);
            }
        });
        final GenericTypeIndicator<ArrayList<Polls>> t = new GenericTypeIndicator<ArrayList<Polls>>() {};

        final ArrayList<String> questionlist = new ArrayList<>();
        final ArrayAdapter<String> adpater = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,questionlist);
        lv.setAdapter(adpater);
        if(polls==null)
        {
            polls = new ArrayList<Polls>();
        }
        databaseReference.child(currentcourse).child("Polls").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                polls.clear();
                questionlist.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    polls.add(data.getValue(Polls.class));
                }
                if(polls!=null)
                {
                    Collections.reverse(polls);
                    for(Polls p : polls)
                    {
                        Log.d("debug"," Datachange of another one ");
                        questionlist.add(p.question);
                    }

                    adpater.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Polls requiredpoll =  polls.get(position);

                PollLayoutActivity.clickedpoll=requiredpoll;
                Intent intent = new Intent(getContext(),PollLayoutActivity.class);
                intent.putExtra("index",position);

                startActivity(intent);
            }
        });


    }

}


