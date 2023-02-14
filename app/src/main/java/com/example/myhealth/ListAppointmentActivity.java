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

public class ListAppointmentActivity extends AppCompatActivity {
    ListView lv;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    ArrayList<HashMap<String,String>> values = new ArrayList<HashMap<String,String>>();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointment);
        lv=findViewById(R.id.listAppointment);
        new Consult().execute();

    }

    class Consult extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(ListAppointmentActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map =new HashMap<>();
            JSONObject object = parser.makeHttpRequest("http://192.168.1.35/test/afficheAppointment.php","GET",map);
            try {
                success=object.getInt("success");
                if(success==1){
                    JSONArray patients = object.getJSONArray("patient");
                    for (int i=0;i<patients.length();i++)
                    {
                        JSONObject patient = patients.getJSONObject(i);
                        HashMap<String,String> m= new HashMap<String, String>();
                        m.put("nom",patient.getString("nom"));
                        m.put("phone",patient.getString("phone"));
                        m.put("date",patient.getString("date"));


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
            SimpleAdapter adapter = new SimpleAdapter(ListAppointmentActivity.this,values,R.layout.itemrv,
                    new String[]{"nom","phone","date"}, new int[]{R.id.txtNompatient,R.id.textNumber,R.id.textDaterv});
            lv.setAdapter(adapter);

        }
    }
}