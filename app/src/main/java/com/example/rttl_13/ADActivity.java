package com.example.rttl_13;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ADActivity extends AppCompatActivity {

        private AdView adView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ad);

            MobileAds.initialize(this, initializationStatus -> {
            });


            adView = findViewById(R.id.adBanner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);/*
            mdView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    adView.loadAd(adRequest);
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            });*/
        }

    }
