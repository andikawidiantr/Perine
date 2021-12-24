package id.progmob.perine.admin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.progmob.perine.R;
import id.progmob.perine.adapter.AdminAdapter;
import id.progmob.perine.adapter.AnggotaAdapter;
import id.progmob.perine.adapter.BukuAdapter;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataAdmin;
import id.progmob.perine.model.DataAnggota;
import id.progmob.perine.model.DataBuku;

public class daftaradmin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    FloatingActionButton fab;
    SwipeRefreshLayout swipe;
    ArrayList itemList = new ArrayList();
    TextView id_user, email, pass, nama_user, jenis_kelamin,role;

    id.progmob.perine.koneksi koneksi=new koneksi();

    //    String success;
    ConnectivityManager conMgr;

    private String url_index = koneksi.isi_konten()+"admin";
    private String url_delete = koneksi.isi_konten()+"deleteanggota";

    private static final String TAG = daftaradmin.class.getSimpleName();

    public static final String TAG_ID = "id_user";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASS = "pass";
    public static final String TAG_NAMAUSER = "nama_user";
    public static final String TAG_JENISKELAMIN = "jenis_kelamin";
    public static final String TAG_ROLE= "role";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    private RecyclerView recyclerView;
    private FloatingActionButton fab_tambah;

    private AdminAdapter adapter;
    private ArrayList<DataAdmin> arrayModelAdmin;

    public static daftaradmin mInstance;

    private DataAdmin dataadmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftaradmin);

        // mendeklarasi
        recyclerView = findViewById(R.id.listAdmin);
        fab_tambah = findViewById(R.id.fab_add_admin);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swlayout1);

        // agar method mainActivity bisa diakses
        mInstance = this;

        swipe.setOnRefreshListener(this);

        fab_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // berpindah ke class TambahActivity
                startActivity(new Intent(daftaradmin.this, createadmin.class));
            }
        });

        id_user = findViewById(R.id.tv_id_anggota);
        nama_user= findViewById(R.id.tv_nama);
        email = findViewById(R.id.tv_email);
        jenis_kelamin = findViewById(R.id.tv_jeniskelamin);

        // untuk menghemat ruang kita buat method MuadData
        getData(this);
    }

    public void getData(final Context context){
//        PgDialog.show(progressDialog);
        itemList.clear();
        swipe.setRefreshing(true);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_index, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    arrayModelAdmin = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("admin");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        dataadmin = new DataAdmin();
                        dataadmin.setId(data.getInt("id_user"));
                        dataadmin.setEmail(data.getString("email"));
                        dataadmin.setNama_user(data.getString("nama_user"));
                        dataadmin.setPass(data.getString("pass"));
                        dataadmin.setJenis_kelamin(data.getString("jenis_kelamin"));
                        dataadmin.setRole(data.getString("role"));
                        arrayModelAdmin.add(dataadmin);
                    }


                    System.out.println("ini adalah list data "+ arrayModelAdmin.get(0).getNama_user());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    adapter = new AdminAdapter(context, arrayModelAdmin);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
//                    PgDialog.hide(progressDialog);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                PgDialog.hide(progressDialog);
//                Common.volleyErrorHandle(DoaActivity.this, error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void cancelPost(View view) {
        super.onBackPressed();
    }
    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        getData(getApplicationContext());
    }
}