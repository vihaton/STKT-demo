package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by xvixvi on 1.5.2016.
 */
public class EdistymismittarinKasittelija {

    private ProgressBar progressBar;
    private Table progressTable;
    private Skin skin;
    private Stage stage;
    private Pelaaja pelaaja;

    public EdistymismittarinKasittelija(Stage stage, Skin masterSkin, Pelaaja pelaaja) {
        this.skin = masterSkin;
        this.stage = stage;
        this.pelaaja = pelaaja;

        luoProgressTable();
//        luoDebugTaulukko();
    }

    /**
     * Luo tyhjän taulukon, joka asetetaan keskipisteeseen. Helpottaa debuggaamista, kun näkee missä
     * on peliavaruuden y- ja x-akselit sekä keskipisteen paikan.
     */
    private void luoDebugTaulukko() {
        Table aputable = new Table();
        aputable.setSize(100, 50);
        aputable.setPosition(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2);
        stage.addActor(aputable);
    }

    public void luoProgressTable() {
        progressBar = new ProgressBar(0, 100, 1, false, skin.get("progressBarStyle", ProgressBar.ProgressBarStyle.class));
        progressBar.setValue(0);

        Label otsikko = new Label("Edistymismittari:", skin, "otsikko");
        otsikko.setAlignment(Align.center);

        this.progressTable = new Table();
        progressTable.setBackground(new TextureRegionDrawable(new TextureRegion(skin.get("gray", Texture.class))));
        progressTable.add(otsikko).row();
        progressTable.add(progressBar).minWidth(SelviytyjanPurjeet.V_WIDTH * 0.9f);
        progressTable.setBounds(0, 0, progressTable.getPrefWidth(), progressTable.getPrefHeight()); //Ilman tätä tausta ei renderöidy

        progressTable.setOrigin(Align.center);        //siirtää taulukon "origoa" suhteessa taulukon vasempaan alakulmaan. Kiertäminen ja skaalaaminen tehdään suhteessa origoon.

        float x = (SelviytyjanPurjeet.TAUSTAN_LEVEYS - progressTable.getWidth()) / 2;
        float y = (SelviytyjanPurjeet.TAUSTAN_KORKEUS - progressTable.getHeight()) / 2;
        progressTable.setPosition(x, y);        //asettaa taulukon VASEMMAN ALAKULMAN (ei siis riipu originin muuttamisesta!!) sijainnin

        stage.addActor(progressTable);
    }

    public void pyoritaMittaria(float angleToPoint) {
        progressTable.setTransform(true);
        progressTable.setRotation(angleToPoint - 90);
    }

    public void paivitaMittarinArvo(float delta) {
        progressBar.setValue(pelaaja.getVastausprosentti());
        progressBar.act(delta);
    }
}
