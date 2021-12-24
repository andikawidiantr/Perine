package id.progmob.perine.model;

public class DataAnggota {
    private String  email, pass, nama_user, jenis_kelamin, role;
    String id_user;
    public DataAnggota() {
    }

    public DataAnggota(String id_user, String email, String pass, String nama_user, String jenis_kelamin, String role) {
        this.id_user = id_user;
        this.email = email;
        this.pass = pass;
        this.nama_user = nama_user;
        this.jenis_kelamin = jenis_kelamin;
        this.role = role;
    }

    public String getId() {
        return id_user;
    }

    public void setId(String id_user) {
        this.id_user= id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

