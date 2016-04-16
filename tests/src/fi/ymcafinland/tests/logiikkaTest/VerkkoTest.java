package fi.ymcafinland.tests.logiikkaTest;

import org.junit.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import fi.ymcafinland.tests.GdxTestaus;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;

import static org.junit.Assert.*;

/**
 * Created by xvixvi on 23.3.2016.
 */

@RunWith(GdxTestaus.class)
public class VerkkoTest {

        private Verkko verkko;

        @Before
        public void setVerkko() throws Exception {
                verkko = new Verkko(2048, 2048);
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