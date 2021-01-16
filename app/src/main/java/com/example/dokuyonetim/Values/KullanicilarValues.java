package com.example.dokuyonetim.Values;

public class KullanicilarValues {
    private String adSoyad;
    private String email;



    public KullanicilarValues() {

    }
    public String getAdSoyad() {
        return adSoyad;
    }

    public String getEmail() {
        return email;
    }


    public KullanicilarValues(String adSoyad, String email) {
        this.adSoyad = adSoyad;
        this.email = email;
    }



}
