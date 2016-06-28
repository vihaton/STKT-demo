package fi.ymcafinland.demo.logiikka;

/**
 * Created by xvixvi on 16.4.2016.
 */
public class SliderVaittama implements Vaittama {

    private String teksti;
    private String solmunID;
    private int mihinSelviytymiskeinoonVaikuttaa;
    private float arvo;
    private boolean antiVaittama;
    private boolean checked;

    public SliderVaittama(String txt, String solmunID, boolean antiVaittama) {
        this.teksti = txt;
        this.solmunID = solmunID;
        asetaVaikutusSelviytyjaan(Integer.parseInt(solmunID));
        arvo = 0f;
        this.antiVaittama = antiVaittama;
    }
    public String getID(){
        return solmunID;
    }
    private void asetaVaikutusSelviytyjaan(int id) {
        if(id == -1){
            return;
        }
        this.mihinSelviytymiskeinoonVaikuttaa = (id - 7) / 3;
    }

    /**
     * @return sen ensimmäisen tason solmun id, mihin tämä väittämä vaikuttaa
     */
    public int getMihinSelviytymiskeinoonVaikuttaa() {
        return mihinSelviytymiskeinoonVaikuttaa;
    }

    @Override
    public boolean getChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getVaittamanTeksti() {
        return teksti;
    }

    public String getSolmunID() {
        return solmunID;
    }

    public float getVaikuttavaArvo() {
        if (antiVaittama) return -arvo;
        return this.arvo;
    }

    public float getNakyvaArvo() {
        return arvo;
    }

    public void setArvo(float arvo) {
        this.arvo = arvo;
    }
}
