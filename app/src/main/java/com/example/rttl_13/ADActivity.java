package com.example.rttl_13;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.util.Map;

public class ADActivity extends AppCompatActivity {
    private AdView adView;

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5354046379";
    private static final String TAG = "ADActivity";

    private CountDownTimer countDownTimer;

    private Button retryButton;
    private long timeRemaining;

    private RewardedInterstitialAd rewardedInterstitialAd;
    boolean isLoadingAds;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ad);

            MobileAds.initialize(this, initializationStatus -> {
                loadRewardedInterstitialAd();
            });


            adView = findViewById(R.id.adBanner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            loadRewardedInterstitialAd();
            retryButton = findViewById(R.id.button2);
            retryButton.setOnClickListener(
                    view -> {
                        createTimer(2);
                    });

        }

    private void loadRewardedInterstitialAd() {
        if (rewardedInterstitialAd == null) {
            isLoadingAds = true;

            AdRequest adRequest = new AdRequest.Builder().build();
            // Use the test ad unit ID to load an ad.
            RewardedInterstitialAd.load(
                    ADActivity.this,
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(RewardedInterstitialAd ad) {
                            Log.d(TAG, "onAdLoaded");

                            rewardedInterstitialAd = ad;
                            isLoadingAds = false;
                            Toast.makeText(ADActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                            // Handle the error.
                            rewardedInterstitialAd = null;
                            isLoadingAds = false;
                            Toast.makeText(ADActivity.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void createTimer(long time) {
        final TextView textView = findViewById(R.id.timer);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer =
                new CountDownTimer(time * 1000, 50) {
                    @Override
                    public void onTick(long millisUnitFinished) {
                        timeRemaining = ((millisUnitFinished / 1000) + 1);
                        textView.setText("seconds remaining: " + timeRemaining);
                    }

                    @Override
                    public void onFinish() {
                        textView.setText("You Lose!");
                        //addCoins(GAME_OVER_REWARD);
                        retryButton.setVisibility(View.VISIBLE);
                        //gameOver = true;

                        if (rewardedInterstitialAd == null) {
                            Log.d(TAG, "The rewarded interstitial ad is not ready.");
                            return;
                        }

                        RewardItem rewardItem = rewardedInterstitialAd.getRewardItem();
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();

                        Log.d(TAG, "The rewarded interstitial ad is ready.");
                        introduceVideoAd(rewardAmount, rewardType);
                    }
                };
        countDownTimer.start();
    }

    private void introduceVideoAd(int rewardAmount, String rewardType) {
        AdDialogFragment dialog = AdDialogFragment.newInstance(rewardAmount, rewardType);
        dialog.setAdDialogInteractionListener(
                new AdDialogFragment.AdDialogInteractionListener() {
                    @Override
                    public void onShowAd() {
                        Log.d(TAG, "The rewarded interstitial ad is starting.");

                        showRewardedVideo();
                    }

                    @Override
                    public void onCancelAd() {
                        Log.d(TAG, "The rewarded interstitial ad was skipped before it starts.");
                    }
                });
        dialog.show(getSupportFragmentManager(), "AdDialogFragment");
    }
    private void showRewardedVideo() {

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

                        Toast.makeText(ADActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    /** Called when the ad failed to show full screen content. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedInterstitialAd = null;
                        loadRewardedInterstitialAd();

                        Toast.makeText(
                                        ADActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {//當廣告影片關閉
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedInterstitialAd = null;
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                        Toast.makeText(ADActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                        // Preload the next rewarded interstitial ad.
                        loadRewardedInterstitialAd();
                    }
                });

        Activity activityContext = ADActivity.this;
        rewardedInterstitialAd.show(
            activityContext,
            new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    //addCoins(rewardItem.getAmount());
                }
            });
    }

    }
