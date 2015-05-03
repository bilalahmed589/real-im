package com.ahmedbilal.realim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class InboxActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "I0chGdodqu6WDSiTTVdSaOpNdPLd51PrCI51A5Ol", "QbCsaksXw5Q1P29ztBHjexCKemQErT7jS28Mq4Fj");
        ParsePush.subscribeInBackground("ahmedBilalChannel", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        setContentView(R.layout.activity_inbox);
        initViews();
    }

    private final void initViews() {
        Button joinButton = (Button) findViewById(R.id.JoinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);
                final EditText input = new EditText(InboxActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(InboxActivity.this);
                builder.setTitle("");
                builder.setMessage("What is your name");
                builder.setView(input);
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent chatIntent = new Intent(InboxActivity.this,ChatActivity.class);
                                chatIntent.putExtra("userName",input.getText().toString());
                                if(input.getText().toString().trim().isEmpty()){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "You must provide your name.",
                                            Toast.LENGTH_LONG);
                                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                    v.setTextColor(Color.RED);
                                    toast.show();
                                }else {
                                    startActivity(chatIntent);
                                }
                            }
                        });

                builder.setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                builder.show();

/*
                final AlertDialog alertDialog = new AlertDialog.Builder(InboxActivity.this).create(); //Read Update
                alertDialog.setTitle("Join Chat");
                alertDialog.setMessage("What is your name");
                final EditText input = new EditText(InboxActivity.this);
                alertDialog.setView(input);
//                alertDialog.setButton(0,"Dismiss", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });

                alertDialog.setButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent chatIntent = new Intent(InboxActivity.this,ChatActivity.class);
                        startActivity(chatIntent);
                    }
                });

                alertDialog.show();  //<-- See This!*/
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InboxActivity.this);
        builder.setTitle("");
        builder.setMessage("You are exiting from Real IM.");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        return;
                    }
                });

        builder.setNegativeButton("Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
        return;
    }

}
