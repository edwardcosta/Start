package com.android.usuario.start;

/**
 * Created by eduar on 04/05/2017.
 */

public class Singleton {
    private static Singleton instance;

    public static synchronized Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}
