package com.example.MAILER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.File;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "done";
    //EditText usremail, passwrd ,repasswrd;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup=findViewById(R.id.signup);
        //usremail=findViewById(R.id.username);
        //passwrd=findViewById(R.id.pass);
        //repasswrd=findViewById(R.id.repass);
        //CRUDops crudmanage=new CRUDops();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
//*****************************email
            String username="heckermen2024@gmail.com";
//*****************************password
            String password="Heckermen@2024";
            String messagetosend="Hi hello";
            Properties props =new Properties();
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.port","587");
                Session session =Session.getInstance(props,new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username,password);
                    }
                });
                try {
                    Message message=new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
//****************************************recipient's mail
                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("rewa.wader20@vit.edu"));
                    message.setSubject("Hi Rewa");

//                    MimeMultipart multipart = new MimeMultipart();
//                    MimeBodyPart attachment = new MimeBodyPart();
//                    attachment.attachFile(new File("/app/src/main/res/raw/hehehe.txt"));
//
//
//                    MimeBodyPart messageBodyPart = new MimeBodyPart();
//                    messageBodyPart.setContent("Hello","text/html");
//
//
//                    multipart.addBodyPart(messageBodyPart);
//                    multipart.addBodyPart(attachment);

                    try {
                        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        String sFileName = "abcd.txt";
                        String sBody = "Hello";
                        File gpxfile = new File(root, sFileName);
                        FileWriter writer = new FileWriter(gpxfile);
                        writer.append(sBody);
                        writer.flush();
                        writer.close();
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // creates message part
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent("It Works", "text/html");

                    // creates multi-part
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    // adds attachments
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try {
                        attachPart.attachFile("F:/MAILER/app/src/main/res/assets/lmao.jpg");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    multipart.addBodyPart(attachPart);



                    message.setContent(multipart);
//                    BodyPart messageBodyPart = new MimeBodyPart();
//
//                    // Now set the actual message
//                    messageBodyPart.setText("This is message body");
//
//                    // Create a multipart message
//                    Multipart multipart = new MimeMultipart();
//                    multipart.addBodyPart(messageBodyPart);
//                    messageBodyPart = new MimeBodyPart();
//                    String filename = "hehehe.txt";
//                    DataSource source = new FileDataSource(filename);
//                    messageBodyPart.setDataHandler(new DataHandler(source));
//                    messageBodyPart.setFileName(filename);
//                    multipart.addBodyPart(messageBodyPart);
//
//                    // Send the complete message parts
//                    message.setContent(multipart);
                    Transport.send(message);
                }
                catch(Exception e){
                    System.out.println("here"+e);
                }
//                Log.d(TAG, "done ");
//                System.out.println("donee");
//                try{
//                    Properties properties = new Properties();
//                    properties.put("mail.smtp.auth", true);
//                    properties.put("mail.smtp.host", "smtp.gmail.com");
//                    properties.put("mail.smtp.port", 587);
//                    properties.put("mail.smtp.startlls.enable", true);
//                    properties.put("mail.transport.protocol", "smtp");
//
//
//
//                    Session session = Session.getInstance(properties,new Authenticator() {
//                        @Override
//                        protected PasswordAuthentication getPasswordAuthentication(){
//                            return new PasswordAuthentication("rewa.wader20@vit.edu", "Mama$_1234");
//
//                        }
//                    });
//                    Message message = new MimeMessage(session);
//                    message.setSubject("AutoMail");
//
//
//                    Address addressTo = new InternetAddress("rewa.wader20@vit.edu");
//                    message.setRecipient(Message.RecipientType.TO, addressTo);
//
//                    MimeMultipart multipart = new MimeMultipart();
//
////                    MimeBodyPart attachment = new MimeBodyPart();
////
//
//                    MimeBodyPart messageBodyPart = new MimeBodyPart();
//                    messageBodyPart.setContent("Hello","text/html");
//
//
//                    multipart.addBodyPart(messageBodyPart);
////                    multipart.addBodyPart(attachment);
//
//                    message.setContent(multipart);
//
//                    Transport.send(message);
//                }
//                     catch (Exception e){
//
//                     }

                }

//            String usrmail=((EditText)usremail).getText().toString();
//            String password=((EditText)passwrd).getText().toString();
//            String repassword=((EditText)repasswrd).getText().toString();
//
//                if(!usrmail.contains("@gmail.com")){
//                Toast.makeText(MainActivity.this,"Please provide a valid gmail",Toast.LENGTH_LONG).show();
//                }
//                else if(password.equals("") || repassword.equals("") || password==null || repassword==null){
//                    Toast.makeText(MainActivity.this,"Please enter Password and Repassword",Toast.LENGTH_LONG).show();
//                }
//                else if(password.length()<8){
//                    Toast.makeText(MainActivity.this,"Password should be of atleast 8 characters long",Toast.LENGTH_LONG).show();
//                }
//                else if(!password.equals(repassword)){
//                    Toast.makeText(MainActivity.this,"Password and Retyped Password don't match",Toast.LENGTH_LONG).show();
//                }
//                else if(!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",password)){
//                    Toast.makeText(MainActivity.this,"Password must contain atleast one special character and alphabet",Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Credentials doctor = new Credentials(usrmail,password);
//                    crudmanage.CreateRecord(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(MainActivity.this,"Succesfully Signed Up",Toast.LENGTH_LONG).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(MainActivity.this,"Couldn't Sign Up."+e.getMessage()+". Please try again",Toast.LENGTH_LONG).show();
//                                }
//                    });
//
//                }
//            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


}