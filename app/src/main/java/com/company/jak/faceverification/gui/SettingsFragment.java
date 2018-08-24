package com.company.jak.faceverification.gui;

/**
 * Created by jake on 19/08/18.
 */

import com.company.jak.faceverification.R;
import com.neurotec.face.verification.NFaceVerification;
import com.neurotec.face.verification.NFaceVerificationLivenessMode;
import com.neurotec.lang.NCore;
import com.company.jak.faceverification.NFV;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_PREF_LIVENESS_TH = "key_pref_liveness_th";
    public static final String KEY_PREF_QUALITY_TH = "key_pref_quality_th";
    public static final String KEY_PREF_MATCHING_TH = "key_pref_matching_th";
    public static final String KEY_PREF_LIVENESS_MODE = "key_pref_liveness_mode";

    public static final int PREF_LIVENESS_TH_DEFAULT_VALUE = 50;
    public static final int PREF_QUALITY_TH_DEFAULT_VALUE = 50;
    public static final int PREF_MATCHING_TH_DEFAULT_VALUE = 48;
    public static final String PREF_LIVENESS_MODE_DEFAULT_VALUE = "NONE";

    private SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals(KEY_PREF_LIVENESS_MODE)) {
                        String value = sharedPreferences.getString(key, PREF_LIVENESS_MODE_DEFAULT_VALUE);
                        NFaceVerificationLivenessMode livenessMode = NFaceVerificationLivenessMode.valueOf(value);
                        NFV.getInstance().setLivenessMode(livenessMode);
                    } else if (key.equals(KEY_PREF_LIVENESS_TH)) {
                        byte th = (byte)sharedPreferences.getInt(key, PREF_LIVENESS_TH_DEFAULT_VALUE);
                        NFV.getInstance().setLivenessThreshold(th);
                    } else if (key.equals(KEY_PREF_QUALITY_TH)) {
                        byte th = (byte)sharedPreferences.getInt(key, PREF_QUALITY_TH_DEFAULT_VALUE);
                        NFV.getInstance().setQualityThreshold(th);
                    } else if (key.equals(KEY_PREF_MATCHING_TH)) {
                        int th = sharedPreferences.getInt(key, PREF_MATCHING_TH_DEFAULT_VALUE);
                        NFV.getInstance().setMatchingThreshold(th);
                    }
                }
            };

    public static void loadSettings() {
        NFaceVerification instance = NFV.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NCore.getContext());

        String value = sharedPreferences.getString(KEY_PREF_LIVENESS_MODE, PREF_LIVENESS_MODE_DEFAULT_VALUE);
        NFaceVerificationLivenessMode livenessMode = NFaceVerificationLivenessMode.valueOf(value);
        instance.setLivenessMode(livenessMode);

        byte byteTh = (byte)sharedPreferences.getInt(KEY_PREF_LIVENESS_TH, PREF_LIVENESS_TH_DEFAULT_VALUE);
        instance.setLivenessThreshold(byteTh);

        byteTh = (byte)sharedPreferences.getInt(KEY_PREF_QUALITY_TH, PREF_QUALITY_TH_DEFAULT_VALUE);
        instance.setQualityThreshold(byteTh);

        int intTh = sharedPreferences.getInt(KEY_PREF_MATCHING_TH, PREF_MATCHING_TH_DEFAULT_VALUE);
        instance.setMatchingThreshold(intTh);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(listener);
    }
}