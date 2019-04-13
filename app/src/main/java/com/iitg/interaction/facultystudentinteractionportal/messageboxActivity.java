package com.iitg.interaction.facultystudentinteractionportal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class messageboxActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagebox);
        TextView subjecttv= findViewById(R.id.tv_msgbox_subject);
        TextView sendertv = findViewById(R.id.tv_sender);
        TextView receivertv = findViewById(R.id.tv_receiver);
        TextView datetv= findViewById(R.id.tv_datetime);
        TextView bodytv = findViewById(R.id.tv_body);
        Button deletebtn= findViewById(R.id.btn_deletemsg);
        Button replybtn = findViewById(R.id.btn_replymsg);


        Intent intent = getIntent();
        final String subject = intent.getStringExtra("subject");
        final String sender = intent.getStringExtra("sender");
        final String receiver = intent.getStringExtra("receiver");
        String body = intent.getStringExtra("body");
        String date = intent.getStringExtra("datetime");
        final int uniqueid =intent.getIntExtra("id",-1);


        Log.d("debug","uniquid at start msgboxactivity = "+uniqueid);

        subjecttv.setText(subject);
        sendertv.setText(sender);
        receivertv.setText(receiver);
        datetv.setText(date);
        bodytv.setText(body);

        replybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getApplicationContext(),ComposeMessage.class);
                intent2.putExtra("sender",sender);
                intent2.putExtra("subject",subject);
                startActivity(intent2);

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users");
                ArrayList<Messages> msglist=UserInfo.messages;
                Log.d("debug","uniquid in clikclistener = "+uniqueid);

                Log.d("debug","msglis "+msglist.size() + " "+uniqueid);
                if(uniqueid==-1)
                {
                    Toast.makeText(getApplicationContext(),"Not able to delete!",Toast.LENGTH_LONG).show();
                    return;
                }
                msglist.remove(uniqueid);

//                for(Messages a : msglist)
//                {
//                    if(a.uniquid.equals(uniqueid))
//                        msglist.remove(a);
//
//                    break;
//                }
                dataref.child(UserInfo.username).child("messages").setValue(msglist);

                Intent intent1 = new Intent(getApplicationContext(),MessageActivity.class);
                startActivity(intent1);
                messageboxActivity.this.finish();
            }
        });



    }
}