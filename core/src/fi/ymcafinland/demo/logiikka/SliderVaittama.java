package fi.ymcafinland.demo.logiikka;

/**
 * Created by xvixvi on 16.4.2016.
 */
public class SliderVaittama implements Vaittama {

    private String teksti;
    private String solmunID;
    private int mihinSelviytymiskeinoonVaikuttaa;
    private float arvo;
    private boolean checked;
    private boolean antiVaittama;

    public SliderVaittama(String txt, String solmunID, boolean antiVaittama) {
        this.teksti = txt;
        this.solmunID = solmunID;
        asetaVaikutusSelviytyjaan(Integer.parseInt(solmunID));
        arvo = 0f;
        this.antiVaittama = antiVaittama;
        checked = false;
    }

    private void asetaVaikutusSelviytyjaan(int id) {
        this.mihinSelviytymiskeinoonVaikuttaa = (id - 7) / 3;
    }

    /**
     * @return sen ensimmäisen tason solmun id, mihin tämä väittämä vaikuttaa
     */
    public int getMihinSelviytymiskeinoonVaikuttaa() {
        return mihinSelviytymiskeinoonVaikuttaa;
    }

    public String getVaittamanTeksti() {
        return teksti;
    }

    public String getSolmunID() {
        return solmunID;
    }

    public float getArvo() {
        return this.arvo;
    }

    public void setArvo(float arvo) {
        if (antiVaittama) {
            arvo = -arvo;
        }
        this.arvo = arvo;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }
}
