package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;

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
    public ShaderProgram shaderOutline;
    Image taustapallo;
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
        lisaaSolmutStageen();
    }

    private void lisaaSolmutStageen() {
        for (Solmu s : solmut) {
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            taustapallo = luoTaustapallo();
            Table tekstit = luoTekstitaulukko(s);
            Table pallontaulukko = new Table();



            sprite = new Sprite(pallonKuva);
            loadShader();

            tekstit.setPosition(x, y);
            pallontaulukko.setPosition(x, y);
            pallontaulukko.setOrigin(Align.center);
            pallontaulukko.setRotation(s.getKulma());

            pallontaulukko.add(taustapallo).minSize(pallonLeveys,pallonKorkeus);
            if(Integer.parseInt(s.getID()) < 24)
                solmuKuvaTaulukot.add(pallontaulukko);
            solmuTaulukot.add(tekstit);

            stage.addActor(pallontaulukko);
            stage.addActor(tekstit);
        }
    }
    private void loadShader() {
        String vertexShader;
        String fragmentShader;
        vertexShader = Gdx.files.internal("shaders/outline.vsh").readString();
        fragmentShader = Gdx.files.internal("shaders/outline.fsh").readString();
        shaderOutline = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderOutline.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader: "
                    + shaderOutline.getLog());
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
    public void paivitaSolmut(float angleToPointCamera, Batch batch) {
        rotateTables(angleToPointCamera);
        paivitaSolmujenKoko();
        paivitaOutlineGlow(angleToPointCamera, batch);

    }
// AngletoPoint ei toimi tässä yhteydessä?
    private void paivitaOutlineGlow(float angleToPointCamera, Batch batch) {
        for (int i = 0; i < 23; i++) {
            Table t = solmuKuvaTaulukot.get(i);
            t.setTransform(true);
            t.setDebug(true);

            shaderOutline.begin();
            shaderOutline.setUniformf("u_viewportInverse", new Vector2(1f / pallonLeveys, 1f / pallonKorkeus));
            shaderOutline.setUniformf("u_offset", 1);
            shaderOutline.setUniformf("u_step", Math.min(1f, pallonLeveys / 70f));
            shaderOutline.setUniformf("u_color", new Vector3(0, 0, 1f));
            shaderOutline.end();
            batch.setShader(shaderOutline);
            batch.begin();
            batch.draw(sprite, t.getX(), t.getY(), pallonLeveys/2, pallonKorkeus/2, pallonLeveys, pallonKorkeus, 1f, 1f, angleToPointCamera);
            batch.end();
            batch.setShader(null);
        }
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
