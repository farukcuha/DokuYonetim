package com.example.dokuyonetim.Values;

public class UrunlerValues {
    private String ürünAciklama;
    private String ürünAdi;
    private String ürünFiyati;
    private String ürünResim;
    private String ürünSatisTuru;
    private String ürünTahminiKargo;

    public UrunlerValues() {
    }

    public UrunlerValues(String ürünAciklama, String ürünAdi, String ürünFiyati, String ürünResim, String ürünSatisTuru, String urunTahminiKargo) {
        this.ürünAciklama = ürünAciklama;
        this.ürünAdi = ürünAdi;
        this.ürünFiyati = ürünFiyati;
        this.ürünResim = ürünResim;
        this.ürünSatisTuru = ürünSatisTuru;
        this.ürünTahminiKargo = urunTahminiKargo;
    }

    public String getÜrünAciklama() {
        return ürünAciklama;
    }

    public String getÜrünAdi() {
        return ürünAdi;
    }

    public String getÜrünFiyati() {
        return ürünFiyati;
    }

    public String getÜrünResim() {
        return ürünResim;
    }

    public String getÜrünSatisTuru() {
        return ürünSatisTuru;
    }

    public String getUrunTahminiKargo() {
        return ürünTahminiKargo;
    }
}
