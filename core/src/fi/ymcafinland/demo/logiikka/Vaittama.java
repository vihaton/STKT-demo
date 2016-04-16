package fi.ymcafinland.demo.logiikka;

/**
 * Created by xvixvi on 16.4.2016.
 */
public class Vaittama {

    private String teksti;
    private String solmunID;

    public Vaittama(String txt, String solmunID) {
        this.teksti = txt;
        this.solmunID = solmunID;
    }

    public String getTeksti() {
        return teksti;
    }

    public String getSolmunID() {
        return solmunID;
    }
}
