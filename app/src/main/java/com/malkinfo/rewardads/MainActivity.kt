package com.malkinfo.rewardads

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : AppCompatActivity() {

    private var mRewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"
    private lateinit var root:LinearLayout
    private lateinit var claimBtn:ImageView
    private lateinit var rewards:TextView
    /*this you reward Ads ID*/
    private val AD_UINT_ID = "ca-app-pub-3940256099942544/5224354917"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById(R.id.root)
        claimBtn = findViewById(R.id.claimBtn)
        rewards = findViewById(R.id.rewards)
        MobileAds.initialize(this@MainActivity){}
        /*first here we load the ads*/
        loadRewardAd()
        /*when click the btn here show the ads*/
        claimBtn.setOnClickListener {
            showRewardAd()
        }


    }

    private fun showRewardAd() {

        if (mRewardedAd != null) {
            mRewardedAd?.show(this, OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    var rewardType = rewardItem.type
                    root.setBackgroundColor(Color.BLACK)
                    claimBtn.visibility = View.GONE

                    showRewardDialog()

                    Log.d(TAG, "User earned the reward.")
                }
            })
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }

    private fun loadRewardAd() {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(this,AD_UINT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mRewardedAd = null
                rewardCall()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd

            }
        })

    }
    private  fun rewardCall(){
        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
                loadRewardAd()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

    fun showRewardDialog() {
        val view = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.reward_dialog,null)
        val rewardget  = view.findViewById<ImageView>(R.id.rewardget)

        val rewardDialog = AlertDialog.Builder(this@MainActivity)
        rewardDialog.setView(view)
        rewardDialog.setCancelable(false)
        val getRewardDialog = rewardDialog.create()
        rewardget.setOnClickListener {
            root.setBackgroundColor(Color.WHITE)
            rewards.visibility = View.VISIBLE
            rewards.text = "You have win Reward Coin : 550"
            getRewardDialog.dismiss()
        }
        getRewardDialog.show()
    }
}