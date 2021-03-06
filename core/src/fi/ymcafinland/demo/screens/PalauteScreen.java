package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.main.MasterSkin;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by Sasu on 11.4.2016.
 */
public class PalauteScreen extends PohjaScreen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private Pelaaja pelaaja;
    private Label arvio;
    private Table exitTable;

    public PalauteScreen(SelviytyjanPurjeet sp, Pelaaja pelaaja, Skin masterSkin) {
        super(masterSkin, "PalS");
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.pelaaja = pelaaja;
        this.exitTable = createReturnButton(sp);
        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        luoSisalto();
    }

    private void luoSisalto() {
        Label otsikko = new Label(pelaaja.getNimi(), skin, "otsikko");
        rootTable.add(otsikko).top().expandX().padTop(otsikko.getHeight());
        rootTable.row();

        //todo palautteet kolmesarakkeiseen taulukkoon: numero/nimi/prosentit
        this.arvio = new Label(pelaaja.toString(), skin, "arvio");
        arvio.setFontScale(MasterSkin.HIERO_FONT_SCALE);
        rootTable.add(arvio).expand();
        rootTable.row();

        rootTable.add(exitTable).bottom().center();
    }


    @Override
    public void show() {
        super.show();

        paivitaPalaute();
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    }

    private void paivitaPalaute() {
        String palaute = "";
        ArrayList<String> keinotJarjestyksessa = pelaaja.getSelviytymiskeinotJarjestyksessa();

        for (int i = 0; i < keinotJarjestyksessa.size(); i++) {
            String kaksdesimaalinenArvo = String.format("%.1f", pelaaja.getSelviytymisprosentit(pelaaja.getIndeksiJarjestetystaListasta(i)));
            palaute += i+1 + ". " + keinotJarjestyksessa.get(i) + " " + kaksdesimaalinenArvo + "%\n";
        }
        arvio.setText(palaute);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();
    }

    //Todo createReturnButton copypastettu lähes suoraan QuestionScreenistä -> createReturnButton PohjaScreeniin?
    private Table createReturnButton(final SelviytyjanPurjeet sp) {
        Button returnButton = new Button(skin.get("returnButtonStyle", Button.ButtonStyle.class));
        returnButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PalS", "returnbuttonia painettiin");
                sp.setPlayScreen(null);
            }
        });

        Table exitTable = new Table();
        exitTable.add(returnButton);

        exitTable.validate();

        return exitTable;
    }
}