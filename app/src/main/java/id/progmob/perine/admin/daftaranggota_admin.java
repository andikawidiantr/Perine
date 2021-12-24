package id.progmob.perine.admin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
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
import id.progmob.perine.adapter.AnggotaAdapter;
import id.progmob.perine.adapter.BukuAdapter;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataAnggota;
import id.progmob.perine.model.DataBuku;

public class daftaranggota_admin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipe;
    ArrayList itemList = new ArrayList();
    TextView id_user, email, pass, nama_user, jenis_kelamin,role;

    id.progmob.perine.koneksi koneksi=new koneksi();

    //    String success;
    ConnectivityManager conMgr;

    private String url_index = koneksi.isi_konten()+"anggota";
    private String url_delete = koneksi.isi_konten()+"deleteanggota";


    private static final String TAG = daftaranggota_admin.class.getSimpleName();

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

    private AnggotaAdapter adapter;
    private ArrayList<DataAnggota> arrayModelAnggota;

    public static daftaranggota_admin mInstance;

    private DataAnggota dataanggota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftaranggota);

        // mendeklarasi
        recyclerView = findViewById(R.id.listAnggota);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swlayout2);
        swipe.setOnRefreshListener(this);

        // agar method mainActivity bisa diakses
        mInstance = this;

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
                    arrayModelAnggota = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("anggota");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        dataanggota = new DataAnggota();
                        dataanggota.setId(data.getString("id_user"));
                        dataanggota.setEmail(data.getString("email"));
                        dataanggota.setNama_user(data.getString("nama_user"));
                        dataanggota.setPass(data.getString("pass"));
                        dataanggota.setJenis_kelamin(data.getString("jenis_kelamin"));
                        dataanggota.setRole(data.getString("role"));
                        arrayModelAnggota.add(dataanggota);
                    }


                    System.out.println("ini adalah list data "+ arrayModelAnggota.get(0).getNama_user());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    adapter = new AnggotaAdapter(context, arrayModelAnggota);
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