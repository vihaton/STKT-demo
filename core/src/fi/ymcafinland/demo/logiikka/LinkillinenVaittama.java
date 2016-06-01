package fi.ymcafinland.demo.logiikka;

/**
 * Created by xvixvi on 1.6.2016.
 */
public class LinkillinenVaittama implements Vaittama {

    private Vaittama vaittama;
    private String linkki;

    public LinkillinenVaittama(Vaittama vaittama, String linkki) {
        this.vaittama = vaittama;
        this.linkki = linkki;
    }

    public String getLinkki() {
        return linkki;
    }

    @Override
    public void setArvo(float uusiArvo) {
        vaittama.setArvo(uusiArvo);
    }

    @Override
    public float getVaikuttavaArvo() {
        return vaittama.getVaikuttavaArvo();
    }

    @Override
    public float getNakyvaArvo() {
        return vaittama.getNakyvaArvo();
    }

    @Override
    public String getVaittamanTeksti() {
        return vaittama.getVaittamanTeksti();
    }

    @Override
    public int getMihinSelviytymiskeinoonVaikuttaa() {
        return vaittama.getMihinSelviytymiskeinoonVaikuttaa();
    }
}
