package com.example.asyrofiabdusani.earthquakeapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DecimalFormat;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by Asyrofi Abdusani on 16/02/2018.
 */

public class EarthquakeAdapter extends ArrayAdapter {
    private static final String LOCATION_SEPARATOR="of";
    public EarthquakeAdapter(MainActivity context, ArrayList<Earthquake> earthquakes) {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_list, parent, false);
        }

        Earthquake listItem = (Earthquake) getItem(position);

        TextView skalaTextView = (TextView) listItemView.findViewById(R.id.skala);
        String formatSkala=formatMagnitude(listItem.getmSkala());
        skalaTextView.setText(formatSkala);

        TextView urlTextView = (TextView) listItemView.findViewById(R.id.url);
        urlTextView.setText((CharSequence) listItem.getmUrl());

        Date waktu=new Date(listItem.getmWaktu());

        TextView waktuTextVeiw=(TextView) listItemView.findViewById(R.id.waktu);
        String FormatWaktu= formatDate(waktu);
        waktuTextVeiw.setText(FormatWaktu);

        TextView jamTextVeiw=(TextView) listItemView.findViewById(R.id.pukul);
        String FormatJam= formatTime(waktu);
        jamTextVeiw.setText(FormatJam);

        String originalLocation=listItem.getmTempat();
        String primaryLocation;
        String locationOffset;

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.tempat);
        primaryLocationView.setText(primaryLocation);

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.jarak);
        locationOffsetView.setText(locationOffset);

        GradientDrawable magnitudeCircle = (GradientDrawable) skalaTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(listItem.getmSkala());
        magnitudeCircle.setColor(magnitudeColor);


        return listItemView;
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
