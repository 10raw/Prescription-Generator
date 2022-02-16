package com.example.presco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class MainActivity extends AppCompatActivity {
    EditText recipient;
    Button sendmail;
    TextView generatedpres;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference stref;
    DatabaseReference dbref;
    private static int MICROPHONE_PERMISSION_CODE=200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         recipient = (EditText)findViewById(R.id.recipientmail);
        sendmail = (Button)findViewById(R.id.button);
        generatedpres=(TextView)findViewById(R.id.generatedpres);
        String Audiofilepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/testRecordingFile.mp3";

        if(checkmic()){
            getmicpermission();
        }

        sendmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                System.out.println("inside it");
                Properties properties = new Properties();
                properties.setProperty("mail.smtp.auth", "true");
                properties.setProperty("mail.smtp.starttls.enable", "true");
                properties.setProperty("mail.smtp.host", "smtp.gmail.com");
                properties.setProperty("mail.smtp.port", "587");
                properties.setProperty("mail.smtp.user", "heckermen2024@gmail.com");
                properties.setProperty("mail.smtp.password", "Heckermen@2024");
                String username="heckermen2024@gmail.com";
                String password="Heckermen@2024";
                Session session = Session.getDefaultInstance(properties,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username,password);
                            }
                        });
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient.getText().toString()));
                    message.setSubject("Prescription from Heckermen");
                    message.setText("Here's ur prescription \n "+generatedpres.getText().toString());

                    Transport.send(message);

                    System.out.println("message sent successfully...");

                } catch (MessagingException e) {e.printStackTrace();}
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
    public void bthRecordPressed(View v){

        try{

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            System.out.println("________"+getRecordingFilePath());
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this,"Recording Started!",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void btnStopPressed(View v) {

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;


        Toast.makeText(this,"Recording Stopped!",Toast.LENGTH_LONG).show();

        stref =FirebaseStorage.getInstance().getReference();
//        dbref=FirebaseDatabase.getInstance().getReference().child("uploadaudio");

        Uri uri = Uri.fromFile(new File(getRecordingFilePath()));
        System.out.println("###########"+uri.getLastPathSegment());
        StorageReference filepath = stref.child("uploadaudio").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               System.out.println("Upload successful ") ;
            }
        });
        String url="http://192.168.1.101:8080/";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        generatedpres.setText(response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("String", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("audiobits","92");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        int socketTimeout = 300000;//30 seconds - change to what you want
RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
        queue.add(postRequest);
    }

    public void btnPlayPressed(View v) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this,"Recording Playing!",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkmic() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
            return true;

        else {
            return false;
        }
    }

    private void getmicpermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDierctory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDierctory, "testRecordingFile"+".mp3");
        return file.getPath();
    }
}