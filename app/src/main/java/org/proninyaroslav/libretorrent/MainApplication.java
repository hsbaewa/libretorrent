/*
 * Copyright (C) 2016-2018 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.proninyaroslav.libretorrent;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.EventBus;
import org.proninyaroslav.libretorrent.core.utils.Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainApplication extends MultiDexApplication
{
    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, "[admob_app_id]");
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);

        Utils.migrateTray2SharedPreferences(this);
        EventBus.builder().logNoSubscriberMessages(false).installDefaultEventBus();
    }

    public AdRequest getAdRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            try {
                String deviceId = getMD5(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                builder.addTestDevice(getMD5(deviceId));
            } catch (Exception ignore) {
                Log.d("a","a");
            }
        }
        return builder.build();
    }

    public String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(str.getBytes());

            String strMD5;
            for(strMD5 = (new BigInteger(1, messageDigest)).toString(16); strMD5.length() < 32; strMD5 = "0" + strMD5) {
            }

            return strMD5;
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            return "00000000000000000000000000000000";
        }
    }
}