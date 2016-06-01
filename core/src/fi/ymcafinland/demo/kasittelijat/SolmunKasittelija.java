package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;

/**
 * Created by xvixvi on 1.4.2016.
 */
public class SolmunKasittelija {

    private Stage stage;
    private ArrayList<Solmu> solmut;
    private Texture pallonKuva;
    private Skin skin;
    private float pallonLeveys;
    private float pallonKorkeus;
    private ArrayList<Table> solmuTaulukot;
    private ArrayList<Table> solmuKuvaTaulukot;
    private Pelaaja pelaaja;

    public SolmunKasittelija(Stage stage, Verkko verkko, Skin masterSkin, Pelaaja pelaaja) {
        this.stage = stage;
        solmut = verkko.getSolmut();
        skin = masterSkin;
        pallonKuva = skin.get("emptynode", Texture.class);
        pallonLeveys = pallonKuva.getWidth();
        pallonKorkeus = pallonKuva.getHeight();
        this.pelaaja = pelaaja;
        solmuKuvaTaulukot = new ArrayList<>();
        solmuTaulukot = new ArrayList<>();
        lisaaSolmutStageen();
    }

    private void lisaaSolmutStageen() {
        for (Solmu s : solmut) {
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            Image taustapallo = luoTaustapallo();
            Table tekstit = luoTekstitaulukko(s);
            Table pallontaulukko = new Table();

            tekstit.setPosition(x, y);
            pallontaulukko.setPosition(x, y);
            pallontaulukko.setOrigin(Align.center);
            pallontaulukko.setRotation(s.getKulma());

            pallontaulukko.add(taustapallo).minSize(pallonLeveys,pallonKorkeus);
            if(Integer.parseInt(s.getID()) < 7)
                solmuKuvaTaulukot.add(pallontaulukko);
            solmuTaulukot.add(tekstit);

            stage.addActor(pallontaulukko);
            stage.addActor(tekstit);
        }
    }

    private Table luoTekstitaulukko(Solmu s) {
        Label otsikko = new Label(s.getOtsikko(), skin, "otsikko");
        otsikko.setFontScale(0.7f);
        Label sisalto = new Label(s.getSisalto(), skin, "sisalto");
        sisalto.setFontScale(2);

        Table tekstit = new Table();

        if (otsikko.getText().length != 0) { //jos otsikko ei ole tyhjä
            tekstit.add(otsikko);
            tekstit.row();
        }
        tekstit.add(sisalto);
        return tekstit;
    }

    private Image luoTaustapallo() {
        Texture emptynode = skin.get("emptynode", Texture.class);
        TextureRegion region = new TextureRegion(emptynode, 0, 0, emptynode.getWidth(), emptynode.getHeight());

        return new Image(region);
    }

    /**
     * Päivittää solmujen asennot ym.
     * <p/>
     * * @param angleToPointCamera kulma, jolla kamera on suunnattu keskipisteeseen
     */
    public void paivitaSolmut(float angleToPointCamera) {
        rotateTables(angleToPointCamera);
        paivitaSolmujenKoko();
    }

    /**
     * Päivittää ylemmäntason pallojen koot pelaajan selviytymispreferenssin mukaan
     */
    private void paivitaSolmujenKoko() {
        for (int i = 0; i < 6; i++) {
            Table t = solmuKuvaTaulukot.get(i);
            t.setDebug(true);
            t.setTransform(true);
            t.setScale(pelaaja.getSelviytymisprosentit(i)*0.07f);
        }
    }

    public void rotateTables(float angleToPointCamera) {
        for (Table t : solmuTaulukot) {
            t.setTransform(true);
            t.setRotation(angleToPointCamera - 90);
        }
    }

}
