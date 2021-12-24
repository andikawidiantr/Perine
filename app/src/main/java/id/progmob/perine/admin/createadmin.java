package id.progmob.perine.admin;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.progmob.perine.MainActivity;
import id.progmob.perine.R;
import id.progmob.perine.anggota.regisAnggota;
import id.progmob.perine.koneksi;

public class createadmin extends AppCompatActivity {
    EditText txt_nama, txt_pass, txt_email;
    RadioGroup jk_regis;
    RadioButton pilihan_jk;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    private TextView AlNama, AlPass, AlEmail, AlJk;
    private String email_1, password_1, nama_1 , jeniskelamin_1;

    id.progmob.perine.koneksi koneksi=new koneksi();
    ProgressDialog pDialog;

    String success;
    ConnectivityManager conMgr;

    private String regis = koneksi.isi_konten()+"createadmin";
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String TAG_ID = "id_user";
    public static final String TAG_NAMA = "nama_user";
    public static final String TAG_PASS = "pass";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_JK = "jenis_kelamin";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createadmin);

        txt_email = findViewById(R.id.emailregis);
        txt_pass = findViewById(R.id.passwordregis);
        txt_nama = findViewById(R.id.namaregis);
        txt_nama = findViewById(R.id.namaregis);
        jk_regis = findViewById(R.id.jenis_kelamin);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }

        final Button btn_regis = (Button) findViewById(R.id.btn_submit_regis);
        btn_regis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RadioButton pilihan_jk  = (RadioButton)findViewById(jk_regis.getCheckedRadioButtonId());

                email_1 = txt_email.getText().toString();
                password_1 = txt_pass.getText().toString();
                nama_1 = txt_nama.getText().toString();
                jeniskelamin_1 = pilihan_jk.getText().toString();

                // mengecek kolom yang kosong
                if (email_1.trim().length() > 0 && password_1.trim().length() > 0 && nama_1.trim().length() > 0 && jeniskelamin_1.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
//                        Registrasi(email_1, password_1, nama_1, jeniskelamin_1);
                        DialogForm();
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }

//                Intent intent = new Intent(createadmin.this, MainActivity.class);
//                finish();
//                startActivity(intent);

            }
        });
    }

    private void DialogForm(){
        dialog = new AlertDialog.Builder(createadmin.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alert_dialogs, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        AlNama = (TextView) dialogView.findViewById(R.id.AlNama);
        AlPass = (TextView) dialogView.findViewById(R.id.AlPass);
        AlJk = (TextView) dialogView.findViewById(R.id.AlJk);
        AlEmail = (TextView) dialogView.findViewById(R.id.AlEmail);

        AlNama.setText("Nama\t\t\t\t\t\t\t\t\t\t: " + nama_1);
        AlPass.setText("Password\t\t\t\t\t\t\t: " + password_1);
        AlEmail.setText("Email\t\t\t\t\t\t\t\t\t\t: " + email_1);
        AlJk.setText("Jenis Kelamin\t\t\t: " + jeniskelamin_1);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Registrasi(email_1, password_1, nama_1, jeniskelamin_1);
                Intent intent = new Intent(createadmin.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void Registrasi(final String email_1, final String password_1, final String nama_1, final String jeniskelamin_1) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Save Date ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, regis, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Regis Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getString(TAG_SUCCESS);

                    // Check for error node in json
                    if (success.equals("1")) {
//                        String id = jObj.getString(TAG_ID);
//                        String nama = jObj.getString(TAG_NAMA);
//                        String nim = jObj.getString(TAG_JK);
//                        String email = jObj.getString(TAG_EMAIL);

                        Log.e("Successfully Regis!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putBoolean(session_status, true);
//                        editor.putString(TAG_ID, id);
//                        editor.putString(TAG_NAMA, nama);
////                        editor.putString(TAG_NIM, nim);
//                        editor.putString(TAG_EMAIL, email);
//                        editor.commit();

                        // Memanggil Dashboards
                        Intent intent = new Intent(createadmin.this, daftaradmin.class);
                        finish();
                        startActivity(intent);
                    } else {
                        //Toast.makeText(getApplicationContext(),
                        //jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Email / Password salah!", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "ERROR Json!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Regis Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "ERROR PP!", Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            public Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email_1);
                params.put("pass", password_1);
                params.put("nama_user", nama_1);
                params.put("jenis_kelamin", jeniskelamin_1);
//                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };

        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}