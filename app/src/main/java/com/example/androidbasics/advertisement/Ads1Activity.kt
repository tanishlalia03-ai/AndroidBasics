package com.example.androidbasics.advertisement

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidbasics.databinding.ActivityAds1Binding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class Ads1Activity : AppCompatActivity() {

    // 1. Fixed naming and initialization
    private lateinit var binding: ActivityAds1Binding
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. Properly initialize View Binding
        binding = ActivityAds1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MobileAds.initialize(this) {}

        // loading ads
        loadBannerAd()
        loadInterstitialAd()
        loadRewardedAd()

        // 3. Ensure buttons exist in your XML or add them
        binding.btn1.setOnClickListener {
            showRewardedAdAndThen {
                // do your task here (e.g., give coins)
            }
        }

        binding.btn2.setOnClickListener {
            showInterstitialAndThen {
                // do your task here (e.g., move to next level)
            }
        }
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }
            })
    }

    private fun showInterstitialAndThen(action: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    loadInterstitialAd()
                    action()
                }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    mInterstitialAd = null
                    action()
                }
            }
            mInterstitialAd?.show(this)
        } else {
            action()
        }
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }
            })
    }

    private fun showRewardedAdAndThen(action: () -> Unit) {
        val ad = mRewardedAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mRewardedAd = null
                    loadRewardedAd()
                }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    mRewardedAd = null
                    action()
                }
            }
            ad.show(this) { rewardItem ->
                Toast.makeText(this, "Reward Earned!", Toast.LENGTH_SHORT).show()
                action()
            }
        } else {
            Toast.makeText(this, "Ad not ready yet", Toast.LENGTH_SHORT).show()
            action()
        }
    }

    override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }
}