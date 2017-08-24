package com.siukit.raspconn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.util.ArrayList;

/**
 * Created by siukit on 24/03/2017.
 */

//this class customize an Arayadapter which will be used for displaying logs on listview
public class LogsAdaptor extends ArrayAdapter<Log> {
    private final Context context;
    private final ArrayList<Log> data;
    private final int layoutResourceId;
    final ImagePopup imagePopup = new ImagePopup(ViewLogsActivity.context);



    public LogsAdaptor(Context context, int layoutResourceId, ArrayList<Log> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            //two textviews and a image view for displaying name and entry time
            holder = new ViewHolder();
            holder.textView1 = (TextView) row.findViewById(R.id.tvName);
            holder.textView2 = (TextView) row.findViewById(R.id.tvEntryTime);
            holder.imageView = (ImageView) row.findViewById(R.id.ivPicture);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Log log = data.get(position);

        holder.textView1.setText(log.getName());
        holder.textView2.setText(log.getEntryTime());


        //decode the image from database and set it on image view
        if (!log.getPicture().equals("null")) {
            byte[] decodedString = Base64.decode(log.getPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Bitmap icon = decodedByte;
            holder.imageView.setImageBitmap(icon);

            //otherwise just use default image
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher);
        }

        final ImageView imgView = holder.imageView;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup.setBackgroundColor(Color.BLACK);
                imagePopup.setWindowWidth(1200);
                imagePopup.setWindowHeight(1200);
                imagePopup.setHideCloseIcon(false);
                imagePopup.initiatePopup(imgView.getDrawable());
            }
        });
        return row;
    }

    static class ViewHolder {
        TextView textView1;
        TextView textView2;
        ImageView imageView;

    }
}