package fi.ymcafinland.demo.tests.logiikkaTest;

import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

import logiikka.Solmu;
import logiikka.Verkko;

public class VerkkoTest {

    private Verkko verkko;

    @Before
    public void setUp() throws Exception {
        verkko = new Verkko();
    }

    @Test
    public void testVerkkoTest() {
        ArrayList<Solmu> solmut = verkko.getSolmut();
        assertTrue(solmut != null);

        //tarkistetaan, että jokaisella solmulla on molemmat sisarukset
        for (Solmu s : solmut) {
            assertTrue(s.getOikeaSisarus() != null);
            assertTrue(s.getVasenSisarus() != null);
        }
    }

}