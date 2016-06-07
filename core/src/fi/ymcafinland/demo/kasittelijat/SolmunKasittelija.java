package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private ArrayList<Table> glowKuvaTaulukot;
    private Pelaaja pelaaja;


    Sprite sprite;

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
        glowKuvaTaulukot = new ArrayList<>();
        lisaaSolmutStageen();
        Texture glow = skin.get("glow", Texture.class);
        sprite = new Sprite(glow);
    }

    private void lisaaSolmutStageen() {
        for (Solmu s : solmut) {
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            Image taustapallo = luoTaustapallo();
            taustapallo.setOrigin(Align.center);

            Table tekstit = luoTekstitaulukko(s);
            Table pallontaulukko = new Table();

            tekstit.setPosition(x, y);
            asetaTauluSolmujenPaikalle(s, x, y, pallontaulukko);

            pallontaulukko.add(taustapallo).minSize(pallonLeveys, pallonKorkeus);

            Table glowiTaulu = new Table();
            Image glowimage = luoGlowKuva();
            glowimage.setOrigin(Align.center);
            asetaTauluSolmujenPaikalle(s, x, y, glowiTaulu);


            glowiTaulu.add(glowimage).minSize(pallonLeveys*1.27f, pallonKorkeus*1.27f);



            if(Integer.parseInt(s.getID()) < 25)
                solmuKuvaTaulukot.add(pallontaulukko);

            if(Integer.parseInt(s.getID()) < 25 && Integer.parseInt(s.getID())> 6) {
                glowKuvaTaulukot.add(glowiTaulu);
            }

            solmuTaulukot.add(tekstit);
            stage.addActor(pallontaulukko);
            stage.addActor(tekstit);
            stage.addActor(glowiTaulu);
        }
        luoGlowAnimaatiot();
    }

    private void asetaTauluSolmujenPaikalle(Solmu s, float x, float y, Table glowiTaulu) {
        glowiTaulu.setOrigin(Align.center);
        glowiTaulu.setPosition(x, y);
        glowiTaulu.setRotation(s.getKulma());
    }
//ToDo parempi glow kuva.
    private void luoGlowAnimaatiot() {

        for (Table t : glowKuvaTaulukot) {
            t.setTransform(true);
            t.addAction(Actions.forever(Actions.sequence(Actions.alpha(0.7f, 0.5f), Actions.alpha(1f, 0.5f))));
            t.addAction(Actions.forever(Actions.rotateBy(2, 0.25f)));
            t.addAction(Actions.forever(Actions.sequence(Actions.scaleTo(1.02f, 1.02f, 1.5f), Actions.scaleTo(1, 1, 1.5f))));
//            t.addAction(Actions.forever(Actions.sequence(Actions.moveBy(2, 2, 1.5f), Actions.moveBy(-2, -2, 1.5f))));
//            t.addAction(Actions.forever(Actions.sequence(Actions.moveBy(-2, 1, 2.5f), Actions.moveBy(1, -2, 2.5f))));
        }
    }

    private Image luoGlowKuva() {
        Texture glow = skin.get("glow", Texture.class);
        TextureRegion region = new TextureRegion(glow, 0, 0, glow.getWidth(), glow.getHeight());
        return new Image(region);
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
