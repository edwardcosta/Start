package com.android.usuario.start.Util;

import com.android.usuario.start.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eduar on 04/05/2017.
 */

public class Singleton {
    public static final String DATABASE_USER_REFERENCE = "user";
    public static final String DATABASE_USERS_REFERENCE = "users";

    public static final String DATABASE_PROJECT_REFERENCE = "project";
    public static final String DATABASE_PROJECTS_REFERENCE = "projects";

    private static final List<String> PROJECT_DIFFICULTY = Arrays.asList("Fácil","Médio","Difícil");

    private static final List<String> PROFILE_TYPES = Arrays.asList("","Hacker","Hipster","Hustler");
    private static final List<Integer> IMAGES_PROFILE = Arrays.asList(
            R.drawable.img_profile_placeholder,R.drawable.img_profile_placeholder,
            R.drawable.img_profile_placeholder,R.drawable.img_profile_placeholder);

    public static String getStringProfileType(int profile){
        return PROFILE_TYPES.get(profile);
    }

    public static int getImageProfileType(int profile){
        return IMAGES_PROFILE.get(profile);
    }

    public static String getStringProjectDifficulty(int projectDifficulty){
        return PROJECT_DIFFICULTY.get(projectDifficulty);
    }

}
