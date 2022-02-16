//package com.example.MAILER;
//
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class CRUDops {
//
//    private  FirebaseDatabase dbinstance;
//    private DatabaseReference dbreference;
//
//    public CRUDops(){
//        dbinstance=FirebaseDatabase.getInstance();
//        dbreference=dbinstance.getReference(Credentials.class.getSimpleName());
//    }
//    public Task<Void> CreateRecord(Credentials c){
//        return dbreference.push().setValue(c);
//
//
//    }
//}
