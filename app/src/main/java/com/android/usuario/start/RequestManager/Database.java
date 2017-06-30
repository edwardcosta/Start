package com.android.usuario.start.RequestManager;

import com.android.usuario.start.Util.Singleton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eduar on 16/05/2017.
 */

public class Database {

    public static DatabaseReference getUsersReference(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Singleton.DATABASE_USER_REFERENCE).child(Singleton.DATABASE_USERS_REFERENCE);
        databaseReference.keepSynced(true);
        return databaseReference;
    }

    public static DatabaseReference getProjectsReference(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Singleton.DATABASE_PROJECT_REFERENCE).child(Singleton.DATABASE_PROJECTS_REFERENCE);
        databaseReference.keepSynced(true);
        return databaseReference;
    }
}
