package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by blueb on 3/31/2017.
 */

public class Earthquake_adapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";


    public Earthquake_adapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context,0,earthquakes);

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View list_item= convertView;

        if(list_item==null){
            list_item= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Earthquake current_eq=getItem(position);

        TextView magView=(TextView) list_item.findViewById(R.id.mag);
        magView.setText(formatMagnitude((double)current_eq.getMagnitude()) );

        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();
        int magnitudeColor = getMagnitudeColor(current_eq.getMagnitude());

        magnitudeCircle.setColor(magnitudeColor);




        String originalLocation = current_eq.getLocation();
        String loc;
        String loc_offset;

        if(originalLocation.contains(LOCATION_SEPARATOR)){

            String parts[] = originalLocation.split(LOCATION_SEPARATOR);
            loc_offset=parts[0]+LOCATION_SEPARATOR;
            loc=parts[1];

        }
        else{

            loc_offset=getContext().getString(R.string.near_the);
            loc=originalLocation;
        }

        TextView offsetView=(TextView) list_item.findViewById(R.id.loc_offset);
        offsetView.setText(loc_offset);

        TextView locView=(TextView) list_item.findViewById(R.id.loc);
        locView.setText(loc);

        Date date=new Date(current_eq.getTime());

        TextView dateView=(TextView) list_item.findViewById(R.id.date);
        dateView.setText(formatDate(date));

        TextView timeView=(TextView) list_item.findViewById(R.id.time);
        timeView.setText(formatTime(date));

        return list_item;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
