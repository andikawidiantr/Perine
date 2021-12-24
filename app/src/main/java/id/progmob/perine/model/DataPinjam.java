package id.progmob.perine.model;

public class DataPinjam {
    private String  nama_user, nama_buku, status,tanggal_kembali, tanggal_pinjam;
    Integer id_buku,id_user,id_peminjaman;

    public DataPinjam() {
    }

    public DataPinjam(Integer id_peminjaman,Integer id_user,Integer id_buku, String nama_user, String nama_buku, String status, String tanggal_pinjam, String tanggal_kembali) {
        this.id_peminjaman = id_peminjaman;
        this.id_buku = id_buku;
        this.id_user = id_user;
        this.nama_user = nama_user;
        this.nama_buku = nama_buku;
        this.status = status;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kembali = tanggal_kembali;
    }

    public Integer getId_peminjaman() {
        return id_peminjaman;
    }

    public void setId_peminjaman(Integer id_peminjaman) {
        this.id_peminjaman = id_peminjaman;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_bukupinjam() {
        return id_buku;
    }

    public void setId_bukupinjam(Integer id_buku) {
        this.id_buku = id_buku;
    }

    public String getNama_buku() {
        return nama_buku;
    }

    public void setNama_buku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public String getTanggal_pinjam() {
        return tanggal_pinjam;
    }

    public void setTanggal_pinjam(String tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }

    public String getTanggal_kembali() {
        return tanggal_kembali;
    }

    public void setTanggal_kembali(String tanggal_kembali) {
        this.tanggal_kembali = tanggal_kembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }
}

