package com.android.usuario.start.RequestManager;

import com.android.usuario.start.Util.Singleton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by eduar on 16/05/2017.
 */

public class Storage {

    private static final String images = "images";
    private static final String videos = "videos";

    public static StorageReference getUserImagesReference(String userId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_USERS_REFERENCE).child(userId).child(images);
    }

    public static StorageReference getUserVideosReference(String userId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_USERS_REFERENCE).child(userId).child(videos);
    }

    public static StorageReference getProjectImagesReference(String projectId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_PROJECTS_REFERENCE).child(projectId).child(images);
    }

    public static StorageReference getProjectVideosReference(String projectId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_PROJECTS_REFERENCE).child(projectId).child(videos);
    }
}
