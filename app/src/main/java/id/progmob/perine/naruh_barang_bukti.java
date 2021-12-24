package id.progmob.perine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.progmob.perine.adapter.BukuAnggotaAdapter;
import id.progmob.perine.admin.daftarbuku_admin;
import id.progmob.perine.anggota.PinjemBuku;
import id.progmob.perine.app.AppController;
import id.progmob.perine.model.DataBuku;

public class naruh_barang_bukti {

//     // implementasi
//    EditText buku, penulis, penerbit;
//    Button btn_edit;
//    RadioGroup jenis_buku;
//
//    // untuk menerima Data dari MainActivity
//    String sbuku, spenulis, spenerbit, sjenis;
//    int sid;
//
//    id.progmob.perine.koneksi koneksi=new koneksi();
//    ProgressDialog pDialog;
//
//    int success;
//    ConnectivityManager conMgr;
//
//    String tag_json_obj = "json_obj_req";
//
//    private static final String TAG = editbuku.class.getSimpleName();
//
//    public static final String TAG_ID = "id_buku";
//    public static final String TAG_BUKU = "buku";
//    public static final String TAG_PENULIS = "penulis";
//    public static final String TAG_PENERBIT = "penerbit";
//    public static final String TAG_JENIS = "jenis";
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_MESSAGE = "message";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_editbuku);
//
//        // deklarasi
//        buku = findViewById(R.id.AE_judul);
//        penulis = findViewById(R.id.AE_penulis);
//        penerbit = findViewById(R.id.AE_penerbit);
//        jenis_buku = findViewById(R.id.AE_opsiKat);
//        btn_edit = findViewById(R.id.btn_update_buku);
//
//        RadioButton fiksi = (RadioButton)findViewById(R.id.rb_fiksi);
//        RadioButton  nonfiksi = (RadioButton)findViewById(R.id.rb_nonfiksi);
//
//        // menerima data dari MainActivity menggunakana "Bundle"
//        Bundle intent = getIntent().getExtras();
//        if (intent != null) {
//            sid = intent.getInt("ed_id");
//            sbuku = intent.getString("ed_buku");
//            spenulis = intent.getString("ed_penulis");
//            spenerbit = intent.getString("ed_penerbit");
//            sjenis = intent.getString("ed_jenis");
//
////            Toast.makeText(editbuku.this, String.valueOf(sid) , Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getApplicationContext() ,"Kosong intentnya", Toast.LENGTH_LONG).show();
//        }
//
//        // lalu "Bundle" ini, akan di set ke edittext
//        buku.setText(sbuku);
//        penulis.setText(spenulis);
//        penerbit.setText(spenerbit);
////        jenis_buku.setText(sjenis);
//        if(sjenis.equals("fiksi")){
//            fiksi.setChecked(true);
//        }else if(sjenis.equals("non-fiksi")){
//            nonfiksi.setChecked(true);
//        }
//
//        btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // mengambil text dalam edittext
//                String ubuku = buku.getText().toString();
//                String upenerbit = penulis.getText().toString();
//                String upenulis = penerbit.getText().toString();
//
//                RadioButton pilihan_jk  = (RadioButton)findViewById(jenis_buku.getCheckedRadioButtonId());
//
//                String ujenis = pilihan_jk.getText().toString();
//
//                updateData(sid, ubuku, upenulis, upenerbit, ujenis);
//
//                // validasi kode, nama dan harga tidak boleh kosong
////                if (kode.isEmpty()) { // kode_barang tidak lebih dari 6 digit
////                    Toast.makeText(UbahActivity.this, "Kode Masih Kosong!", Toast.LENGTH_SHORT).show();
////                } else if (nama.isEmpty()) {
////                    Toast.makeText(UbahActivity.this, "Nama Masih Kosong!", Toast.LENGTH_SHORT).show();
////                } else if (harga.isEmpty()) { // harga barang tidak boleh dari 9 digit
////                    Toast.makeText(UbahActivity.this, "Harga Masih Kosong!", Toast.LENGTH_SHORT).show();
////                } else {
////                    // mengupdate data
////                    updateData(ed_id, kode, nama, harga);
////                }
//            }
//        });
//    }
//
////    private void updateData(int id, String kode, String nama, String harga) {
////        String url_edit = koneksi.isi_konten()+"editbuku";
////        // buat StringRequest volley dan jangan lupa requestnya POST "Request.Method.POST"
////        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                // menerapkan ke model class menggunakan GSON
////                // mengkonversi JSON ke java object
////                ResponStatus responStatus = new Gson().fromJson(response, ResponStatus.class);
////                int status_kode = responStatus.getStatus_kode();
////                String status_pesan = responStatus.getStatus_pesan();
////
////                // jika respon status kode yg dihasilkan 1 maka berhasil
////                if (status_kode == 1) {
////                    Toast.makeText(UbahActivity.this, status_pesan, Toast.LENGTH_SHORT).show();
////                    daftarbuku_admin.mInstance.getData(); // memanggil MainActivity untuk memproses method MemuatData()
////                    finish(); // keluar
////                } else {
////                    Toast.makeText(UbahActivity.this, "Status Kesalahan Tidak Diketahui!", Toast.LENGTH_SHORT).show();
////                }
////            }
//    private void updateData(int id, String s_buku, String s_penulis, String s_penerbit, String s_jenis) {
//        String url_edit = koneksi.isi_konten()+"editbuku/"+id;
//        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Response: " + response.toString());
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    success = jObj.getInt(TAG_SUCCESS);
//
//                    // Cek error pada json
//                    if (success == 1) {
////                        Log.d("get edit data", jObj.toString());
////                        String idx = jObj.getString(TAG_ID);
////                        String bukux = jObj.getString(TAG_BUKU);
////                        String penulisx = jObj.getString(TAG_PENULIS);
////                        String penerbitx = jObj.getString(TAG_PENERBIT);
////                        String jenisx = jObj.getString(TAG_JENIS);
//
//                        Intent intent = new Intent(editbuku.this, daftarbuku_admin.class);
//                        finish();
//                        startActivity(intent);
//
//                    } else {
//                        Toast.makeText(editbuku.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
////                Toast.makeText(editbuku.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            public Map getParams() {
//                // set ke params
//                HashMap hashMap = new HashMap<>();
////                hashMap.put("id_buku", id);
//                hashMap.put("buku", s_buku);
//                hashMap.put("penulis", s_penulis);
//                hashMap.put("penerbit", s_penerbit);
//                hashMap.put("jenis", s_jenis);
//
//                return hashMap;
//            }
//        };
//        Toast.makeText(editbuku.this, s_buku + " " + s_penulis + " " + s_penerbit, Toast.LENGTH_LONG).show();
//        // memanggil AppController dan menambahkan dalam antrin
//        // text "ubah_barang" anda bisa mengganti inisial yang lain
////        AppController.getInstance().addToQueue(strReq, "ubah_barang");
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }


