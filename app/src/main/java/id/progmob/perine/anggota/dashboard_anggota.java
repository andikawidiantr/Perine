package id.progmob.perine.anggota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.progmob.perine.MainActivity;
import id.progmob.perine.R;
import id.progmob.perine.admin.daftaranggota_admin;
import id.progmob.perine.admin.daftarbuku_admin;
import id.progmob.perine.admin.dashboard_admin;

public class dashboard_anggota extends AppCompatActivity implements View.OnClickListener {
    private CardView buku,pinjam,editprofile,aboutus;
    private Button btn_logout;
    public final static String TAG_ID = "id_user";
    public final static String TAG_NAMA = "nama_user";
    //    public final static String TAG_NIM = "nim";
    public final static String TAG_EMAIL = "email";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_anggota);

        buku = (CardView) findViewById(R.id.daftarbuku);
        pinjam = (CardView) findViewById(R.id.riwayatpinjam);
        editprofile = (CardView) findViewById(R.id.edit_profile);
        aboutus = (CardView) findViewById(R.id.tentangkami);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        buku.setOnClickListener(this);
        editprofile.setOnClickListener(this);
        pinjam.setOnClickListener(this);
        aboutus.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(MainActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_NAMA, null);
//                editor.putString(T, null);
                editor.putString(TAG_EMAIL, null);
                editor.commit();

                Intent ua = new Intent(dashboard_anggota.this, MainActivity.class);
                finish();
                startActivity(ua);

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.daftarbuku : intent = new Intent(dashboard_anggota.this, daftarbuku_anggota.class);startActivity(intent);break;
            case R.id.riwayatpinjam : intent = new Intent(dashboard_anggota.this, riwayatpeminjaman.class);startActivity(intent);break;
            case R.id.edit_profile : intent = new Intent(dashboard_anggota.this,editprofile.class);startActivity(intent);break;
            case R.id.tentangkami : intent = new Intent(dashboard_anggota.this, aboutus.class);startActivity(intent);break;
            default:break;
        }
    }
}