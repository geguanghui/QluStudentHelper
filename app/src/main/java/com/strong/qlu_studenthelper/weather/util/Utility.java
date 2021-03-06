
package com.strong.qlu_studenthelper.weather.util;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.strong.qlu_studenthelper.weather.db.City;
import com.strong.qlu_studenthelper.weather.db.County;
import com.strong.qlu_studenthelper.weather.db.Province;
import com.strong.qlu_studenthelper.weather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.litepal.LitePalBase.TAG;

public class Utility {

    /**
     * parsing and process provincial data returned by the server
     */
    public static boolean handleProvinceResponse(String response) {
        Log.d(TAG, "handleProvinceResponse: executed/" + response );
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++ ) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    Log.d(TAG, "q" + provinceObject.getString("name"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * parsing and process municipal data returned by the server
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * parsing and process county data returned by the server
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * parsing JSON data returned into weather bean
     */
    public static Weather handleWeatherResponse(String response) {
        try {

            // 将整个json实例化保存在jsonObject中
            JSONObject jsonObject = new JSONObject(response);
           /* // 从jsonObject中取出键为"HeWeather6"的数据,并保存在数组中
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            // 取出数组中的第一项,并以字符串形式保存
            String weatherContent = jsonArray.getJSONObject(0).toString();
            // 返回通过Gson解析后的Weather对象*/
            Gson g=new Gson();
            Weather weather=  g.fromJson(jsonObject.toString(),Weather.class);

            return weather;

        } catch (Exception e) {
            e.printStackTrace();


        }
        return null;

    }

}