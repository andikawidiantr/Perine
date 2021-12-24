package id.progmob.perine.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.progmob.perine.R;
import id.progmob.perine.admin.daftaranggota_admin;
import id.progmob.perine.app.AppController;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataAnggota;

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.ViewHolder>{
    private Context context;
    private List<DataAnggota> arrayModelAnggota;
    id.progmob.perine.koneksi koneksi=new koneksi();

    private static final String TAG = daftaranggota_admin.class.getSimpleName();

    public static final String TAG_ID = "id_user";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASS = "pass";
    public static final String TAG_NAMAUSER = "nama_user";
    public static final String TAG_JENISKELAMIN = "jenis_kelamin";
    public static final String TAG_ROLE= "role";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private AnggotaAdapter adapter;

    String tag_json_obj = "json_obj_req";
    // membuat kontruksi recyclerviewadapter
    public AnggotaAdapter(Context context, ArrayList<DataAnggota> arrayModelAnggota) {
        this.context = context;
        this.arrayModelAnggota = arrayModelAnggota;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_anggota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataAnggota modelAnggota = arrayModelAnggota.get(position);

        // menset data
//        holder.id_buku.setText(modelBuku.getId());
        holder.tv_id_anggota.setText(modelAnggota.getId());
        holder.tv_nama.setText(modelAnggota.getNama_user());
        holder.tv_email.setText(modelAnggota.getEmail());
        holder.tv_jeniskelamin.setText(modelAnggota.getJenis_kelamin());
    }

    @Override
    public int getItemCount() {
        return arrayModelAnggota.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_id_anggota, tv_nama, tv_email, tv_jeniskelamin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id_anggota = itemView.findViewById(R.id.tv_id_anggota);
            tv_nama= itemView.findViewById(R.id.tv_nama);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_jeniskelamin = itemView.findViewById(R.id.tv_jeniskelamin);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mendapatkan posisi pada adapter
                    int position = getAdapterPosition();
                    // mengambil posisi pada arraymodelbarang
                    DataAnggota arrayanggota = arrayModelAnggota.get(position);

                    int hasil_pos = position+1;

                    String[] pilihan = {"Delete"};
                    // menamplkan pilihan alertdialog
                    new AlertDialog.Builder(context)
                            .setTitle("Pilihan")
                            .setItems(pilihan, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) { // 1 sama dengan Ubah
//                                        deletebuku(position+1,arraybuku);
//                                        Toast.makeText(context, String.valueOf(arraybuku), Toast.LENGTH_LONG).show();
                                        String url_delete = koneksi.isi_konten()+"deleteanggota/"+arrayanggota.getId();
//                                        Toast.makeText(context, url_delete, Toast.LENGTH_SHORT).show();

                                        StringRequest strReq = new StringRequest(Request.Method.GET, url_delete, new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                Log.d(TAG, "Response: " + response.toString());

                                                try {
                                                    JSONObject jObj = new JSONObject(response);
                                                    int success = jObj.getInt(TAG_SUCCESS);

                                                    // Cek error pada json
                                                    if (success == 1) {
                                                        Log.e("delete", jObj.toString());

//                                                        getData();

                                                        Toast.makeText(context, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                                        adapter.notifyDataSetChanged();

                                                    } else {
                                                        Toast.makeText(context, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    // JSON error
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e(TAG, "Error Delete: " + error.getMessage());
                                                Toast.makeText(context, error.getMessage() + String.valueOf(hasil_pos), Toast.LENGTH_LONG).show();
                                            }
                                        }) {

//                                            @Override
//                                            protected Map<String, String> getParams() {
//                                                // Posting parameters ke post url
//                                                Map<String, String> params = new HashMap<String, String>();
////                                                params.put("id_buku", String.valueOf(hasil_pos));
//
//                                                return params;
//                                            }
                                        };
                                        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
                                    }
                                }
                            })
                            .create()
                            .show();
                }
            });
        }
    }
}

