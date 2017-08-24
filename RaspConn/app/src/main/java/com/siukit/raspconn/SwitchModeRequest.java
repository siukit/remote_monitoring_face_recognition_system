package com.siukit.raspconn;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siukit on 31/03/2017.
 */

public class SwitchModeRequest extends StringRequest {

//    private static final String LI_URL = "http://192.168.3.2/switch_mode.php";
    private static final String LI_URL = "http://194.81.104.22/~14412104/dis/switch_mode.php";
    private Map<String, String> params;

    public SwitchModeRequest(String is_on, String mode, Response.Listener<String> listener){
        super(Request.Method.POST, LI_URL, listener, null);
        params = new HashMap<>();
        //params.put("Content-Type", "application/json; charset=utf-8");
        params.put("mode", mode);
        params.put("is_on", is_on);
    }

    @Override
    public Map<String, String> getParams() {
        //params.put("Content-Type", "application/json; charset=utf-8");
        return params;
    }
}
