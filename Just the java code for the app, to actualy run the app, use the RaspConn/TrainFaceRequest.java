package com.siukit.raspconn;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siukit on 02/04/2017.
 */

public class TrainFaceRequest extends StringRequest {
    private static final String RE_URL = "http://194.81.104.22/~14412104/dis/new_employee.php";
    private Map<String, String> params;

    public TrainFaceRequest(String name, String frontal_pic, String smile_pic, String glasses_pic, String reg_time, Response.Listener<String> listener){
        super(Method.POST, RE_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("frontal_pic", frontal_pic);
        params.put("smile_pic", smile_pic);
        params.put("glasses_pic", glasses_pic);
        params.put("reg_time", reg_time);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
