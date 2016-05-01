package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
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


        progressBar = new ProgressBar(0, 100, 1, false, skin.get("progressBarStyle", ProgressBar.ProgressBarStyle.class));

        progressBar.setValue(0);

        Label otsikko = new Label("Edistymismittari:", skin, "otsikko");
        otsikko.setScale(0.7f);
        otsikko.setAlignment(Align.center);

        this.progressTable = new Table();
        progressTable.top().center().add(otsikko);
        progressTable.row();
        //ilmeisesti taulukko käsittelee ProgB. "pisteenä", jonka sijainti on PB:n vasemman alakulman sijainti, eikä esim PB:n keskikohta
        progressTable.add(progressBar);

        progressBar.setFillParent(true);

        //siirtää taulukon "origoa" suhteessa taulukon vasempaan alakulmaan. Esim kiertäminen tehdään suhteessa origoon.
//        progressTable.setOrigin(progBarWidth, progBarHeight);
        //asettaa taulukon vasemman alakulman sijainnin
        progressTable.setPosition(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2);

        stage.addActor(progressTable);
    }

    public void paivitaMittari(float delta, float angleToPoint1) {
        progressBar.setValue(pelaaja.getVastausmaara());
        progressBar.act(delta);

        progressTable.setTransform(true);
        progressTable.setRotation(angleToPoint1 - 90);
    }
}
