package id.progmob.perine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import id.progmob.perine.MainActivity;
import id.progmob.perine.R;
import id.progmob.perine.admin.daftarbuku_admin;
import id.progmob.perine.admin.dashboard_admin;
import id.progmob.perine.anggota.PinjemBuku;
import id.progmob.perine.app.AppController;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataBuku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukuAnggotaAdapter extends RecyclerView.Adapter<BukuAnggotaAdapter.ViewHolder> {
    private Context context;
    private List<DataBuku> arrayModelBarangs;
    //    ArrayList<DataBuku> arrayModelBarangs = new ArrayList<DataBuku>();
    id.progmob.perine.koneksi koneksi=new koneksi();

    private static final String TAG = daftarbuku_admin.class.getSimpleName();

    public static final String TAG_ID = "id_buku";
    public static final String TAG_BUKU = "buku";
    public static final String TAG_PENULIS = "penulis";
    public static final String TAG_PENERBIT = "penerbit";
    public static final String TAG_JENIS = "jenis";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private BukuAnggotaAdapter adapter;

    String tag_json_obj = "json_obj_req";
    // membuat kontruksi recyclerviewadapter
    public BukuAnggotaAdapter(Context context, List<DataBuku> arrayModelBarangs) {
        this.context = context;
        this.arrayModelBarangs = arrayModelBarangs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.card_buku, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // mendapatkan posisi item
        DataBuku modelBuku = arrayModelBarangs.get(position);

        // menset data
        // holder.id_buku.setText(modelBuku.getId());
        holder.buku.setText(modelBuku.getBuku());
        holder.penulis.setText(modelBuku.getPenulis());
        holder.penerbit.setText(modelBuku.getPenerbit());
        holder.jenis.setText(modelBuku.getJenis());
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayModelBarangs.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView id_buku,penulis,penerbit,jenis,buku;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_buku = itemView.findViewById(R.id.tv_id);
            buku = itemView.findViewById(R.id.tv_buku);
            penulis = itemView.findViewById(R.id.tv_penulis);
            penerbit = itemView.findViewById(R.id.tv_penerbit);
            jenis = itemView.findViewById(R.id.tv_jenis);

            // mendeklarasi item ketika diklik
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mendapatkan posisi pada adapter
                    int position = getAdapterPosition();
                    // mengambil posisi pada arraymodelbarang
                    DataBuku arraybuku = arrayModelBarangs.get(position);

                    int hasil_pos = position+1;

//                    String[] pilihan = {"Edit", "Delete"};

                    Intent intent = new Intent(context, PinjemBuku.class);
                    intent.putExtra("ed_id", arraybuku.getId());
                    intent.putExtra("ed_buku", arraybuku.getBuku());
                    intent.putExtra("ed_penulis", arraybuku.getPenulis());
                    intent.putExtra("ed_penerbit", arraybuku.getPenerbit());
                    intent.putExtra("ed_jenis", arraybuku.getJenis());
                    context.startActivity(intent);

                }
            });
        }
    }
}