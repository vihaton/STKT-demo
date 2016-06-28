package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.MasterSkin;

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
    private Vaittamat vaittamat;

    Sprite sprite;

    public SolmunKasittelija(Stage stage, Verkko verkko, Skin masterSkin, Pelaaja pelaaja, Vaittamat vaittamat) {
        this.stage = stage;
        solmut = verkko.getSolmut();
        skin = masterSkin;
        this.vaittamat = vaittamat;
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
            if (s.getID().equals("0")) continue;

            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            Image tausta = luoTaustapallo();
            tausta.setOrigin(Align.center);
            
            Table tekstit = luoTekstitaulukko(s);
            Table pallontaulukko = new Table();

            tekstit.setPosition(x, y);
            asetaTauluSolmujenPaikalle(s, x, y, pallontaulukko);

            pallontaulukko.add(tausta).minSize(pallonLeveys, pallonKorkeus);

            Table glowiTaulu = new Table();
            Image glowimage = luoGlowKuva("glow");
            glowimage.setOrigin(Align.center);
            glowiTaulu.add(glowimage).minSize(pallonLeveys * 1.27f, pallonKorkeus * 1.27f);

            final int solmunID = Integer.parseInt(s.getID());

            //Nää iffit refaktoroidaan varmaan rakennemuutoksen yhteydessä
            if (solmunID < 7) {
                glowimage.setColor(glowimage.getColor().sub(pelaaja.getSelviytymisenVari(solmunID - 1)));
                glowimage.setScale(1.15f);
                asetaTauluSolmujenPaikalle(s, x, y, glowiTaulu);
            }
            if (solmunID < 25 && solmunID > 6) {
                asetaTauluSolmujenPaikalle(s, x, y, glowiTaulu);
                glowKuvaTaulukot.add(glowiTaulu);
            }
            if (solmunID < 25) {
                solmuKuvaTaulukot.add(pallontaulukko);
            }

            solmuTaulukot.add(tekstit);
            stage.addActor(pallontaulukko);
            stage.addActor(tekstit);
            stage.addActor(glowiTaulu);
        }
        paivitaGlowAnimaatiot();
    }

    private void asetaTauluSolmujenPaikalle(Solmu s, float x, float y, Table glowiTaulu) {
        glowiTaulu.setOrigin(Align.center);
        glowiTaulu.setPosition(x, y);
        glowiTaulu.setRotation(s.getKulma());
    }
//ToDo parempi glow kuva.
public void paivitaGlowAnimaatiot() {
        int nykyisenSolmunID = 7;
        for (Table t : glowKuvaTaulukot) {
            for (Action a : t.getActions()) {
                t.removeAction(a);
            }
            float vastausProsentti = vaittamat.getVastausprosenttiSolmusta("" + nykyisenSolmunID);
            if (vastausProsentti == 1) { //Jos kaikkeen vastattu, tyhjennetään Glowkuva -table ja lisätään eri glowkuva eri animaatiolla
                //Todo valmiita glowanimaatioita ei luoda joka kerta uudelleen
                t.clear();
                Image glowimage = luoGlowKuva("glowReady");
                glowimage.setOrigin(Align.center);
                t.add(glowimage).minSize(pallonLeveys * 1.27f, pallonKorkeus * 1.27f);
                t.addAction(Actions.forever(Actions.alpha(1f)));
                t.addAction(Actions.forever(Actions.rotateBy(2, 0.005f)));
            } else {
                float solmunAlpha = 0.6f * vastausProsentti + 0.1f;
                t.setTransform(true);
                t.addAction(Actions.forever(Actions.sequence(Actions.alpha(solmunAlpha, 1f), Actions.alpha((solmunAlpha + 0.3f), 1f))));
                t.addAction(Actions.forever(Actions.rotateBy(2, 0.25f)));
                t.addAction(Actions.forever(Actions.sequence(Actions.scaleTo(1.02f, 1.02f, 1.5f), Actions.scaleTo(1, 1, 1.5f))));
            }
            nykyisenSolmunID++;
        }
    }

    private Image luoGlowKuva(String asset) {
        Texture glow = skin.get(asset, Texture.class);
        TextureRegion region = new TextureRegion(glow, 0, 0, glow.getWidth(), glow.getHeight());
        return new Image(region);
    }


    private Table luoTekstitaulukko(Solmu s) {
        Label otsikko = new Label(s.getOtsikko(), skin, "otsikko");
        otsikko.setFontScale(0.7f);
        Label sisalto = new Label(s.getSisalto(), skin, "sisalto");
        sisalto.setFontScale(MasterSkin.HIERO_FONT_SCALE - 0.03f);

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
        int i = 0;
        for(Table t : solmuKuvaTaulukot) {
            t.setTransform(true);
            t.setScale(pelaaja.getSelviytymisprosentit(i) * 0.07f);
            i++;
        }
    }

    public void rotateTables(float angleToPointCamera) {
        for (Table t : solmuTaulukot) {
            t.setTransform(true);
            t.setRotation(angleToPointCamera - 90);
        }
    }

}
