package fi.ymcafinland.demo.logiikka;

/**
 * Created by Sasu on 11.4.2016.
 */
public class Pelaaja {

    private float fyysinen;
    private float eettinen;
    private float luova;
    private float alyllinen;
    private float sosiaalinen;
    private float tunteellinen;

    /**
     * Pelaajalla on selviytymisarvosanat joihin vaikutetaan vastaamalla väittämiin
     */

    public Pelaaja(){
        this.alyllinen =0f;
        this.eettinen =0f;
        this.fyysinen = 0f;
        this.luova = 0f;
        this.sosiaalinen = 0f;
        this.tunteellinen = 0f;
    }
    public void lisaaEettista(float maara){
        this.eettinen += maara;
    }
    public void lisaaAlyllista(float maara){
        this.alyllinen += maara;
    }
    public void lisaaFyysista(float maara){
        this.fyysinen += maara;
    }
    public void lisaaLuovuutta(float maara){
        this.luova += maara;
    }
    public void lisaaSosiaalista(float maara){
        this.sosiaalinen += maara;
    }
    public void lisaaTunteellista(float maara){
        this.tunteellinen += maara;
    }
    public float getEettinen() {
        return eettinen;
    }

    public void setEettinen(float eettinen) {
        this.eettinen = eettinen;
    }
    public float getFyysinen() {
        return fyysinen;
    }

    public void setFyysinen(float fyysinen) {
        this.fyysinen = fyysinen;
    }

    public float getLuova() {
        return luova;
    }

    public void setLuova(float luova) {
        this.luova = luova;
    }

    public float getAlyllinen() {
        return alyllinen;
    }

    public void setAlyllinen(float alyllinen) {
        this.alyllinen = alyllinen;
    }

    public float getSosiaalinen() {
        return sosiaalinen;
    }

    public void setSosiaalinen(float sosiaalinen) {
        this.sosiaalinen = sosiaalinen;
    }

    public float getTunteellinen() {
        return tunteellinen;
    }

    public void setTunteellinen(float tunteellinen) {
        this.tunteellinen = tunteellinen;
    }
}
