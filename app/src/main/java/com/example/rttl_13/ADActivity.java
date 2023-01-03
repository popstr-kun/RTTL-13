package com.example.rttl_13;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;



public class ADActivity extends AppCompatActivity {
    //private AdView adView;

    private static final String AD_UNIT_ID = "ca-app-pub-9243343214101739/7625542297";
    private static final String TAG = "ADActivity";


    //private Button retryButton;

    protected RewardedInterstitialAd rewardedInterstitialAd;
    boolean isLoadingAds;

//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.ad);
//
//
//            MobileAds.initialize(this, initializationStatus -> {
//                loadRewardedInterstitialAd();
//            });
//
//            adView = findViewById(R.id.adBanner);///橫幅廣告
//            AdRequest adRequest = new AdRequest.Builder().build();
//            adView.loadAd(adRequest);
//
//
//            loadRewardedInterstitialAd();
//            retryButton = findViewById(R.id.adbutton);
//            retryButton.setOnClickListener(view -> {
//                        showRewardedVideo();//AD開始
//                    });
//
//        }

    ADActivity (Context context){
        MobileAds.initialize(context, initializationStatus -> {
            loadRewardedInterstitialAd(context);
        });
    }

    protected void loadRewardedInterstitialAd(Context context) {
        if (rewardedInterstitialAd == null) {
            isLoadingAds = true;

            AdRequest adRequest = new AdRequest.Builder().build();
            // Use the test ad unit ID to load an ad.
            RewardedInterstitialAd.load(
                    context,
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(RewardedInterstitialAd ad) {
                            //Log.d(TAG, "onAdLoaded");

                            rewardedInterstitialAd = ad;
                            isLoadingAds = false;
                            //Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            //Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                            // Handle the error.
                            rewardedInterstitialAd = null;
                            isLoadingAds = false;
                            //Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    protected void showRewardedVideo(Context context, Activity activity) {

        if (rewardedInterstitialAd == null) {
            Log.d(TAG, "The rewarded interstitial ad wasn't ready yet.");
            return;
        }

        rewardedInterstitialAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when ad showed the full screen content. */
                    @Override
                    public void onAdShowedFullScreenContent() {//當廣告影片開始
                        Log.d(TAG, "onAdShowedFullScreenContent");

                        Toast.makeText(context, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    /** Called when the ad failed to show full screen content. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedInterstitialAd = null;
                        loadRewardedInterstitialAd(context);

                        Toast.makeText(
                                        context, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {//當廣告影片關閉
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedInterstitialAd = null;
                        //Log.d(TAG, "onAdDismissedFullScreenContent");
                        //Toast.makeText(context, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                               // .show();
                        // Preload the next rewarded interstitial ad.
                        loadRewardedInterstitialAd(context);
                    }
                });


        rewardedInterstitialAd.show(
                activity,
                rewardItem -> {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");

                });
    }



    }


