package id.progmob.perine.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLInput;
import java.util.ArrayList;

import id.progmob.perine.model.DataPinjam;

public class db_pinjam extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db_pinjam";
    public static final int DATABASE_VERSION = 1;
    public static final String TABEL_NAME = "tb_peminjaman";
    public static final String id_peminjaman = "id_peminjaman";
    public static final String id_user = "id_user";
    public static final String id_buku = "id_buku";
    public static final String nama_user = "nama_user";
    public static final String nama_buku = "nama_buku";
    public static final String tanggal_pinjam = "tanggal_pinjam";
    public static final String tanggal_kembali = "tanggal_kembali";
    public static final String status = "status";
    private  SQLiteDatabase db;

    public db_pinjam (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query ="CREATE TABLE " + TABEL_NAME + "("
                + id_peminjaman + " INTEGER PRIMARY KEY,"
                + id_user + " INTEGER,"
                + id_buku + " INTEGER,"
                + nama_buku + " TEXT,"
                + nama_user + " TEXT,"
                + tanggal_pinjam + " DATETIME,"
                + tanggal_kembali + " DATETIME,"
                + status + " TEXT)";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists "+TABEL_NAME+" " ;
        db.execSQL(query);
        onCreate(db);
    }

    public void insertRiwayatPeminjamans(ArrayList<DataPinjam> arrDataPinjam){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABEL_NAME, null, null);
        for(int i=0;i<arrDataPinjam.size();i++){
            ContentValues values=new ContentValues();
            values.put(id_user,arrDataPinjam.get(i).getId_user());
            values.put(id_buku,arrDataPinjam.get(i).getId_bukupinjam());
            values.put(nama_user,arrDataPinjam.get(i).getNama_user());
            values.put(nama_buku,arrDataPinjam.get(i).getNama_buku());
            values.put(tanggal_kembali,arrDataPinjam.get(i).getTanggal_kembali());
            values.put(tanggal_pinjam,arrDataPinjam.get(i).getTanggal_pinjam());
            values.put(status,arrDataPinjam.get(i).getStatus());
            db.insert(TABEL_NAME,null,values);
        }
    }

    public ArrayList<DataPinjam> getRiwayatPeminjaman(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<DataPinjam> data=new ArrayList<DataPinjam>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABEL_NAME,null);
        if(cursor.moveToFirst()){
            do{
                data.add(new DataPinjam(
                        cursor.getInt(cursor.getColumnIndex(id_peminjaman)),
                        cursor.getInt(cursor.getColumnIndex(id_user)),
                        cursor.getInt(cursor.getColumnIndex(id_buku)),
                        cursor.getString(cursor.getColumnIndex(nama_user)),
                        cursor.getString(cursor.getColumnIndex(nama_buku)),
                        cursor.getString(cursor.getColumnIndex(status)),
                        cursor.getString(cursor.getColumnIndex(tanggal_pinjam)),
                        cursor.getString(cursor.getColumnIndex(tanggal_kembali))
                        ));
            }while(cursor.moveToNext());
        }
        return data;
    }
    public void insertData(ContentValues values){
        db.insert(TABEL_NAME,null,values);
    }
//    public void updateData(ContentValues values, long id){
//        db.update(tabel_biodata, values, nim + "=" + id, null);
//    }
//    //    public void updateData(ContentValues values, long id){
////        db.update(table_name,values,row_id + "=" + id, null);
////    }
//    public void deleteData(long id){
//        db.delete(tabel_biodata, nim + "=" + id,null);
//    }
//    public Cursor getAllData(){
//        return db.query(tabel_biodata,null,null,
//                null,null, null,null);
//    }
//    public Cursor getData(long id){
//        return db.rawQuery("select * from " + tabel_biodata + " where " + nim + "=" + id, null);
//    }
}

