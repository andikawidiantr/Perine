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

public class editadmin extends AppCompatActivity {
    EditText txt_nama, txt_pass, txt_email;
    Button btn_edit;
    RadioGroup jk_regis;
    RadioButton rb_laki, rb_perempuan;

    // untuk menerima Data dari MainActivity
    String snama, spass, semail,sjk;
    int sid;

    id.progmob.perine.koneksi koneksi=new koneksi();
    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;

    String tag_json_obj = "json_obj_req";

    private static final String TAG = editadmin.class.getSimpleName();

    public static final String TAG_ID = "id_user";
    public static final String TAG_NAMA = "nama_user";
    public static final String TAG_PASS = "pass";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_JK = "jenis_kelamin";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editadmin);
        // deklarasi
        txt_email = findViewById(R.id.emailregis);
        txt_pass = findViewById(R.id.passwordregis);
        txt_nama = findViewById(R.id.namaregis);
        txt_nama = findViewById(R.id.namaregis);
        jk_regis = findViewById(R.id.jenis_kelamin);
        rb_laki = findViewById(R.id.rb_laki);
        rb_perempuan = findViewById(R.id.rb_perempuan);
        btn_edit = findViewById(R.id.btn_edit);

        // menerima data dari MainActivity menggunakana "Bundle"
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            sid = intent.getInt("ed_id_admin");
            snama = intent.getString("ed_nama_user");
            spass = intent.getString("ed_pass");
            semail = intent.getString("ed_email");
            sjk = intent.getString("ed_jk");

//            Toast.makeText(editbuku.this, String.valueOf(sid) , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext() ,"Kosong intentnya", Toast.LENGTH_LONG).show();
        }

        // lalu "Bundle" ini, akan di set ke edittext
        txt_nama.setText(snama);
        txt_email.setText(semail);
        txt_pass.setText(spass);
//        jenis_buku.setText(sjenis);
        if(sjk.equals("Laki-Laki")){
            rb_laki.setChecked(true);
        }else if(sjk.equals("Perempuan")){
            rb_perempuan.setChecked(true);
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
                String uemail = txt_email.getText().toString();
                String unama = txt_nama.getText().toString();
                String upass = txt_pass.getText().toString();

                RadioButton pilihan_jk  = (RadioButton)findViewById(jk_regis.getCheckedRadioButtonId());

                String ujenis = pilihan_jk.getText().toString();

                // mengecek kolom yang kosong
                if (uemail.trim().length() > 0 && unama.trim().length() > 0 && upass.trim().length() > 0 && ujenis.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        updateData(sid, uemail, unama, upass, ujenis);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(editadmin.this, daftaradmin.class);
                finish();
                startActivity(intent);
//                updateData(sid, ubuku, upenulis, upenerbit, ujenis);
            }
        });
    }

    private void updateData(int id, String s_email, String s_nama, String s_pass, String s_jenis) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Save Data ...");
        showDialog();

        String url_edit = koneksi.isi_konten()+"editadmin/"+id;
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
                        Log.e(url_edit, jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                        Log.d("get edit data", jObj.toString());
//                        String idx = jObj.getString(TAG_ID);
//                        String bukux = jObj.getString(TAG_BUKU);
//                        String penulisx = jObj.getString(TAG_PENULIS);
//                        String penerbitx = jObj.getString(TAG_PENERBIT);
//                        String jenisx = jObj.getString(TAG_JENIS);
                        Intent intent = new Intent(editadmin.this, daftaradmin.class);
                        finish();
                        startActivity(intent);


                    } else {
                        Toast.makeText(editadmin.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                params.put("email", s_email);
                params.put("pass", s_pass);
                params.put("nama_user", s_nama);
                params.put("jenis_kelamin", s_jenis);

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