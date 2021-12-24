package id.progmob.perine.anggota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import id.progmob.perine.adapter.BukuAnggotaAdapter;
import id.progmob.perine.admin.daftarbuku_admin;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataBuku;

public class daftarbuku_anggota extends AppCompatActivity {
    FloatingActionButton fab;

    SwipeRefreshLayout swipe;
    ArrayList itemList = new ArrayList();
    TextView id_buku, buku_txt, penulis, penerbit, jenis;
    id.progmob.perine.koneksi koneksi=new koneksi();

    //    String success;
    ConnectivityManager conMgr;

    private String url_index = koneksi.isi_konten()+"buku";
    private String url_delete = koneksi.isi_konten()+"delete_buku";


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

    private BukuAnggotaAdapter adapter;
    private ArrayList<DataBuku> arrayModelBuku;

    public static daftarbuku_anggota mInstance;

    private DataBuku databuku;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarbuku_anggota);
        // mendeklarasi
        recyclerView = findViewById(R.id.listBuku);

        // agar method mainActivity bisa diakses
        mInstance = this;


        id_buku = findViewById(R.id.tv_id);
        buku_txt = findViewById(R.id.tv_buku);
        penulis = findViewById(R.id.tv_penulis);
        penerbit = findViewById(R.id.tv_penerbit);
        jenis = findViewById(R.id.tv_jenis);

        // untuk menghemat ruang kita buat method MuadData
        getData(this);
//
    }

    private void getData(final Context context){
//        PgDialog.show(progressDialog);
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

                    adapter = new BukuAnggotaAdapter(context, arrayModelBuku);
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