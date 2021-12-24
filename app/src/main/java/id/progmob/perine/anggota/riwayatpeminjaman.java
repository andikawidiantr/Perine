package id.progmob.perine.anggota;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
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

import id.progmob.perine.MainActivity;
import id.progmob.perine.R;
import id.progmob.perine.adapter.TransaksiAdapter;
import id.progmob.perine.admin.transaksi;
import id.progmob.perine.database.db_pinjam;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataPinjam;

public class riwayatpeminjaman extends AppCompatActivity {

    TextView judul,tanggal_kembali,tanggal_pinjam, status;

    id.progmob.perine.koneksi koneksi=new koneksi();

    //    String success;
    ConnectivityManager conMgr;

    private static final String TAG = riwayatpeminjaman.class.getSimpleName();

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

    public static riwayatpeminjaman mInstance;

    private DataPinjam dataPinjam;

    String sbuku, snama_user, stanggal_pinjam, sstatus;
    int sid;
    private db_pinjam db;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayatpeminjaman);

        recyclerView = findViewById(R.id.listPeminjamananggota);

        // agar method mainActivity bisa diakses
        mInstance = this;

        // memberi event klik pada fab (floating action buttom)


        judul = findViewById(R.id.rw_judul);
        tanggal_pinjam = findViewById(R.id.rw_tgl_pinjam);
        tanggal_kembali = findViewById(R.id.rw_tgl_kembali);
        status = findViewById(R.id.rw_status);

        db=new db_pinjam(this);
        // untuk menghemat ruang kita buat method MuadData
//        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();

//        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
//
//               @Override
//               public void onLost(Network network) {
//                   Toast.makeText(getApplicationContext(),"matiang",Toast.LENGTH_LONG).show();
//                    arrayModelPinjam=db.getRiwayatPeminjaman();
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//                    recyclerView.setLayoutManager(linearLayoutManager);
//                    adapter = new TransaksiAdapter(getApplicationContext(), arrayModelPinjam);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
//               }
//               @Override
//                public void onAvailable(Network network) {
//                    getData(getApplicationContext());
//                    Toast.makeText(getApplicationContext(),"hidup wifinya",Toast.LENGTH_LONG).show();
//            }
//           }
//        );

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                getData(this);
//                Toast.makeText(getApplicationContext(),"hidup wifinya",Toast.LENGTH_LONG).show();
            } else {
                arrayModelPinjam=db.getRiwayatPeminjaman();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new TransaksiAdapter(getApplicationContext(), arrayModelPinjam);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }

//        if (conMgr.getActiveNetworkInfo() != null
//                && conMgr.getActiveNetworkInfo().isAvailable()
//                && conMgr.getActiveNetworkInfo().isConnected()) {
//            getData(this);
//            Toast.makeText(getApplicationContext(),"hidup wifinya",Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getApplicationContext(),"matiang",Toast.LENGTH_LONG).show();
//            arrayModelPinjam=db.getRiwayatPeminjaman();
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            adapter = new TransaksiAdapter(getApplicationContext(), arrayModelPinjam);
//            recyclerView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//            Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
//        }


//
    }

    public void getData(final Context context){
//        PgDialog.show(progressDialog);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String idUser = sharedpreferences.getString("id_user", null);
        String namaUser = sharedpreferences.getString("nama_user", null);
        String role = sharedpreferences.getString("role", null);
        String email = sharedpreferences.getString("email", null);

        String url_riwayat = koneksi.isi_konten()+"showpinjem/"+idUser;
//        Toast.makeText(riwayatpeminjaman.this,  idUser , Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_riwayat, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    arrayModelPinjam = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("peminjaman_anggota");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        dataPinjam = new DataPinjam();
                        dataPinjam.setId_peminjaman(data.getInt("id_peminjaman"));
                        dataPinjam.setId_user(data.getInt("id_user"));
                        dataPinjam.setId_bukupinjam(data.getInt("id_buku"));
                        dataPinjam.setNama_buku(data.getString("nama_buku"));
                        dataPinjam.setTanggal_pinjam(data.getString("tanggal_pinjam"));
                        dataPinjam.setTanggal_kembali(data.getString("tanggal_kembali"));
                        dataPinjam.setStatus(data.getString("status"));
                        dataPinjam.setNama_user(data.getString("nama_user"));

                        arrayModelPinjam.add(dataPinjam);

                    }

                    //masukin sql
                    db.insertRiwayatPeminjamans(arrayModelPinjam);
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
}