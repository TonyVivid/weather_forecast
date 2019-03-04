package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
String update_time="????";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         new showWeatherInfo().execute();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog().build());




    }

public void changeImg(ImageView view,String weather_type){


        if(weather_type.equals("小雨"))
            //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainy_small));
            view.setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
        if(weather_type.equals("晴"))
            //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny_small));
            view.setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
        if(weather_type.equals("阴"))
            //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.windy_small));
            view.setImageDrawable(getResources().getDrawable(R.drawable.windy_small));


}
public String getNumber(String str){
        String regex="[^0-9|\56]";
       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(str);
      return m.replaceAll("").trim();
}
public String connectURL(){
    String my_url = "http://t.weather.sojson.com/api/weather/city/101040100";
    Log.d("TAG","come to url");
    try {

        URL url = new URL(my_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int code = connection.getResponseCode();
        Log.d("TAG","come to code");
        String c=String.valueOf(code);
        Log.d("TAG",c);
        if(code==200) {
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {

                response.append(line).append("\r\n");
            }

            in.close();
           reader.close();
           connection.disconnect();
           return response.toString();
        }
        else{
                return null;
        }

        }
        catch (Exception e){
        e.printStackTrace();
        return null;

        }
}
public String changeWeekday(String day){
        String weekday=day;
        if(day.equals("星期一")){
            weekday="MON";
        }
        if(day.equals("星期二")){
            weekday="TUE";
        }
        if(day.equals("星期三")){
        weekday="WED";
        }
        if(day.equals("星期四")){
        weekday="THU";
        }
        if(day.equals("星期五")){
        weekday="FRI";
        }
        if(day.equals("星期六")){
        weekday="SAT";
        }
        if(day.equals("星期日")){
        weekday="SUN";
        }
        return weekday;
}
public String changeTitleDay(String day){
    String weekday=day;
    if(day.equals("星期一")){
        weekday="MONDAY";
    }
    if(day.equals("星期二")){
        weekday="TUESDAY";
    }
    if(day.equals("星期三")){
        weekday="WEDNESDAY";
    }
    if(day.equals("星期四")){
        weekday="THURSDAY";
    }
    if(day.equals("星期五")){
        weekday="FRIDAY";
    }
    if(day.equals("星期六")){
        weekday="SATURDAY";
    }
    if(day.equals("星期日")){
        weekday="SUNDAY";
    }
    return weekday;

}
public void getWeatherInfo() {

                String info=connectURL();
                if(info==null) {
                    Toast.makeText(getApplicationContext(),"please check internet connection",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject json = new JSONObject(info);
                    JSONObject time = json.getJSONObject("cityInfo");
                    String current_time=time.getString("updateTime");


                        update_time=time.getString("updateTime");
                        JSONObject data = json.getJSONObject("data");

                        JSONArray forecast = data.getJSONArray("forecast");

                        JSONObject day0 = forecast.getJSONObject(0);
                        JSONObject day1 = forecast.getJSONObject(1);
                        JSONObject day2 = forecast.getJSONObject(2);
                        JSONObject day3 = forecast.getJSONObject(3);
                        JSONObject day4 = forecast.getJSONObject(4);



                        String tody_date = day0.getString("ymd");

                        String tody_hightemperature = day0.getString("high");
                        String tody_lowtemperature = day0.getString("low");
                        String today_day=day0.getString("week");
                        TextView txt_weekday=(TextView) findViewById(R.id.title_day);
                        TextView txt_temperature = (TextView) findViewById(R.id.temperature_of_the_day);
                        TextView txt_position = (TextView) findViewById(R.id.tv_location);
                        TextView txt_date=(TextView)findViewById(R.id.tv_date);

                        txt_temperature.setText(getNumber(tody_hightemperature));
                        txt_position.setText("Chongqing");
                        txt_date.setText(tody_date);
                        txt_weekday.setText(changeTitleDay(today_day));
                        TextView txt_first=(TextView)findViewById(R.id.first_day);
                        TextView txt_second=(TextView)findViewById(R.id.second_day);
                        TextView txt_third=(TextView)findViewById(R.id.third_day);
                        TextView txt_fourth=(TextView)findViewById(R.id.fourth_day);

                        txt_first.setText(changeWeekday(day1.getString("week")));
                        txt_second.setText(changeWeekday(day2.getString("week")));
                        txt_third.setText(changeWeekday(day3.getString("week")));
                        txt_fourth.setText(changeWeekday(day4.getString("week")));
                        ImageView img0=(ImageView)findViewById(R.id.img_weather_condition);
                        ImageView img1=(ImageView)findViewById(R.id.first_img);
                        ImageView img2=(ImageView)findViewById(R.id.second_img);
                        ImageView img3=(ImageView)findViewById(R.id.third_img);
                        ImageView img4=(ImageView)findViewById(R.id.fourth_img);
                        changeImg(img0,day0.getString("type"));
                        changeImg(img1,day1.getString("type"));
                        changeImg(img2,day2.getString("type"));
                        changeImg(img3,day3.getString("type"));
                        changeImg(img4,day4.getString("type"));





                }
                catch(Exception e){
                    e.printStackTrace();
                }

        }

        public void btnClick(View view) {

        String info=connectURL();

        if(info==null) {
            Toast.makeText(getApplicationContext(),"please check internet connection",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            try {
                JSONObject json = new JSONObject(info);
                JSONObject time = json.getJSONObject("cityInfo");
                String current_time = time.getString("updateTime");
                Log.d("TAG", "click the button");
                Log.d("TAG", current_time);
                Log.d("TAG", update_time);
                if (update_time.equals(current_time)) {
                    Toast.makeText(getApplicationContext(), "this is the latest weather forecast", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "it's same");
                } else {
                    new DownloadUpdate().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private class showWeatherInfo extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... strings){
            getWeatherInfo();
            return null;
        }
    }
    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
           getWeatherInfo();


            return null;
        }


        }
}