//    package id.progmob.perine.adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import id.progmob.perine.MainActivity;
//import id.progmob.perine.R;
//import id.progmob.perine.admin.daftarbuku_admin;
//import id.progmob.perine.admin.dashboard_admin;
//import id.progmob.perine.anggota.PinjemBuku;
//import id.progmob.perine.app.AppController;
//import id.progmob.perine.koneksi;
//import id.progmob.perine.model.DataBuku;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//    public class BukuAnggotaAdapter extends RecyclerView.Adapter<id.progmob.perine.adapter.BukuAnggotaAdapter.ViewHolder> {
//        private Context context;
//        private List<DataBuku> arrayModelBarangs;
//        //    ArrayList<DataBuku> arrayModelBarangs = new ArrayList<DataBuku>();
//        id.progmob.perine.koneksi koneksi=new koneksi();
//
//        private static final String TAG = daftarbuku_admin.class.getSimpleName();
//
//        public static final String TAG_ID = "id_buku";
//        public static final String TAG_BUKU = "buku";
//        public static final String TAG_PENULIS = "penulis";
//        public static final String TAG_PENERBIT = "penerbit";
//        public static final String TAG_JENIS = "jenis";
//        private static final String TAG_SUCCESS = "success";
//        private static final String TAG_MESSAGE = "message";
//
//        private id.progmob.perine.adapter.BukuAnggotaAdapter adapter;
//
//        String tag_json_obj = "json_obj_req";
//        // membuat kontruksi recyclerviewadapter
//        public BukuAnggotaAdapter(Context context, ArrayList<DataBuku> arrayModelBarangs) {
//            this.context = context;
//            this.arrayModelBarangs = arrayModelBarangs;
//        }
//
//        @NonNull
//        @Override
//        public id.progmob.perine.adapter.BukuAnggotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            // membuat layout inflater
//            View view = LayoutInflater.from(context).inflate(R.layout.card_buku, parent, false);
//            return new id.progmob.perine.adapter.BukuAnggotaAdapter.ViewHolder(view);
//        }
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onBindViewHolder(@NonNull id.progmob.perine.adapter.BukuAnggotaAdapter.ViewHolder holder, int position) {
//            // mendapatkan posisi item
//            DataBuku modelBuku = arrayModelBarangs.get(position);
//
//            // menset data
//            // holder.id_buku.setText(modelBuku.getId());
//            holder.buku.setText(modelBuku.getBuku());
//            holder.penulis.setText(modelBuku.getPenulis());
//            holder.penerbit.setText(modelBuku.getPenerbit());
//            holder.jenis.setText(modelBuku.getJenis());
//        }
//
//        @Override
//        public int getItemCount() {
//            // mengembalikan data set
//            return arrayModelBarangs.size();
//        }
//
//        // membuat class viewholder
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            // implementasi textview
//            private TextView id_buku,penulis,penerbit,jenis,buku;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                // mendeklarasi item ketika diklik
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // mendapatkan posisi pada adapter
//                        int position = getAdapterPosition();
//                        // mengambil posisi pada arraymodelbarang
//                        DataBuku arraybuku = arrayModelBarangs.get(position);
//
//                        int hasil_pos = position+1;
//
////                    String[] pilihan = {"Edit", "Delete"};
//
//                        Intent intent = new Intent(context, PinjemBuku.class);
//
//                        context.startActivity(intent);
//
//                    }
//                });
//            }
//        }
//    }
}