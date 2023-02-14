package com.example.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InscriptionActivity extends AppCompatActivity {
    EditText txtFirstname,txtLastname,txtDateBirth,txtPhone,txtLog,txtPwd;
    Button add;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        txtFirstname=findViewById(R.id.txtFirstname);
        txtLastname=findViewById(R.id.txtLastname);
        txtDateBirth=findViewById(R.id.txtDateBirth);
        txtPhone=findViewById(R.id.txtPhone);
        txtLog=findViewById(R.id.txtLog);
        txtPwd=findViewById(R.id.txtPwd);

        add=findViewById(R.id.btnSubscribe);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        new Add().execute();
            }
        });
    }
    class Add extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InscriptionActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("firstname",txtFirstname.getText().toString());
            map.put("lastname",txtLastname.getText().toString());
            map.put("dateBirth",txtDateBirth.getText().toString());
            map.put("phone",txtPhone.getText().toString());
            map.put("login",txtLog.getText().toString());
            map.put("password",txtPwd.getText().toString());


            JSONObject objet= parser.makeHttpRequest("http://192.168.1.35/test/inscription.php","GET",map);
            try {
                success=objet.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (success==1){
                Toast.makeText(InscriptionActivity.this,"User saved",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(InscriptionActivity.this,"Failed registration",Toast.LENGTH_LONG).show();

            }
            dialog.cancel();
        }
    }

}