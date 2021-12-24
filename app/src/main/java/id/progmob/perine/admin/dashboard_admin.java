package id.progmob.perine.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.progmob.perine.R;
import id.progmob.perine.MainActivity;

public class dashboard_admin extends AppCompatActivity implements View.OnClickListener {
    private CardView buku,anggota,Transaksi,aboutus;
    private Button btn_logout;
    public final static String TAG_ID = "id_user";
    public final static String TAG_NAMA = "nama_user";
//    public final static String TAG_NIM = "nim";
    public final static String TAG_EMAIL = "email";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        buku = (CardView) findViewById(R.id.daftarbuku);
        anggota = (CardView) findViewById(R.id.daftaranggota);
        Transaksi = (CardView) findViewById(R.id.transaksi);
        aboutus = (CardView) findViewById(R.id.tentangkami);
        btn_logout = (Button) findViewById(R.id.buttonLogout);
        buku.setOnClickListener(this);
        anggota.setOnClickListener(this);
        Transaksi.setOnClickListener(this);
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
//                editor.putString(TAG_NIM, null);
                editor.putString(TAG_EMAIL, null);
                editor.commit();

                Intent ua = new Intent(dashboard_admin.this, MainActivity.class);
                finish();
                startActivity(ua);

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.daftarbuku : intent = new Intent(dashboard_admin.this, daftarbuku_admin.class);startActivity(intent);break;
            case R.id.daftaranggota : intent = new Intent(dashboard_admin.this, daftaranggota_admin.class);startActivity(intent);break;
            case R.id.transaksi : intent = new Intent(dashboard_admin.this, transaksi.class);startActivity(intent);break;
            case R.id.tentangkami : intent = new Intent(dashboard_admin.this, daftaradmin.class);startActivity(intent);break;
            default:break;
        }
    }
}