package com.siukit.raspconn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewStreamActivity extends AppCompatActivity {

    private String RPI_IP = "192.168.3.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);

        final WebView wvStreamView = (WebView)findViewById(R.id.wvStreamView);
        int zoom_level=100;
        wvStreamView.setInitialScale(zoom_level);
//        wvStreamView.getSettings().setJavaScriptEnabled(true);
//        wvStreamView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        //http://169.254.173.113:8080/stream/video.mjpeg
        final String stream = "http://" + RPI_IP + ":8080/stream/video.mjpeg";

        wvStreamView.setWebViewClient(new WebViewClient());
//        int width = wvStreamView.getWidth();
//        int height = wvStreamView.getHeight();
//        + "?width="+width+"&height="+height
        wvStreamView.loadUrl(stream);

//        wvStreamView.post(new Runnable()
//        {
//            @Override
//            public void run() {
//                int width = wvStreamView.getWidth();
//                int height = wvStreamView.getHeight();
//                wvStreamView.loadUrl(stream);
//            }
//        });
    }
}
