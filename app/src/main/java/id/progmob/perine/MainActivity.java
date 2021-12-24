package id.progmob.perine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import id.progmob.perine.admin.dashboard_admin;
import id.progmob.perine.anggota.dashboard_anggota;
import id.progmob.perine.anggota.regisAnggota;

public class MainActivity extends AppCompatActivity  {
    EditText txt_email, txt_password;
    Button btn_login;
    TextView regis;

    id.progmob.perine.koneksi koneksi=new koneksi();
    ProgressDialog pDialog;

    String success;
    ConnectivityManager conMgr;

    private String url = koneksi.isi_konten()+"loginAndroid";
    private static final String TAG = MainActivity.class.getSimpleName();

//    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_ID = "id_user";
    public final static String TAG_NAMA = "nama_user";
    public final static String TAG_ROLE = "role";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_JENIS_KELAMIN = "jenis_kelamin";
    public final static String TAG_PASS = "pass";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, nama, role, email, jenis_kelamin, pass;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_email = findViewById(R.id.email);
        txt_password = findViewById(R.id.pass);
        btn_login = findViewById(R.id.buttonLogin);

        regis = findViewById(R.id.register);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regispage = new Intent(MainActivity.this, regisAnggota.class);
                finish();
                startActivity(regispage);
            }
        });

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }

        // Cek session login jika TRUE maka langsung buka Dashbard
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        role = sharedpreferences.getString(TAG_ROLE, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        jenis_kelamin = sharedpreferences.getString(TAG_JENIS_KELAMIN, null);
        pass = sharedpreferences.getString(TAG_PASS, null);

        if (session) {
            if(role.equals("admin")){
            Intent intent = new Intent(MainActivity.this, dashboard_admin.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_NAMA, nama);
//            intent.putExtra(TAG_NIM, nim);
            intent.putExtra(TAG_EMAIL, email);
            finish();
//            Toast.makeText(getApplicationContext() ,role, Toast.LENGTH_LONG).show();
            startActivity(intent);
            }else {
                Intent intent3 = new Intent(MainActivity.this, dashboard_anggota.class);
                intent3.putExtra(TAG_ID, id);
                intent3.putExtra(TAG_NAMA, nama);
//            intent.putExtra(TAG_NIM, nim);
                intent3.putExtra(TAG_EMAIL, email);
                finish();
//            Toast.makeText(getApplicationContext() ,role, Toast.LENGTH_LONG).show();
                startActivity(intent3);
            }
        }
//        else {
//            Intent intent = new Intent(MainActivity.this, dashboard_admin.class);
//            intent.putExtra(TAG_ID, id);
//            intent.putExtra(TAG_NAMA, nama);
////            intent.putExtra(TAG_NIM, nim);
//            intent.putExtra(TAG_EMAIL, email);
//            finish();
//            startActivity(intent);
//        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_1 = txt_email.getText().toString();
                String password_1 = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (email_1.trim().length() > 0 && password_1.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(email_1, password_1);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //login
    private void checkLogin(final String email_1, final String password_1) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equals("1")) {
                        String id = jObj.getString(TAG_ID);
                        String nama = jObj.getString(TAG_NAMA);
                        String role = jObj.getString(TAG_ROLE);
                        String email = jObj.getString(TAG_EMAIL);
                        String jk = jObj.getString(TAG_JENIS_KELAMIN);
                        String password = jObj.getString(TAG_PASS);
                        Log.e("Successfully Login!", jObj.toString());

//                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_NAMA, nama);
                        editor.putString(TAG_ROLE, role);
                        editor.putString(TAG_EMAIL, email);
                        editor.putString(TAG_JENIS_KELAMIN, jk);
                        editor.putString(TAG_PASS, password);
                        editor.commit();


                        String roleLogin = jObj.getString("role");


                        if(roleLogin.equals("admin")){
//                            FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
                            Toast.makeText(getApplicationContext(),"Login Admin Success", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(MainActivity.this, dashboard_admin.class);
                            intent1.putExtra(TAG_ID, id);
                            intent1.putExtra(TAG_NAMA, nama);
                            intent1.putExtra(TAG_ROLE, role);
                            intent1.putExtra(TAG_EMAIL, email);
                            finish();
                            startActivity(intent1);

                        }else if(roleLogin.equals("pengguna")){
//                            FirebaseMessaging.getInstance().subscribeToTopic("pengguna");
                            Intent intent2 = new Intent(MainActivity.this, dashboard_anggota.class);
                            intent2.putExtra(TAG_ID, id);
                            intent2.putExtra(TAG_NAMA, nama);
                            intent2.putExtra(TAG_ROLE, role);
                            intent2.putExtra(TAG_EMAIL, email);
                            intent2.putExtra(TAG_JENIS_KELAMIN, jk);
                            finish();
                            startActivity(intent2);
                            Toast.makeText(getApplicationContext(),"Login Anggota Success", Toast.LENGTH_SHORT).show();
                        }

//                        if(user_type.equals("admin")){
//                            Intent intent = new Intent(MainActivity.this, dashboard_admin.class);
//                            intent.putExtra(TAG_ID, id);
//                            intent.putExtra(TAG_USERNAME, username);
//                            finish();
//                            startActivity(intent);
//                        }
//                        else{
//                            Intent intent = new Intent(Login.this, MainActivity.class);
//                            intent.putExtra(TAG_ID, id);
//                            intent.putExtra(TAG_USERNAME, username);
//                            finish();
//                            startActivity(intent);

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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "ERROR PP!", Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            public Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email_1);
                params.put("password", password_1);
//                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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