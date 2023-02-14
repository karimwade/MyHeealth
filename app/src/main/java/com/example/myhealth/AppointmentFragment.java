package com.example.myhealth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AppointmentFragment extends Fragment {
    EditText txtName,txtPhone,txtDAte;
    Button rv,listrv;
    JSONParser parser=new JSONParser();
    int success;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        txtName=view.findViewById(R.id.txtName);
        txtPhone=view.findViewById(R.id.txtTelephone);
        txtDAte=view.findViewById(R.id.txtAptDate);
        rv=view.findViewById(R.id.btnAppt);
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 rendezVous();
            }
        });

        listrv=view.findViewById(R.id.btnListAppt);
        listrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent list = new Intent(getActivity(),ListAppointmentActivity.class);
                startActivity(list);
            }
        });
        return view;

    }
    public void rendezVous()
    {
        String url ="http://192.168.1.35/test/rendezvous.php";
        OkHttpClient client = new OkHttpClient();
        FormBody formBody= new FormBody.Builder()
                .add("nom",txtName.getText().toString())
                .add("phone",txtPhone.getText().toString())
                .add("date",txtDAte.getText().toString())
                .build();
        Request request= new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message="Appointment Failed";
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String result=response.body().string();
                    JSONObject object=new JSONObject(result);
                    int success=object.getInt("success");
                    if(success==1){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String message="Succesfull meeting";
                                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String message="Appointment Failed";
                                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });



    }

}