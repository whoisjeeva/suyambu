package io.gopiper.piper

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object Ads {

    private const val BANNER_1 = "ca-app-pub-1517962596069817/6601227862"
    private const val INTERSTITIAL_1 = "ca-app-pub-1517962596069817/9835278755"
    private const val REWARD_1 = "ca-app-pub-3940256099942544/5224354917"


    private const val TEST_BANNER_1 = "ca-app-pub-3940256099942544/6300978111"
    private const val TEST_INTERSTITIAL_1 = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_REWARD_1 = "ca-app-pub-3940256099942544/5224354917"


    val bannerId: String
        get() = if (Config.DEBUG_MODE) "TEST_BANNER_1" else BANNER_1

    private val interstitialId: String
        get() = if (Config.DEBUG_MODE) "TEST_INTERSTITIAL_1" else INTERSTITIAL_1


    private var interstitialAd: InterstitialAd? = null
    fun initInterstitial() {
        interstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("Ads.kt", "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d("Ads.kt", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("Ads.kt", "Ad showed fullscreen content.")
                interstitialAd = null
            }
        }
    }

    fun loadInterstitial(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, interstitialId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("Ads.kt", adError.message)
                interstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("Ads.kt", "Ad was loaded.")
                this@Ads.interstitialAd = interstitialAd
            }
        })
    }

    fun showInterstitial(activity: Activity) {
        interstitialAd?.show(activity)
    }
}