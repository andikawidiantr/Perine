package id.progmob.perine.anggota;

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

import id.progmob.perine.MainActivity;
import id.progmob.perine.R;
import id.progmob.perine.koneksi;

public class PinjemBuku extends AppCompatActivity {

    Button btn_borrow;
    ConnectivityManager conMgr;
    String sbuku, spenulis, spenerbit, sjenis, success;;
    int sid;

    TextView buku, penulis;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    ProgressDialog pDialog;
    private static final String TAG = PinjemBuku.class.getSimpleName();
    id.progmob.perine.koneksi koneksi=new koneksi();
    private String pinjam = koneksi.isi_konten()+"createtransaksi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjem_buku);


        buku = findViewById(R.id.judulbuku);
        penulis = findViewById(R.id.penulisbuku);
        btn_borrow = findViewById(R.id.buttonBorrow);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            sid = intent.getInt("ed_id");
            sbuku = intent.getString("ed_buku");
            spenulis = intent.getString("ed_penulis");
            spenerbit = intent.getString("ed_penerbit");
            sjenis = intent.getString("ed_jenis");

//                    Toast.makeText(PinjemBuku.this, String.valueOf(sid) , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext() ,"Kosong intentnya", Toast.LENGTH_LONG).show();
        }

        buku.setText(sbuku);
        penulis.setText(spenulis);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }


        btn_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);
                String idUser = sharedpreferences.getString("id_user", null);
                String namaUser = sharedpreferences.getString("nama_user", null);
                String role = sharedpreferences.getString("role", null);
                String email = sharedpreferences.getString("email", null);

//                Toast.makeText(PinjemBuku.this,  sbuku , Toast.LENGTH_SHORT).show();

                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    pinjam(idUser, sid, namaUser, sbuku);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(PinjemBuku.this, dashboard_anggota.class);
                finish();
                Toast.makeText(getApplicationContext() ,"Cek Riwayat Peminjaman", Toast.LENGTH_LONG).show();
                startActivity(intent);

            }

        });
    }

    private void pinjam(final String id_user1,  final int id_buku, final String nama_user, final String judul_buku) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Save Date ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, pinjam, new Response.Listener<String>() {

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

                        // Memanggil Dashboards
                        Intent intent = new Intent(PinjemBuku.this, MainActivity.class);
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
                params.put("id_user", id_user1);
                params.put("id_buku", String.valueOf(id_buku));
                params.put("nama_user", nama_user);
                params.put("nama_buku", judul_buku);
//                params.put("jenis_kelamin", jeniskelamin_1);
//                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };

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