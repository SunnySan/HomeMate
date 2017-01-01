package com.sunny.homemate;

/**
 * Created by sunny on 2017/1/1.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefActivity extends PreferenceActivity {

    private SharedPreferences sharedPreferences;
    private Preference defaultDeviceid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 加入欄位變數宣告
        super.onCreate(savedInstanceState);
        // 指定使用的設定畫面配置資源
        // 這行敘述從API Level 11開始會產生警告訊息
        // 不過不會影響應用程式的運作
        addPreferencesFromResource(R.xml.mypreference);

        // 讀取deviceid設定元件
        defaultDeviceid = (Preference)findPreference("deviceid");
        // 建立SharedPreferences物件
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 讀取設定的預設deviceid
        String deviceid = sharedPreferences.getString("deviceid", "12345678");

        defaultDeviceid.setDefaultValue(deviceid);
    }
}
