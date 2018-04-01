package com.example.android.quakereport;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Myadapter extends ArrayAdapter<Earthquake> {
    private TextView textView=null,textView1=null,textView2=null,textView3=null,textView4=null,textView5=null;
    private  int res;
    Myadapter(Context context, ArrayList<Earthquake> arrayList,int res){
        super(context,0,arrayList);
        Log.i("myadapter constructor","called/////");
        this.res=res;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i("getview","called///////");
        View view=convertView;
        if(view==null){
            view=LayoutInflater.from(getContext()).inflate(res,parent,false);
            textView=(TextView)view.findViewById(R.id.t1);
            textView2=(TextView)view.findViewById(R.id.t2);
            textView4=(TextView)view.findViewById(R.id.t4);
            textView3=(TextView)view.findViewById(R.id.t3);
            textView5=(TextView)view.findViewById(R.id.t5);
        }
        Earthquake earthquake=getItem(position);
        textView.setText(earthquake.getMagnitude());
        GradientDrawable gradientDrawable=(GradientDrawable)textView.getBackground();
        int color=backgroundcolor(earthquake.getMagnitude());
        gradientDrawable.setColor(color);
        String text=earthquake.getPlace();
        String word[]=text.split("of");
        if(word.length==1){
            textView2.setText("At");
            textView4.setText(word[0]);
        }else{
            textView2.setText(word[0]+"of");
            textView4.setText(word[1]);

        }
        if(res==R.layout.earthquake_list_land){
         //   Log.i("date",earthquake.getTime());
            Date date=new Date(Long.parseLong(earthquake.getTime()));
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yy");
    // give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
            String d2d = sdf.format(date);
         //   Log.i("date",d2d);
            textView3.setText(d2d);
            //Log.i("color",String.valueOf(color));
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("h:mm a");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            d2d=simpleDateFormat.format(date);
            textView5.setText(d2d);
        }else{
            textView3.setVisibility(View.GONE);
            textView5.setVisibility(View.GONE);
        }
        return  view;
    }

    int backgroundcolor(String mag){
        Log.i("backgroundcolor","called/////");
        double magnitude=Double.parseDouble(mag);
        int color=0;
        int val=(int)Math.floor(magnitude);
       // Log.i("magnitude",String.valueOf(magnitude));
        switch(val){
            case 1:
                color=R.color.magnitude1;
                break;
            case 2:
                color=R.color.magnitude2;
                break;
            case 3:
                color= R.color.magnitude3;
                break;
            case 4:
                color=R.color.magnitude4;
                break;
            case 5:
                color=R.color.magnitude5;
                break;
            case 6:
                color=R.color.magnitude6;
                break;
            case 7:
                color=R.color.magnitude7;
                break;
            case 8:
                color=R.color.magnitude8;
                break;
            case 9:
                color=R.color.magnitude9;
                break;
            case 10:
                color=R.color.magnitude10plus;
        }
        //Log.i("color n",String.valueOf(color));
        return ContextCompat.getColor(getContext(),color);
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }



}
