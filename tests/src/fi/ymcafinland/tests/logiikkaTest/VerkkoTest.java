package fi.ymcafinland.tests.logiikkaTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.tests.testauksenApuluokat.GdxHeadlessTestaus;

import static org.junit.Assert.assertTrue;

/**
 * Created by xvixvi on 23.3.2016.
 */

@RunWith(GdxHeadlessTestaus.class)
public class VerkkoTest {

    private Verkko verkko;

    @Before
    public void setVerkko() throws Exception {
        verkko = new Verkko(SelviytyjanPurjeet.TAUSTAN_LEVEYS, SelviytyjanPurjeet.TAUSTAN_KORKEUS);
    }

    @Test
    public void verkossaSolmujaJaSisaruksia() {
        ArrayList<Solmu> solmut = verkko.getSolmut();
        assertTrue(solmut != null);

        //tarkistetaan, että jokaisella solmulla on molemmat sisarukset
        for (Solmu s : solmut) {
            assertTrue(s.getOikeaSisarus() != null);
            assertTrue(s.getVasenSisarus() != null);
        }
    }

    //Todo testejä verkolle
}