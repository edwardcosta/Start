package com.android.usuario.start.RequestManager;

import com.android.usuario.start.Util.Singleton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eduar on 16/05/2017.
 */

public class Database {

    public static DatabaseReference getUsersReference(){
        return FirebaseDatabase.getInstance().getReference().child(Singleton.DATABASE_USERS_REFERENCE);
    }

    public static DatabaseReference getProjectsReference(){
        return FirebaseDatabase.getInstance().getReference()
                .child(Singleton.DATABASE_PROJECT_REFERENCE).child(Singleton.DATABASE_PROJECTS_REFERENCE);
    }
}
