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
        pelaaja.lisaaAlyllista(1);
        Assert.assertEquals(pelaaja.getAlyllinen(), 1f);
    }

}
