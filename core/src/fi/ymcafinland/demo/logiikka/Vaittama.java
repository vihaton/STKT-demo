package fi.ymcafinland.demo.logiikka;

/**
 * Created by xvixvi on 16.4.2016.
 */
public class Vaittama {

    private String teksti;
    private String solmunID;
    private int mihinSelviytymiskeinoonVaikuttaa;
    private float arvo;
    private boolean checked;

    public Vaittama(String txt, String solmunID) {
        this.teksti = txt;
        this.solmunID = solmunID;
        asetaVaikutusSelviytyjaan(Integer.parseInt(solmunID));
        arvo = 0f;
        checked = false;
    }

    private void asetaVaikutusSelviytyjaan(int id) {
        this.mihinSelviytymiskeinoonVaikuttaa = (id - 7) / 3;
    }

    public int getMihinSelviytymiskeinoonVaikuttaa() {
        return mihinSelviytymiskeinoonVaikuttaa;
    }

    public String getTeksti() {
        return teksti;
    }

    public String getSolmunID() {
        return solmunID;
    }

    public float getArvo() {
        return this.arvo;
    }

    public void setArvo(float arvo) {
        this.arvo = arvo;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }
}
