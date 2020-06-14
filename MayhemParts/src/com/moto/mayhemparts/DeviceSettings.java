/*
* Copyright (C) 2020 ronaxdevil <pratabidya.007@gmail.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.com/licenses/>.
*
*/

package com.moto.mayhemparts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import com.moto.mayhemparts.preferences.CustomSeekBarPreference;
import com.moto.mayhemparts.preferences.SecureSettingListPreference;
import com.moto.mayhemparts.preferences.SecureSettingSwitchPreference;
import com.moto.mayhemparts.preferences.LedBlinkPreference;
import com.moto.mayhemparts.preferences.VibratorStrengthPreference;
import com.moto.mayhemparts.preferences.YellowFlashPreference;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String PREF_BACKLIGHT_DIMMER = "backlight_dimmer";
    public static final String BACKLIGHT_DIMMER_PATH = "/sys/module/mdss_fb/parameters/backlight_dimmer";

    public static final String KEY_VIBSTRENGTH = "vib_strength";

    public static final String PREF_SPECTRUM = "spectrum";
    public static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";

    public static final String PREF_HEADPHONE_GAIN = "headphone_gain";
    public static final String HEADPHONE_GAIN_PATH = "/sys/kernel/sound_control/headphone_gain";
    public static final String PREF_MICROPHONE_GAIN = "microphone_gain";
    public static final String MICROPHONE_GAIN_PATH = "/sys/kernel/sound_control/mic_gain";
    public static final String PREF_SPEAKER_GAIN = "speaker_gain";
    public static final String SPEAKER_GAIN_PATH = "/sys/kernel/sound_control/speaker_gain";
    public static final String CATEGORY_FASTCHARGE = "usb_fastcharge";
    public static final String PREF_USB_FASTCHARGE = "fastcharge";
    public static final String USB_FASTCHARGE_PATH = "/sys/kernel/fast_charge/force_fast_charge";
    public static final String PREF_KEY_FPS_INFO = "fps_info";

    public static final String PREF_MSM_TOUCHBOOST = "touchboost";
    public static final String MSM_TOUCHBOOST_PATH = "/sys/module/msm_performance/parameters/touchboost";

    public static final String PREF_GPUBOOST = "gpuboost";
    public static final String GPUBOOST_SYSTEM_PROPERTY = "persist.gpuboost.profile";
    public static final String PREF_CPUBOOST = "cpuboost";
    public static final String CPUBOOST_SYSTEM_PROPERTY = "persist.cpuboost.profile";

    final static String PREF_TORCH_BRIGHTNESS = "torch_brightness";
    public static final String TORCH_1_BRIGHTNESS_PATH = "/sys/devices/soc/qpnp-flash-led-23" +
            "/driver/qpnp-flash-led-23/leds/led:torch_0/max_brightness";
    public static final String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc/qpnp-flash-led-23" +
            "/driver/qpnp-flash-led-23/leds/led:torch_1/max_brightness";

    public static final String PREF_CHARGING_LED = "charging_led";
    public static final String CHARGING_LED_PATH = "/sys/devices/soc/leds-atc-20" +
            "/driver/leds-atc-20/leds/charging/max_brightness";
    public static final String KEY_FLASH = "yellow_flash";

    public static final String HIGH_PERF_AUDIO = "highperfaudio";
    public static final String HIGH_AUDIO_PATH = "/sys/module/snd_soc_wcd9330/parameters/high_perf_mode";

    public static final String PERF_MSM_THERMAL = "msmthermal";
    public static final String MSM_THERMAL_PATH = "/sys/module/msm_thermal/parameters/enabled";
    public static final String PERF_CORE_CONTROL = "corecontrol";
    public static final String CORE_CONTROL_PATH = "/sys/module/msm_thermal/core_control/enabled";
    public static final String PERF_VDD_RESTRICTION = "vddrestrict";
    public static final String VDD_RESTRICTION_PATH = "/sys/module/msm_thermal/vdd_restriction/enabled";
    public static final String PREF_CPUCORE = "cpucore";
    public static final String CPUCORE_SYSTEM_PROPERTY = "persist.cpucore.profile";

    private VibratorStrengthPreference mVibratorStrength;
    private SecureSettingListPreference mSPECTRUM;
    private CustomSeekBarPreference mHeadphoneGain;
    private CustomSeekBarPreference mMicrophoneGain;
    private CustomSeekBarPreference mSpeakerGain;
    private SecureSettingSwitchPreference mFastcharge;
    private SecureSettingSwitchPreference mBacklightDimmer;
    private SecureSettingSwitchPreference mTouchboost;
    private SecureSettingListPreference mGPUBOOST;
    private SecureSettingListPreference mCPUBOOST;
    private CustomSeekBarPreference mTorchBrightness;
    private LedBlinkPreference mLedBlink;
    private YellowFlashPreference mYellowFlash;
    private SecureSettingSwitchPreference mHighAudio;
    private SecureSettingSwitchPreference mMsmThermal;
    private SecureSettingSwitchPreference mCoreControl;
    private SecureSettingSwitchPreference mVddRestrict;
    private SecureSettingListPreference mCPUCORE;
    private static Context mContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_mayhem_parts, rootKey);
        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        String device = FileUtils.getStringProp("ro.build.product", "unknown");

        mSPECTRUM = (SecureSettingListPreference) findPreference(PREF_SPECTRUM);
        mSPECTRUM.setValue(FileUtils.getStringProp(SPECTRUM_SYSTEM_PROPERTY, "0"));
        mSPECTRUM.setSummary(mSPECTRUM.getEntry());
        mSPECTRUM.setOnPreferenceChangeListener(this);

        if (FileUtils.fileWritable(BACKLIGHT_DIMMER_PATH)) {
            mBacklightDimmer = (SecureSettingSwitchPreference) findPreference(PREF_BACKLIGHT_DIMMER);
            mBacklightDimmer.setEnabled(BacklightDimmer.isSupported());
            mBacklightDimmer.setChecked(BacklightDimmer.isCurrentlyEnabled(this.getContext()));
            mBacklightDimmer.setOnPreferenceChangeListener(new BacklightDimmer(getContext()));
        } else {
            getPreferenceScreen().removePreference(findPreference(PREF_BACKLIGHT_DIMMER));
        }

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        if (FileUtils.fileWritable(HIGH_AUDIO_PATH)) {
            mHighAudio = (SecureSettingSwitchPreference) findPreference(HIGH_PERF_AUDIO);
            mHighAudio.setChecked(FileUtils.getFileValueAsBoolean(HIGH_AUDIO_PATH, true));
            mHighAudio.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(HIGH_PERF_AUDIO));
        }

        mHeadphoneGain = (CustomSeekBarPreference) findPreference(PREF_HEADPHONE_GAIN);
        mHeadphoneGain.setOnPreferenceChangeListener(this);

        mMicrophoneGain = (CustomSeekBarPreference) findPreference(PREF_MICROPHONE_GAIN);
        mMicrophoneGain.setOnPreferenceChangeListener(this);

        mSpeakerGain = (CustomSeekBarPreference) findPreference(PREF_SPEAKER_GAIN);
        mSpeakerGain.setOnPreferenceChangeListener(this);

        if (FileUtils.fileWritable(USB_FASTCHARGE_PATH)) {
            mFastcharge = (SecureSettingSwitchPreference) findPreference(PREF_USB_FASTCHARGE);
            mFastcharge.setEnabled(Fastcharge.isSupported());
            mFastcharge.setChecked(Fastcharge.isCurrentlyEnabled(this.getContext()));
            mFastcharge.setOnPreferenceChangeListener(new Fastcharge(getContext()));
        } else {
            getPreferenceScreen().removePreference(findPreference(CATEGORY_FASTCHARGE));
        }

        if (FileUtils.fileWritable(MSM_TOUCHBOOST_PATH)) {
            mTouchboost = (SecureSettingSwitchPreference) findPreference(PREF_MSM_TOUCHBOOST);
            mTouchboost.setEnabled(Touchboost.isSupported());
            mTouchboost.setChecked(Touchboost.isCurrentlyEnabled(this.getContext()));
            mTouchboost.setOnPreferenceChangeListener(new Touchboost(getContext()));
        } else {
            getPreferenceScreen().removePreference(findPreference(PREF_MSM_TOUCHBOOST));
        }

        mGPUBOOST = (SecureSettingListPreference) findPreference(PREF_GPUBOOST);
        mGPUBOOST.setValue(FileUtils.getStringProp(GPUBOOST_SYSTEM_PROPERTY, "0"));
        mGPUBOOST.setSummary(mGPUBOOST.getEntry());
        mGPUBOOST.setOnPreferenceChangeListener(this);

        mCPUBOOST = (SecureSettingListPreference) findPreference(PREF_CPUBOOST);
        mCPUBOOST.setValue(FileUtils.getStringProp(CPUBOOST_SYSTEM_PROPERTY, "0"));
        mCPUBOOST.setSummary(mCPUBOOST.getEntry());
        mCPUBOOST.setOnPreferenceChangeListener(this);

        mTorchBrightness = (CustomSeekBarPreference) findPreference(PREF_TORCH_BRIGHTNESS);
        mTorchBrightness.setEnabled(FileUtils.fileWritable(TORCH_1_BRIGHTNESS_PATH) &&
                FileUtils.fileWritable(TORCH_2_BRIGHTNESS_PATH));
        mTorchBrightness.setOnPreferenceChangeListener(this);

        mLedBlink = (LedBlinkPreference) findPreference(PREF_CHARGING_LED);
        if (mLedBlink != null) {
            mLedBlink.setEnabled(LedBlinkPreference.isSupported());
        }

        mYellowFlash = (YellowFlashPreference) findPreference(KEY_FLASH);
        if (mYellowFlash != null) {
            mYellowFlash.setEnabled(YellowFlashPreference.isSupported());
        }

        if (FileUtils.fileWritable(MSM_THERMAL_PATH)) {
            mMsmThermal = (SecureSettingSwitchPreference) findPreference(PERF_MSM_THERMAL);
            mMsmThermal.setChecked(FileUtils.getFilesValueAsBoolean(MSM_THERMAL_PATH, true));
            mMsmThermal.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(PERF_MSM_THERMAL));
        }

        if (FileUtils.fileWritable(CORE_CONTROL_PATH)) {
            mCoreControl = (SecureSettingSwitchPreference) findPreference(PERF_CORE_CONTROL);
            mCoreControl.setChecked(FileUtils.getFileValueAsBoolean(CORE_CONTROL_PATH, true));
            mCoreControl.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(PERF_CORE_CONTROL));
        }

        if (FileUtils.fileWritable(VDD_RESTRICTION_PATH)) {
            mVddRestrict = (SecureSettingSwitchPreference) findPreference(PERF_VDD_RESTRICTION);
            mVddRestrict.setChecked(FileUtils.getFileValueAsBoolean(VDD_RESTRICTION_PATH, true));
            mVddRestrict.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(PERF_VDD_RESTRICTION));
        }

        mCPUCORE = (SecureSettingListPreference) findPreference(PREF_CPUCORE);
        mCPUCORE.setValue(FileUtils.getStringProp(CPUCORE_SYSTEM_PROPERTY, "0"));
        mCPUCORE.setSummary(mCPUCORE.getEntry());
        mCPUCORE.setOnPreferenceChangeListener(this);

        SwitchPreference fpsInfo = (SwitchPreference) findPreference(PREF_KEY_FPS_INFO);
        fpsInfo.setChecked(prefs.getBoolean(PREF_KEY_FPS_INFO, false));
        fpsInfo.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {
            case PREF_SPECTRUM:
                mSPECTRUM.setValue((String) value);
                mSPECTRUM.setSummary(mSPECTRUM.getEntry());
                FileUtils.setStringProp(SPECTRUM_SYSTEM_PROPERTY, (String) value);
                break;

            case HIGH_PERF_AUDIO:
                FileUtils.setValue(HIGH_AUDIO_PATH, (boolean) value);
                break;

            case PREF_HEADPHONE_GAIN:
                FileUtils.setValue(HEADPHONE_GAIN_PATH, value + " " + value);
                break;

            case PREF_MICROPHONE_GAIN:
                FileUtils.setValue(MICROPHONE_GAIN_PATH, (int) value);
                break;

            case PREF_SPEAKER_GAIN:
                FileUtils.setValue(SPEAKER_GAIN_PATH, (int) value);
                break;

            case PREF_GPUBOOST:
                mGPUBOOST.setValue((String) value);
                mGPUBOOST.setSummary(mGPUBOOST.getEntry());
                FileUtils.setStringProp(GPUBOOST_SYSTEM_PROPERTY, (String) value);
                break;

            case PREF_CPUBOOST:
                mCPUBOOST.setValue((String) value);
                mCPUBOOST.setSummary(mCPUBOOST.getEntry());
                FileUtils.setStringProp(CPUBOOST_SYSTEM_PROPERTY, (String) value);
                break;

            case PREF_TORCH_BRIGHTNESS:
                FileUtils.setValue(TORCH_1_BRIGHTNESS_PATH, (int) value);
                FileUtils.setValue(TORCH_2_BRIGHTNESS_PATH, (int) value);
                break;

            case PERF_MSM_THERMAL:
                FileUtils.setValue(MSM_THERMAL_PATH, (boolean) value);
                break;

            case PERF_CORE_CONTROL:
                FileUtils.setValue(CORE_CONTROL_PATH, (boolean) value);
                break;

            case PERF_VDD_RESTRICTION:
                FileUtils.setValue(VDD_RESTRICTION_PATH, (boolean) value);
                break;

            case PREF_CPUCORE:
                mCPUCORE.setValue((String) value);
                mCPUCORE.setSummary(mCPUCORE.getEntry());
                FileUtils.setStringProp(CPUCORE_SYSTEM_PROPERTY, (String) value);
                break;

            case PREF_KEY_FPS_INFO:
                boolean enabled = (Boolean) value;
                Intent fpsinfo = new Intent(this.getContext(), FPSInfoService.class);
                if (enabled) {
                    this.getContext().startService(fpsinfo);
                } else {
                    this.getContext().stopService(fpsinfo);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isAppNotInstalled(String uri) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
