package fi.ymcafinland.demo.logiikka;

/**
 * Created by xvixvi on 1.6.2016.
 * <p/>
 * Väittämällä täytyy olla arvo, väittämän teksti sekä sen pitää tietää, mihin selviytymiskeinoon se vaikuttaa.
 * <p/>
 * Tämä toteutus tarkoittaa, että yksi väittämä vaikuttaa vain yhteen selviytymiskeinoon positiivisesti tai negatiivisesti.
 */
public interface Vaittama {

    void setArvo(float uusiArvo);

    float getVaikuttavaArvo();

    float getNakyvaArvo();

    String getVaittamanTeksti();

    int getMihinSelviytymiskeinoonVaikuttaa();
}
