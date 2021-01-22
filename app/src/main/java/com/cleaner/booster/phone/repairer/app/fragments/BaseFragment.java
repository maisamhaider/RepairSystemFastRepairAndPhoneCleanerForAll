package com.cleaner.booster.phone.repairer.app.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cleaner.booster.phone.repairer.app.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class BaseFragment extends Fragment {
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadInterstial();
    }

    public void loadInterstial() {
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstial));
        try {
            reqNewInterstitial();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reqNewInterstitial() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public void sNewActivityAds(Activity activity) {
        try {


            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                startActivity(new Intent(getContext(), activity.getClass()));
            }
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    startActivity(new Intent(getContext(), activity.getClass()));
                    reqNewInterstitial();
                }
            });
        } catch (Exception e) {
            startActivity(new Intent(getContext(), activity.getClass()));

        }
    }

    @SuppressLint("ResourceType")
    public void adView(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                adView.setVisibility(View.GONE);
            }

        });
    }

}
