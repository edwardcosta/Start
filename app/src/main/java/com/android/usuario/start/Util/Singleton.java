package com.android.usuario.start.Util;

/**
 * Created by eduar on 04/05/2017.
 */

public class Singleton {
    private static Singleton instance;

    public static final String DATABASE_USERS_REFERENCE = "users";

    public static final String DATABASE_PROJECT_REFERENCE = "project";
    public static final String DATABASE_PROJECTS_REFERENCE = "projects";

    public static synchronized Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}
