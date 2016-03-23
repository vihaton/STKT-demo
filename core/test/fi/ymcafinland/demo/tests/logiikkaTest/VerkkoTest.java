package fi.ymcafinland.demo.tests.logiikkaTest;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

import org.junit.*;

import java.util.ArrayList;
import static org.junit.Assert.*;

import fi.ymcafinland.demo.SelviytyjanPurjeet;
import logiikka.Solmu;
import logiikka.Verkko;

public class VerkkoTest {

    private Verkko verkko;

    @BeforeClass
    public static void initGdx() {
        final HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new SelviytyjanPurjeet(), config);
    }

    @Before
    public void setVerkko() throws Exception {
        verkko = new Verkko();
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

}