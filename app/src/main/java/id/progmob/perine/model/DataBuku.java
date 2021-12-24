package id.progmob.perine.model;

public class DataBuku {
    private String  buku, penulis, penerbit, jenis;
    Integer id_buku;
//    id_buku,
    public DataBuku() {
    }

    public DataBuku(Integer id_buku, String buku, String penulis, String penerbit, String jenis) {
        this.id_buku = id_buku;
        this.buku = buku;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.jenis = jenis;
    }

    public Integer getId() {
        return id_buku;
    }

    public void setId(Integer id_buku) {
        this.id_buku = id_buku;
    }

    public String getBuku() {
        return buku;
    }

    public void setBuku(String buku) {
        this.buku = buku;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
