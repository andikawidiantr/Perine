package id.progmob.perine.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import id.progmob.perine.adapter.BukuAdapter;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataBuku;

//public class daftarbuku extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
public class daftarbuku_admin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    FloatingActionButton fab;
    SwipeRefreshLayout swipe;
    ArrayList itemList = new ArrayList();
    TextView id_buku, buku_txt, penulis, penerbit, jenis;
    id.progmob.perine.koneksi koneksi=new koneksi();
    ConnectivityManager conMgr;

    private String url_index = koneksi.isi_konten()+"buku";

    private static final String TAG = daftarbuku_admin.class.getSimpleName();
    public static final String TAG_ID = "id_buku";
    public static final String TAG_BUKU = "buku";
    public static final String TAG_PENULIS = "penulis";
    public static final String TAG_PENERBIT = "penerbit";
    public static final String TAG_JENIS = "jenis";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    private RecyclerView recyclerView;
    private FloatingActionButton fab_tambah;
    private BukuAdapter adapter;
    private ArrayList<DataBuku> arrayModelBuku;
    public static daftarbuku_admin mInstance;
    private DataBuku databuku;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarbuku);

        // mendeklarasi
        recyclerView = findViewById(R.id.listBuku);
        fab_tambah = findViewById(R.id.fab_add_buku);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swlayout);

        // agar method mainActivity bisa diakses
        mInstance = this;

//        // memanggil kontruktor adapter dan mengimplementasikannya
//        adapter = new BukuAdapter(daftarbuku.this, arrayModelBarangs);
//        // lalu menset ke recyclerview adapter
//        recyclerView.setAdapter(adapter);
//        // membuat linear layout manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        // menset ke recyclerview layoutmanger
//        recyclerView.setLayoutManager(linearLayoutManager);

        swipe.setOnRefreshListener(this);

//        swipe.post(new Runnable() {
//               @Override
//               public void run() {
//                   swipe.setRefreshing(true);
//                   itemList.clear();
//                           adapter.notifyDataSetChanged();
//                   getData(getApplicationContext());
//               }
//           }
//        );


        // memberi event klik pada fab (floating action buttom)
        fab_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // berpindah ke class TambahActivity
                startActivity(new Intent(daftarbuku_admin.this, createbuku.class));
            }
        });

        id_buku = findViewById(R.id.tv_id);
        buku_txt = findViewById(R.id.tv_buku);
        penulis = findViewById(R.id.tv_penulis);
        penerbit = findViewById(R.id.tv_penerbit);
        jenis = findViewById(R.id.tv_jenis);

        // untuk menghemat ruang kita buat method MuadData
        getData(this);
//
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        getData(getApplicationContext());
    }

    public void getData(final Context context){
        itemList.clear();
//        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_index, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    arrayModelBuku = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("book");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        databuku = new DataBuku();
                        databuku.setId(data.getInt("id_buku"));
                        databuku.setBuku(data.getString("buku"));
                        databuku.setPenulis(data.getString("penulis"));
                        databuku.setPenerbit(data.getString("penerbit"));
                        databuku.setJenis(data.getString("jenis"));
                        arrayModelBuku.add(databuku);
                    }


                    System.out.println("ini adalah list data "+ arrayModelBuku.get(0).getBuku());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    adapter = new BukuAdapter(context, arrayModelBuku);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
//                    PgDialog.hide(progressDialog);
                } catch (JSONException e) {
//                    PgDialog.hide(progressDialog);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipe.setRefreshing(false);;
//                Common.volleyErrorHandle(DoaActivity.this, error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void cancelPost(View view) {
        super.onBackPressed();
    }
}