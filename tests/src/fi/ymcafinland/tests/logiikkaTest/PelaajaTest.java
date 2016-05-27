package fi.ymcafinland.tests.logiikkaTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.tests.testauksenApuluokat.GdxHeadlessTestaus;

/**
 * Created by Sasu on 11.4.2016.
 */
@RunWith(GdxHeadlessTestaus.class)

public class PelaajaTest {
    private Pelaaja pelaaja;

    @Before
    public void luoPelaaja() throws Exception {
        pelaaja = new Pelaaja();
    }

    @Test
    @Ignore
    public void lisaysPositiivisellaTest() {
        float alkuarvo = pelaaja.getSelviytymisprosentit(Pelaaja.ALYLLINEN);
        pelaaja.lisaaSelviytymisarvoIndeksissa(Pelaaja.ALYLLINEN, 1);
        Assert.assertEquals(pelaaja.getSelviytymisprosentit(Pelaaja.ALYLLINEN), alkuarvo + 1f);
    }

    @Test
    public void getVastausprosenttiToimii() {
        pelaaja.setVaittamienMaara(100);
        pelaaja.lisaaVastauksia(50);
        Assert.assertEquals(50, pelaaja.getVastausprosentti());

        pelaaja.setVaittamienMaara(101);
        Assert.assertEquals(50, pelaaja.getVastausprosentti()); //ylöspäin pyöristys

        pelaaja.setVaittamienMaara(102);
        Assert.assertEquals(49, pelaaja.getVastausprosentti()); //alaspäin pyöristys

        pelaaja.setVaittamienMaara(54);
        Assert.assertEquals(93, pelaaja.getVastausprosentti()); //ylöspäin pyöristys
    }

    @Test
    @Ignore
    public void getSelviytymiskeinotJarjestyksessaToimii() {
        String[] selviytymiskeinot = pelaaja.getSelviytymiskeinot();
        for (int i = 0; i < 6; i++) {
            pelaaja.lisaaSelviytymisarvoIndeksissa(i, i);
            ArrayList<String> jarjestetytKeinot = pelaaja.getSelviytymiskeinotJarjestyksessa();
            Assert.assertTrue("Ensimmäisenä keinona oli " + jarjestetytKeinot.get(0) + ", olisi pitänyt olla " + selviytymiskeinot[i],
                    selviytymiskeinot[i].equalsIgnoreCase(jarjestetytKeinot.get(0)));
        }
    }
    //Todo testejä pelaajalle

}
