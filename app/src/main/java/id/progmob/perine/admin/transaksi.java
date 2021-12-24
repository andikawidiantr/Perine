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
import id.progmob.perine.adapter.TransaksiAdapter;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataPinjam;

public class transaksi extends AppCompatActivity {

    SwipeRefreshLayout swipe;
    ArrayList itemList = new ArrayList();
    TextView judul,tanggal_kembali,tanggal_pinjam, status;


    id.progmob.perine.koneksi koneksi=new koneksi();

    //    String success;
    ConnectivityManager conMgr;

    private static final String TAG = transaksi.class.getSimpleName();

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

    private TransaksiAdapter adapter;
    private ArrayList<DataPinjam> arrayModelPinjam;

    public static transaksi mInstance;

    private DataPinjam dataPinjam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        recyclerView = findViewById(R.id.listPeminjaman);

        // agar method mainActivity bisa diakses
        mInstance = this;

        // memberi event klik pada fab (floating action buttom)


        judul = findViewById(R.id.rw_judul);
        tanggal_pinjam = findViewById(R.id.rw_tgl_pinjam);
        tanggal_kembali = findViewById(R.id.rw_tgl_kembali);
        status = findViewById(R.id.rw_status);

        // untuk menghemat ruang kita buat method MuadData
        getData(this);
//
    }

//    @Override
//    public void onRefresh() {
//        itemList.clear();
//        adapter.notifyDataSetChanged();
//        TampilkanData();
//    }

    public void getData(final Context context){
//        PgDialog.show(progressDialog);
        String url_showpinjam = koneksi.isi_konten()+"history";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_showpinjam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    arrayModelPinjam = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("peminjaman");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        dataPinjam = new DataPinjam();
                        dataPinjam.setId_peminjaman(data.getInt("id_peminjaman"));
                        dataPinjam.setNama_buku(data.getString("nama_buku"));
                        dataPinjam.setTanggal_pinjam(data.getString("tanggal_pinjam"));
                        dataPinjam.setTanggal_kembali(data.getString("tanggal_kembali"));
                        dataPinjam.setStatus(data.getString("status"));
                        dataPinjam.setNama_user(data.getString("nama_user"));

                        arrayModelPinjam.add(dataPinjam);
                    }


//                    System.out.println("ini adalah list data "+ arrayModelBuku.get(0).getBuku());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    adapter = new TransaksiAdapter(context, arrayModelPinjam);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    PgDialog.hide(progressDialog);
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
}