package com.android.usuario.start.Screens.Auth;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Auth.Login.LoginFragmet;
import com.android.usuario.start.Screens.Auth.Login.WelcomeFragmet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import me.relex.circleindicator.CircleIndicator;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ViewPager viewPager;
    private LoginPagerAdapter myPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        viewPager = (ViewPager) findViewById(R.id.activity_login_pager);
        myPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.activity_login_indicator);
        indicator.setViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class LoginPagerAdapter extends FragmentStatePagerAdapter {

        private final int PAGE_COUNT = 2;

        private FragmentManager fragmentManager;

        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new WelcomeFragmet();
                case 1:
                    return new LoginFragmet();
            }

            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
