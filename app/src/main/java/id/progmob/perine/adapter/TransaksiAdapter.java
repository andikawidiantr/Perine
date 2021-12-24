package id.progmob.perine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.progmob.perine.R;
import id.progmob.perine.admin.daftarbuku_admin;
import id.progmob.perine.admin.gantistatus;
import id.progmob.perine.anggota.PinjemBuku;
import id.progmob.perine.koneksi;
import id.progmob.perine.model.DataBuku;
import id.progmob.perine.model.DataPinjam;

public class TransaksiAdapter extends RecyclerView.Adapter<id.progmob.perine.adapter.TransaksiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataPinjam> arrayModelPinjam;
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

    private id.progmob.perine.adapter.TransaksiAdapter adapter;

    String tag_json_obj = "json_obj_req";
    // membuat kontruksi recyclerviewadapter
    public TransaksiAdapter(Context context, ArrayList<DataPinjam> arrayModelPinjam) {
        this.context = context;
        this.arrayModelPinjam = arrayModelPinjam;
    }

    @NonNull
    @Override
    public id.progmob.perine.adapter.TransaksiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.card_transaksi, parent, false);
        return new id.progmob.perine.adapter.TransaksiAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull id.progmob.perine.adapter.TransaksiAdapter.ViewHolder holder, int position) {
        // mendapatkan posisi item
        DataPinjam modelPinjam = arrayModelPinjam.get(position);

        // menset data
        // holder.id_buku.setText(modelBuku.getId());
        holder.judul.setText(modelPinjam.getNama_buku());
        holder.tanggal_pinjam.setText(modelPinjam.getTanggal_pinjam());
        holder.tanggal_kembali.setText(modelPinjam.getTanggal_kembali());
        holder.status.setText(modelPinjam.getStatus());
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayModelPinjam.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView judul,tanggal_kembali,tanggal_pinjam, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.rw_judul);
            tanggal_pinjam = itemView.findViewById(R.id.rw_tgl_pinjam);
            tanggal_kembali = itemView.findViewById(R.id.rw_tgl_kembali);
            status = itemView.findViewById(R.id.rw_status);

            // mendeklarasi item ketika diklik
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mendapatkan posisi pada adapter
                    int position = getAdapterPosition();
                    // mengambil posisi pada arraymodelbarang
                    DataPinjam arraypinjam = arrayModelPinjam.get(position);

                    int hasil_pos = position+1;

//                    String[] pilihan = {"Edit", "Delete"};

                    Intent intent = new Intent(context, gantistatus.class);

                    intent.putExtra("ed_id_pinjam", arraypinjam.getId_peminjaman());
                    intent.putExtra("ed_id_buku", arraypinjam.getId_bukupinjam());
                    intent.putExtra("ed_id_user", arraypinjam.getId_user());
                    intent.putExtra("ed_nama_user", arraypinjam.getNama_user());
                    intent.putExtra("ed_nama_buku", arraypinjam.getNama_buku());
                    intent.putExtra("tgl_pinjam", arraypinjam.getTanggal_pinjam());
                    intent.putExtra("tgl_kembali", arraypinjam.getTanggal_kembali());
                    intent.putExtra("ed_status", arraypinjam.getStatus());
                    context.startActivity(intent);




                }
            });
        }
    }
}