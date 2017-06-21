package com.android.usuario.start.RequestManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.usuario.start.Util.Singleton;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by eduar on 16/05/2017.
 */

public class Storage {

    private static final String images = "images";
    private static final String videos = "videos";

    public static StorageReference getUserImagesReference(String userId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_USERS_REFERENCE).child(images).child(userId);
    }

    public static StorageReference getUserVideosReference(String userId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_USERS_REFERENCE).child(videos).child(userId);
    }

    public static StorageReference getProjectImagesReference(String projectId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_PROJECTS_REFERENCE).child(images).child(projectId);
    }

    public static StorageReference getProjectVideosReference(String projectId){
        return FirebaseStorage.getInstance().getReference()
                .child(Singleton.DATABASE_PROJECTS_REFERENCE).child(videos).child(projectId);
    }

    public static void uploadFromImageView(ImageView imageView, StorageReference storageReference,
                                           final UploadImageCallBack uploadImageCallBack){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                uploadImageCallBack.uploadImageCallBack(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                uploadImageCallBack.uploadImageCallBack(downloadUrl);
            }
        });

    }

    public interface UploadImageCallBack{
        void uploadImageCallBack(Uri uri);
    }
}
