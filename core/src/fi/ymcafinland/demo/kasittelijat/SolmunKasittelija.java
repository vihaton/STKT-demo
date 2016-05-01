package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;

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

    public SolmunKasittelija(Stage stage, Verkko verkko, Skin masterSkin) {
        this.stage = stage;
        solmut = verkko.getSolmut();
        skin = masterSkin;
        pallonKuva = skin.get("emptynode", Texture.class);
        pallonLeveys = pallonKuva.getWidth();
        pallonKorkeus = pallonKuva.getHeight();

        solmuTaulukot = new ArrayList<>();
        lisaaSolmutStageen();
    }

    private void lisaaSolmutStageen() {
        for (Solmu s : solmut) {
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            Image taustapallo = luoTaustapallo();
            Table tekstit = luoTekstitaulukko(s);

            taustapallo.setPosition(x - pallonLeveys / 2, y - pallonKorkeus / 2);
            tekstit.setPosition(x, y);

            taustapallo.setOrigin(Align.center);
            taustapallo.setRotation(s.getKulma());
            solmuTaulukot.add(tekstit);

            stage.addActor(taustapallo);
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
     *
     * * @param angleToPointCamera kulma, jolla kamera on suunnattu keskipisteeseen
     */
    public void paivitaSolmut(float angleToPointCamera) {
        rotateTables(angleToPointCamera);
    }

    public void rotateTables(float angleToPointCamera) {
        for (Table t : solmuTaulukot) {
            t.setTransform(true);
            t.setRotation(angleToPointCamera - 90);
        }
    }

}
