package com.ahmedbilal.realim;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class ChatActivity extends ActionBarActivity {

    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;
    List<CustomAdapterData> listItems=new ArrayList<CustomAdapterData> ();
    //ArrayAdapter<String> adapter;
    ListCustomAdapter adapter;
    String userName="";
    String senderName="";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Chat");
        if(savedInstanceState!=null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE);
        }
        setContentView(R.layout.activity_chat);
        initViews();
        userName= getIntent().getStringExtra("userName");
    }

    protected void initViews(){
        //dispatchTakePictureIntent();

        final Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText)findViewById(R.id.msgText);
                SendPush(text.getText().toString());
                text.setText("");
            }

        });
        ListView lv = (ListView) findViewById(R.id.listView);

        adapter = new ListCustomAdapter (this, R.layout.listitemrows, listItems);
        lv.setAdapter(adapter);
        if(mListState !=null) {
            lv.onRestoreInstanceState(mListState);
        }
    }

    public void addItems(String sender,String message,Bitmap bitmap) {
        CustomAdapterData msgObj = new CustomAdapterData();
        msgObj.setMessage(message);
        msgObj.setUserName (sender);
        msgObj.setImageBitmap(bitmap);
        listItems.add(msgObj);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListView lv = (ListView) findViewById(R.id.listView);
        outState.putParcelable(LIST_STATE, lv
                .onSaveInstanceState());
    }

    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.google.android.c2dm.intent.RECEIVE");

        this.registerReceiver(registrationStatusReceiver, filter);
    }

    public void onPause() {
        super.onPause();

        this.unregisterReceiver(this.registrationStatusReceiver);
    }

    private CustomParsePushBroadcastReceiver registrationStatusReceiver = new  CustomParsePushBroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject notificationData = new JSONObject(intent.getStringExtra("data"));
                senderName = notificationData.getString("userName");
                String isImage ="";
                if(notificationData.has("isImage")) {
                    isImage = notificationData.getString("isImage");
                }

                if(isImage.equalsIgnoreCase("true")){
                    // Locate the class table named "ImageUpload" in Parse.com
                    String imageObjectId = notificationData.getString("imageName");
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                            "ImageUpload");

                    // Locate the objectId from the class
                    query.getInBackground(imageObjectId,
                            new GetCallback<ParseObject>() {

                                public void done(ParseObject object,
                                                 ParseException e) {
                                    // Locate the column named "ImageName" and set
                                    // the string
                                    ParseFile fileObject = (ParseFile) object
                                            .get("ImageFile");
                                    fileObject
                                            .getDataInBackground(new GetDataCallback() {

                                                public void done(byte[] data,
                                                                 ParseException e) {
                                                    if (e == null) {
                                                        Log.d("test",
                                                                "Get the image.");
                                                        // Decode the Byte[] into
                                                        // Bitmap
                                                        Bitmap bmp = BitmapFactory
                                                                .decodeByteArray(
                                                                        data, 0,
                                                                        data.length);
                                                        if(!senderName.equalsIgnoreCase(userName)) {
                                                            addItems(senderName + " : ", null, bmp);
                                                        }


                                                    } else {
                                                        Log.d("test",
                                                                "There was a problem downloading the data.");
                                                    }
                                                }
                                            });
                                }
                            });
                    //addItems("This is image");
                }
                if(!senderName.equalsIgnoreCase(userName)) {
                    addItems(senderName + " : " , notificationData.getString("message"),null );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            addItems(userName + " : ", null, bitmap);
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] image = stream.toByteArray();

            String fileName = UUID.randomUUID().toString() + ".png";
            // Create the ParseFile
            ParseFile file = new ParseFile(fileName, image);
            // Upload the image into Parse Cloud
            file.saveInBackground();

            // Create a New Class called "ImageUpload" in Parse
            final ParseObject imgupload = new ParseObject("ImageUpload");

            // Create a column named "ImageName" and set the string
            imgupload.put("ImageName", fileName);

            // Create a column named "ImageFile" and insert the image
            imgupload.put("ImageFile", file);

            // Create the class and the columns
            imgupload.saveInBackground(
                    new SaveCallback() {
                        @Override
                        public void done(ParseException ex) {
                            if (ex == null) {
                                encodeImagetoString(imgupload.getObjectId());
                                //isSaved = true;
                            } else {
                                // Failed
                                //isSaved = false;
                            }
                        }
                    }
            );
            //mImageView.setImageBitmap(imageBitmap);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_camera)
        {
            dispatchTakePictureIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void SendPush(String message) {

        addItems(userName + " : ",  message,null);

        new AsyncTask<String, Void, String>() {
            private Exception exception;
            protected String doInBackground(String... params) {
                String msg = params[0];
                RestClient client = new RestClient();
                HashMap<String, String> map = new HashMap<String, String>();
                //map.put("API-KEY","3dd635a808ddb6dd4b6731f7c409d53dd4b14df2");
                map.put("X-Parse-Application-Id", "I0chGdodqu6WDSiTTVdSaOpNdPLd51PrCI51A5Ol");
                map.put("X-Parse-REST-API-Key", "gavImnFT6rsE4gF75WGvB3jL7xuAB9R8DDfB8wAz");
                try {
//                    HashMap<String, Object> data = new HashMap<String, Object>();
//                    data.put("data", new HashMap<String, Object>().put("alert", "Dynamic notification"));
//                    data.put("where", new HashMap<String, Object>());
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setWhere(new HashMap<String, String>());
                    HashMap<String,String> data = new HashMap<String, String>();
                    data.put("message",msg);
                    data.put("userName",userName);

                    chatMessage.setData(data);

                    LinkedTreeMap response = (LinkedTreeMap) client.postForObject(chatMessage, "https://api.parse.com/1/push", map);
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
                return "OK";
            }
            protected void onPostExecute() {
            }
        }.execute(message);
    }

    public void encodeImagetoString(String imageName) {

        new AsyncTask<String, Void, String>() {

        @Override
            protected String doInBackground(String... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;

                String imageName = (String) params[0];
                RestClient client = new RestClient();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("X-Parse-Application-Id", "I0chGdodqu6WDSiTTVdSaOpNdPLd51PrCI51A5Ol");
                map.put("X-Parse-REST-API-Key", "gavImnFT6rsE4gF75WGvB3jL7xuAB9R8DDfB8wAz");
                try {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setWhere(new HashMap<String, String>());
                    HashMap<String,String> data = new HashMap<String, String>();
                    data.put("userName",userName);
                    data.put("isImage","true");
                    data.put("imageName",imageName);
                    chatMessage.setData(data);
                    LinkedTreeMap response = (LinkedTreeMap) client.postForObject(chatMessage, "https://api.parse.com/1/push", map);
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());

                }
                return "";
            }

        }.execute(imageName, null, null);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("");
        builder.setMessage("You are leaving chat room. Are you sure?");
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
