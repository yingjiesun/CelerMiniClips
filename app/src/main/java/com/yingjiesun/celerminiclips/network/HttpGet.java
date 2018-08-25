package com.yingjiesun.celerminiclips.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpGet {

    //data hard coded for now
    public static JSONArray getJsonArr(){
        JSONArray jsonArr;
        String json_str = "[{\"id\":1,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_1.jpg\",\"videoUrl\":\"https://media.giphy.com/media/l0ExncehJzexFpRHq/giphy.mp4\"},{\"id\":2,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_2.jpg\",\"videoUrl\":\"https://media.giphy.com/media/26gsqQxPQXHBiBEUU/giphy.mp4\"},{\"id\":3,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_3.jpg\",\"videoUrl\":\"https://media.giphy.com/media/oqLgjAahmDPvG/giphy.mp4\"},{\"id\":4,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_4.jpg\",\"videoUrl\":\"https://media.giphy.com/media/d1E1szXDsHUs3WvK/giphy.mp4\"},{\"id\":5,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_5.jpg\",\"videoUrl\":\"https://media.giphy.com/media/OiJjUsdAb11aE/giphy.mp4\"},{\"id\":6,\"imageUrl\":\"https://wpclipart.com/education/animal_numbers/animal_number_6.jpg\",\"videoUrl\":\"https://media.giphy.com/media/4My4Bdf4cakLu/giphy.mp4\"}]";
        try {
            jsonArr = new JSONArray(json_str);
            return jsonArr;
        } catch (Exception e) {
            Log.i("tag", "*** HttpGet  EXCEPTION: " + e);
        }
        return null;
    }
}
