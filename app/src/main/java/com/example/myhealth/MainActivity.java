package com.example.myhealth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button btnConnect;
    private Button btnSign;
    private EditText txtLogin, txtPassword;
    //public static String login;
    //private String password;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();

    int success;
    JSONArray a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);

        btnSign = findViewById(R.id.btnInscription);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent action = new Intent(MainActivity.this,InscriptionActivity.class);
                startActivity(action);
            }
        });



        //permet d'executer le web service en background

        class Log extends AsyncTask<String, String, String>
        {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Patientez SVP");
                dialog.show();
            }

            //dans la methode doInBackground faut jamais effectuer une tache d'affichage
            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> map = new HashMap<>();
                map.put("login", txtLogin.getText().toString());
                map.put("password", txtPassword.getText().toString());
                JSONObject object = parser.makeHttpRequest("http://192.168.56.1/test/connect.php", "POST", map);

                try {
                    success = object.getInt("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.cancel();
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                if(success==1)
                {
                    alert.setMessage("Login done successfully");
                    alert.setNeutralButton("ok", null);
                    alert.show();
                    Intent intent= new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);


                }
                else
                    {
                    alert.setMessage("No user with this login or password");
                    alert.setNeutralButton("ok", null);
                    alert.show();

                }
            }
        }

        btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = txtLogin.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (login.isEmpty() || password.isEmpty()){
                    String message = "Required Field";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                }
                else {

                    new Log().execute();
                }

            }
        });

    }
}