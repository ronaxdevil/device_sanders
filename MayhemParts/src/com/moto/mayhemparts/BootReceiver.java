/*
 * Copyright (c) 2015 The CyanogenMod Project
 * Copyright (c) 2017 The LineageOS Project
 * Copyright (c) 2020 ronaxdevil <pratabidya.007@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moto.mayhemparts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.provider.Settings;
import com.moto.mayhemparts.preferences.LedBlinkPreference;
import com.moto.mayhemparts.preferences.VibratorStrengthPreference;
import com.moto.mayhemparts.preferences.YellowFlashPreference;

public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        FileUtils.setValue(DeviceSettings.BACKLIGHT_DIMMER_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_BACKLIGHT_DIMMER, 0));

        LedBlinkPreference.restore(context);
        VibratorStrengthPreference.restore(context);
        YellowFlashPreference.restore(context);

        int gain = Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_HEADPHONE_GAIN, 4);
        FileUtils.setValue(DeviceSettings.HEADPHONE_GAIN_PATH, gain + " " + gain);
        FileUtils.setValue(DeviceSettings.MICROPHONE_GAIN_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_MICROPHONE_GAIN, 0));
        FileUtils.setValue(DeviceSettings.SPEAKER_GAIN_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_SPEAKER_GAIN, 0));
        FileUtils.setValue(DeviceSettings.HIGH_AUDIO_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.HIGH_PERF_AUDIO, 0));
        FileUtils.setValue(DeviceSettings.USB_FASTCHARGE_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_USB_FASTCHARGE, 0));
        FileUtils.setValue(DeviceSettings.MSM_TOUCHBOOST_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_MSM_TOUCHBOOST, 0));
        FileUtils.setValue(DeviceSettings.TORCH_1_BRIGHTNESS_PATH,
                Settings.Secure.getInt(context.getContentResolver(),
                        DeviceSettings.PREF_TORCH_BRIGHTNESS, 150));
        FileUtils.setValue(DeviceSettings.TORCH_2_BRIGHTNESS_PATH,
                Settings.Secure.getInt(context.getContentResolver(),
                        DeviceSettings.PREF_TORCH_BRIGHTNESS, 150));
        FileUtils.setValue(DeviceSettings.MSM_THERMAL_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PERF_MSM_THERMAL, 0));
        FileUtils.setValue(DeviceSettings.CORE_CONTROL_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PERF_CORE_CONTROL, 0));
        FileUtils.setValue(DeviceSettings.VDD_RESTRICTION_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PERF_VDD_RESTRICTION, 0));
        boolean enabled = sharedPrefs.getBoolean(DeviceSettings.PREF_KEY_FPS_INFO, false);
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
        }
    }
}
