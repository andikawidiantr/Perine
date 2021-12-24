package id.progmob.perine.admin;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.progmob.perine.R;
import id.progmob.perine.app.AppController;
import id.progmob.perine.koneksi;

public class gantistatus extends AppCompatActivity {
    // implementasi
    EditText judul, nama_user, tanggal_pinjam, status;
    Button btn_ganti;

    // untuk menerima Data dari MainActivity
    String sbuku, snama_user, stanggal_pinjam, sstatus;
    int sid;

    id.progmob.perine.koneksi koneksi=new koneksi();
    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    private static final String TAG = gantistatus.class.getSimpleName();

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
        setContentView(R.layout.activity_gantistatus);

        // deklarasi
        judul = findViewById(R.id.bukujudul);
        nama_user = findViewById(R.id.namapeminjam);
        tanggal_pinjam = findViewById(R.id.tanggalpinjam);
        status = findViewById(R.id.statuspinjam);
        btn_ganti = findViewById(R.id.btn_submit_ganti);



        // menerima data dari MainActivity menggunakana "Bundle"
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            sid = intent.getInt("ed_id_pinjam");
            snama_user = intent.getString("ed_nama_user");
            stanggal_pinjam = intent.getString("tgl_pinjam");
            sstatus = intent.getString("ed_status");
            sbuku = intent.getString("ed_nama_buku");

//            Toast.makeText(gantistatus.this, String.valueOf(sid) , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext() ,"Kosong intentnya", Toast.LENGTH_LONG).show();
        }

        // lalu "Bundle" ini, akan di set ke edittext
        judul.setText(sbuku);
        nama_user.setText(snama_user);
        tanggal_pinjam.setText(stanggal_pinjam);
        status.setText(sstatus);


        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }

        btn_ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mengambil text dalam edittext
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    GantiStatus(sid);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }


                Intent intent = new Intent(gantistatus.this, transaksi.class);
                finish();
                startActivity(intent);
//                updateData(sid, ubuku, upenulis, upenerbit, ujenis);
            }
        });
    }

    private void GantiStatus(int id) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Save Data ...");
        showDialog();

        String url_gantistatus = koneksi.isi_konten()+"gantistatus/"+id;
        StringRequest strReq = new StringRequest(Request.Method.GET, url_gantistatus, new Response.Listener<String>() {

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
                        Intent intent = new Intent(gantistatus.this, transaksi.class);
                        finish();
                        startActivity(intent);


                    } else {
                        Toast.makeText(gantistatus.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
            public Map getParams() {
                // Posting parameters to login url
                Map params = new HashMap<>();
                // set ke params
//                HashMap hashMap = new HashMap<>();
//                hashMap.put("id_buku", id);
                params.put("id_peminjaman", sid);

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