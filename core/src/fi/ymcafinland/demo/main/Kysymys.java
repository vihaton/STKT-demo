package fi.ymcafinland.demo.main;

/**
 * Created by jwinter on 29.3.2016.
 *
 * Kysymys luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class Kysymys {

    private SelviytyjanPurjeet sp;
    private int width;
    private int height;

    public Kysymys(SelviytyjanPurjeet sp) {
        this.sp = sp;
        this.width = sp.V_WIDTH;
        this.height = sp.V_HEIGHT;
    }

    public void render() {

    }

    /**
     * sendData lähettää saadun datan eteenpäin. sendData konfirmoi kysymykseen laitetun tiedon, ja
     * kutsuu tiedon lähettämisen jälkeen dispose -metodia.
     */
    public void sendData() {

    }

    /**
     * Dispose metodi sulkee kysymyksen asettamalla selviytyjän purjeisiin kysymykseksi null. Oletettavasti
     * dispose lähettää ensin keräämänsä datan eteenpäin.
     */
    public void dispose() {
        this.sp.setKysymys(null);
    }

}