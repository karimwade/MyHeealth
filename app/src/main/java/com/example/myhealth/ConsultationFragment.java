package com.example.myhealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ConsultationFragment extends Fragment {
EditText txtIdPatient,txtObservations;
Button btnConsult,btnListConsult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_consultation, container, false);
        txtIdPatient=view.findViewById(R.id.txtidPatient);
        txtObservations=view.findViewById(R.id.txtObservations);

        btnListConsult=view.findViewById(R.id.btnListConsult);
        btnListConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ListConsultationActivity.class);
                startActivity(intent);
            }
        });

        btnConsult=view.findViewById(R.id.btnConsult);
        btnConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consult();
            }
        });
        return view;
    }
    public void consult()
    {
        String url="http://192.168.1.35/test/consultation.php";
        OkHttpClient client = new OkHttpClient();
        FormBody formBody= new FormBody.Builder()
                .add("idPatient",txtIdPatient.getText().toString())
                .add("observations",txtObservations.getText().toString())
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
                        String message="Consultation Failed";
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String result=response.body().string();
                    JSONObject object = new JSONObject(result);
                    int success = object.getInt("success");
                    if (success==1){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String message ="consultation carried out successfully";
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