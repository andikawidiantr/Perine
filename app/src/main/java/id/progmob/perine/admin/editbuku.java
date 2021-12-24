package id.progmob.perine.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.progmob.perine.MainActivity;
import id.progmob.perine.R;
import id.progmob.perine.app.AppController;
import id.progmob.perine.koneksi;

public class editbuku extends AppCompatActivity {
    // implementasi
    EditText buku, penulis, penerbit;
    Button btn_edit;
    RadioGroup jenis_buku;

    // untuk menerima Data dari MainActivity
    String sbuku, spenulis, spenerbit, sjenis;
    int sid;

    id.progmob.perine.koneksi koneksi=new koneksi();
    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    private static final String TAG = editbuku.class.getSimpleName();

    public static final String TAG_ID = "id_buku";
    public static final String TAG_BUKU = "buku";
    public static final String TAG_PENULIS = "penulis";
    public static final String TAG_PENERBIT = "penerbit";
    public static final String TAG_JENIS = "jenis";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbuku);

        // deklarasi
        buku = findViewById(R.id.AE_judul);
        penulis = findViewById(R.id.AE_penulis);
        penerbit = findViewById(R.id.AE_penerbit);
        jenis_buku = findViewById(R.id.AE_opsiKat);
        btn_edit = findViewById(R.id.btn_update_buku);

        RadioButton fiksi = (RadioButton)findViewById(R.id.rb_fiksi);
        RadioButton  nonfiksi = (RadioButton)findViewById(R.id.rb_nonfiksi);

        // menerima data dari MainActivity menggunakana "Bundle"
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            sid = intent.getInt("ed_id");
            sbuku = intent.getString("ed_buku");
            spenulis = intent.getString("ed_penulis");
            spenerbit = intent.getString("ed_penerbit");
            sjenis = intent.getString("ed_jenis");

//            Toast.makeText(editbuku.this, String.valueOf(sid) , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext() ,"Kosong intentnya", Toast.LENGTH_LONG).show();
        }

        // lalu "Bundle" ini, akan di set ke edittext
        buku.setText(sbuku);
        penulis.setText(spenulis);
        penerbit.setText(spenerbit);
//        jenis_buku.setText(sjenis);
        if(sjenis.equals("fiksi")){
            fiksi.setChecked(true);
        }else if(sjenis.equals("non-fiksi")){
            nonfiksi.setChecked(true);
        }

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mengambil text dalam edittext
                String ubuku = buku.getText().toString();
                String upenerbit = penulis.getText().toString();
                String upenulis = penerbit.getText().toString();

                RadioButton pilihan_jk  = (RadioButton)findViewById(jenis_buku.getCheckedRadioButtonId());

                String ujenis = pilihan_jk.getText().toString();

                // mengecek kolom yang kosong
                if (ubuku.trim().length() > 0 && upenulis.trim().length() > 0 && upenerbit.trim().length() > 0 && ujenis.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        updateData(sid, ubuku, upenulis, upenerbit, ujenis);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(editbuku.this, daftarbuku_admin.class);
                finish();
                startActivity(intent);
//                updateData(sid, ubuku, upenulis, upenerbit, ujenis);
            }
        });
    }

    private void updateData(int id, String s_buku, String s_penulis, String s_penerbit, String s_jenis) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Save Data ...");
        showDialog();

        String url_edit = koneksi.isi_konten()+"editbuku/"+id;
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error pada json
                    if (success == 1) {
                        Log.e("Successfully Edit!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                        Log.d("get edit data", jObj.toString());
//                        String idx = jObj.getString(TAG_ID);
//                        String bukux = jObj.getString(TAG_BUKU);
//                        String penulisx = jObj.getString(TAG_PENULIS);
//                        String penerbitx = jObj.getString(TAG_PENERBIT);
//                        String jenisx = jObj.getString(TAG_JENIS);
                        Intent intent = new Intent(editbuku.this, daftarbuku_admin.class);
                        finish();
                        startActivity(intent);


                    } else {
                        Toast.makeText(editbuku.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit Error: " + error.getMessage());
                hideDialog();
//                Toast.makeText(editbuku.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                // set ke params
//                HashMap hashMap = new HashMap<>();
//                hashMap.put("id_buku", id);
                params.put("buku", s_buku);
                params.put("penulis", s_penulis);
                params.put("penerbit", s_penerbit);
                params.put("jenis", s_jenis);

                return params;
            }
        };
//        Toast.makeText(editbuku.this, s_buku + " " + s_penulis + " " + s_penerbit, Toast.LENGTH_LONG).show();
        // memanggil AppController dan menambahkan dalam antrin
        // text "ubah_barang" anda bisa mengganti inisial yang lain
//        AppController.getInstance().addToQueue(strReq, "buku");
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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