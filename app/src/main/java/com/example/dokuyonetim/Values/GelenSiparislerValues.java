package com.example.dokuyonetim.Values;

import java.util.List;

public class GelenSiparislerValues {
     public String kargoTakipNo;
     public String odenenTutar;
     public String siparisDurumu;
     public String siparisNumarasi;
     public String siparisTarihi;

    public GelenSiparislerValues(){

    }


    public GelenSiparislerValues(String kargoTakipNo, String odenenTutar, String siparisDurumu, String siparisNumarasi, String siparisTarihi) {
        this.kargoTakipNo = kargoTakipNo;
        this.odenenTutar = odenenTutar;
        this.siparisDurumu = siparisDurumu;
        this.siparisNumarasi = siparisNumarasi;
        this.siparisTarihi = siparisTarihi;
    }


    public String getKargoTakipNo() {
        return kargoTakipNo;
    }

    public String getOdenenTutar() {
        return odenenTutar;
    }

    public String getSiparisDurumu() {
        return siparisDurumu;
    }

    public String getSiparisNumarasi() {
        return siparisNumarasi;
    }

    public String getSiparisTarihi() {
        return siparisTarihi;
    }


}
