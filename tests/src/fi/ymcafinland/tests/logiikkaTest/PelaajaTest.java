package fi.ymcafinland.tests.logiikkaTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.tests.GdxTestaus;

/**
 * Created by Sasu on 11.4.2016.
 */
@RunWith(GdxTestaus.class)

public class PelaajaTest {
private Pelaaja pelaaja;

    @Before
    public void luoPelaaja() throws Exception {
        pelaaja = new Pelaaja();
    }
    @Test
    public void lisaysPositiivisellaTest(){
        float alkuarvo = pelaaja.getAlyllinen();
        pelaaja.lisaaSelviytymisarvoIndeksissa(1, 1);
        Assert.assertEquals(pelaaja.getAlyllinen(), alkuarvo + 1f);
    }

    //Todo testejä pelaajalle

}
