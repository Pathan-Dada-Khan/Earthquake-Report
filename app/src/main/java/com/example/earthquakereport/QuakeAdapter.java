package com.example.earthquakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuakeAdapter extends ArrayAdapter<Quake>{
    public QuakeAdapter(Activity content, ArrayList<Quake> quake){
        super(content,0,quake);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view=convertView;

        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView mag=view.findViewById(R.id.mag);
        TextView place=view.findViewById(R.id.place);
        TextView country=view.findViewById(R.id.country);
        TextView date=view.findViewById(R.id.date);
        TextView time=view.findViewById(R.id.time);

        Quake current = getItem(position);

        mag.setText(magFormat(current.getMag()));
        place.setText(current.getPlace());
        country.setText(current.getCountry());

        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        int magnitudeColor = getMagnitudeColor(current.getMag());
        magnitudeCircle.setColor(magnitudeColor);

        Date dateFormat = new Date(current.getDate());

        date.setText(formatDate(dateFormat));
        time.setText(formatTime(dateFormat));

        return view;
    }

    private String formatDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("LLL dd, yyyy");
        return format.format(date);
    }

    private String formatTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        return format.format(date);
    }

    private String magFormat(Double mag){
        DecimalFormat format = new DecimalFormat("0.0");

        return format.format(mag);
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
