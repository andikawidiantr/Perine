package id.progmob.perine.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import id.progmob.perine.koneksi;

public class createbuku extends AppCompatActivity {

    EditText txt_judul, txt_penulis, txt_penerbit;
    RadioGroup kat_buku;
    RadioButton pilihan_kat;

    id.progmob.perine.koneksi koneksi=new koneksi();
    ProgressDialog pDialog;

    String success;
    ConnectivityManager conMgr;

    private String url_insert = koneksi.isi_konten()+"createbuku";
    private String url_edit = koneksi.isi_konten()+"editbuku";

    private static final String TAG = createbuku.class.getSimpleName();

    public static final String TAG_ID = "id_buku";
    public static final String TAG_BUKU = "buku";
    public static final String TAG_PENULIS = "penulis";
    public static final String TAG_PENERBIT = "penerbit";
    public static final String TAG_JENIS = "jenis";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    private TextView AlJudul, AlPenulis, AlPenerbit, AlJenis;
    private String mjudul, mpenulis, mpenerbit,mjenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_buku);

        txt_judul = findViewById(R.id.AE_judul);
        txt_penulis = findViewById(R.id.AE_penulis);
        txt_penerbit = findViewById(R.id.AE_penerbit);
        kat_buku = findViewById(R.id.AE_opsiKat);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }

        final Button btn_regis = (Button) findViewById(R.id.btn_insert_buku);
        btn_regis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RadioButton pilihan_jenis  = (RadioButton)findViewById(kat_buku.getCheckedRadioButtonId());

                 mjudul = txt_judul.getText().toString();
                 mpenulis = txt_penulis.getText().toString();
                 mpenerbit = txt_penerbit.getText().toString();
                 mjenis = pilihan_jenis.getText().toString();

                // mengecek kolom yang kosong
                if (mjudul.trim().length() > 0 && mpenulis.trim().length() > 0 && mpenerbit.trim().length() > 0 && mjenis.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        DialogForm();
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }

//                Intent intent = new Intent(createbuku.this, daftarbuku_admin.class);
//                finish();
//                startActivity(intent);

            }
        });
    }

    private void DialogForm(){
        dialog = new AlertDialog.Builder(createbuku.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alert_dialog_buku, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        AlJudul = (TextView) dialogView.findViewById(R.id.AlJudulBuku);
        AlPenulis = (TextView) dialogView.findViewById(R.id.AlPenulis);
        AlPenerbit = (TextView) dialogView.findViewById(R.id.AlPenerbit);
        AlJenis = (TextView) dialogView.findViewById(R.id.AlJenis);

        AlJudul.setText("Judul Buku\t\t\t\t\t : " + mjudul );
        AlPenulis.setText("Penulis\t\t\t\t\t\t\t\t: " + mpenulis);
        AlPenerbit.setText("Penerbit\t\t\t\t\t\t\t: " + mpenerbit);
        AlJenis.setText("Jenis\t\t\t\t\t\t\t\t\t: " + mjenis);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                InsertBuku(mjudul, mpenulis, mpenerbit, mjenis);
                Intent intent = new Intent(createbuku.this, MainActivity.class);
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

    private void InsertBuku(final String input_buku, final String input_penulis, final String input_penerbit, final String input_jenis) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Save Date ...");
        showDialog();

//        String url = "";

//        if (TAG_ID.isEmpty()) {
//            url = url_insert;
//        }
//        else {
//            url = url_edit;
//        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {
//        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {
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
                        Intent intent = new Intent(createbuku.this, daftarbuku_admin.class);
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
                Log.e(TAG, "Insert Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "ERROR PP!", Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            public Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("buku", input_buku);
                params.put("penulis", input_penulis);
                params.put("penerbit", input_penerbit);
                params.put("jenis", input_jenis);
//                params.put("Content-Type", "application/json; charset=utf-8");
//                if (TAG_ID.isEmpty()) {
//                    params.put("buku", input_buku);
//                    params.put("penulis", input_penulis);
//                    params.put("penerbit", input_penerbit);
//                    params.put("jenis", input_jenis);
//                } else {
//                    params.put("id_buku", TAG_ID);
//                    params.put("buku", input_buku);
//                    params.put("penulis", input_penulis);
//                    params.put("penerbit", input_penerbit);
//                    params.put("jenis", input_jenis);
//                }
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