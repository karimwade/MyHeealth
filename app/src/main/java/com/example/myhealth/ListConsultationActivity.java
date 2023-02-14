package com.example.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListConsultationActivity extends AppCompatActivity {
ListView ls;
ProgressDialog dialog;
JSONParser parser = new JSONParser();
ArrayList<HashMap<String,String>> values = new ArrayList<HashMap<String,String>>();
  int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_consultation);
        ls=findViewById(R.id.listConsult);
        new All().execute();

    }

    class All extends AsyncTask <String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(ListConsultationActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map =new HashMap<>();
            JSONObject object = parser.makeHttpRequest("http://192.168.1.35/test/afficheConsultation.php","GET",map);
            try {
                success=object.getInt("success");
                if(success==1){
                    JSONArray patients = object.getJSONArray("patient");
                    for (int i=0;i<patients.length();i++)
                    {
                        JSONObject patient = patients.getJSONObject(i);
                        HashMap<String,String> m= new HashMap<String, String>();
                        m.put("id",patient.getString("id"));
                        m.put("firstname",patient.getString("firstname"));
                        m.put("lastname",patient.getString("lastname"));
                        m.put("phone",patient.getString("phone"));
                        m.put("observations",patient.getString("observations"));

                        values.add(m);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();
            SimpleAdapter adapter = new SimpleAdapter(ListConsultationActivity.this,values,R.layout.item,
                    new String[]{"id","firstname","lastname","phone","observations"}, new int[]{R.id.idP,R.id.textFirst,R.id.textLast,R.id.textTel,R.id.textObs});
                    ls.setAdapter(adapter);

        }
    }

}