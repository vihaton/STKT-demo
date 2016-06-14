package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.kasittelijat.VaittamanKasittelija;
import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 29.3.2016.
 * <p/>
 * QuestionScreen luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class QuestionScreen extends PohjaScreen {
    protected SpriteBatch batch;

    private final Pelaaja pelaaja;
    private final Vaittamat vaittamat;
    private ArrayList<Vaittama> solmunVaittamat;
    private VaittamanKasittelija vaittamanKasittelija;
    Solmu solmu;
    private Table returnButtonTable;
    private Label otsikko;
    private float sidePad;
    private ArrayList<Float> alkuarvot;
    private Table continueButtonTable;

    public QuestionScreen(final SelviytyjanPurjeet sp, Pelaaja pelaaja, Vaittamat vaittamat, Skin masterSkin) {
        super(masterSkin, "QS");
        this.batch = new SpriteBatch();
        this.pelaaja = pelaaja;
        this.vaittamat = vaittamat;
        this.sidePad = 64;

        this.returnButtonTable = createReturnButton(sp);
        taytaRootTable();

        this.vaittamanKasittelija = new VaittamanKasittelija(stage, masterSkin, sp);
        stage.addActor(vaittamanKasittelija.getPane());

        this.continueButtonTable = createContinueButton(sp);
        vaittamanKasittelija.setContinueButtonTable(continueButtonTable);
    }

    private void taytaRootTable() {
        Table otsikkoTable = luoOtsikko();

        rootTable.top().add(otsikkoTable).padTop(sidePad / 2).padLeft(sidePad);
        rootTable.add(returnButtonTable);

        rootTable.validate();
    }

    private Table luoOtsikko() {
        Table ot = new Table();

        otsikko = new Label("Questionscreen on tässä, tämä on siis question screen eli väittämänäkymä, eli QS eli question screen.", skin, "otsikko");
        otsikko.setFillParent(true);
        otsikko.setFontScale(0.7f);
        otsikko.setWrap(true);
        otsikko.setAlignment(Align.center);

        ot.add(otsikko).width(SelviytyjanPurjeet.V_WIDTH - 2 * sidePad);
        ot.validate();

        return ot;
    }

    //tällä hetkellä aivan sama kuin continue, tulee muuttumaan
    private Table createReturnButton(final SelviytyjanPurjeet sp) {
        Button returnButton = new Button(skin.get("returnButtonStyle", Button.ButtonStyle.class));
        returnButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("QS", "returnbuttonia painettiin");
                sendData();
                if (solmu.getMutsi() == null) {
                    sp.setPlayScreenMaxSelviytyjaan();
                } else {
                    sp.setPlayScreen(solmu);
                }
            }
        });

        Table table = new Table();
        table.add(returnButton).padRight(sidePad);

        table.validate();

        return table;
    }

    //tällä hetkellä aivan sama kuin return, tulee muuttumaan
    private Table createContinueButton(final SelviytyjanPurjeet sp) {
        Button continueButton = new Button(skin.get("continueButtonStyle", Button.ButtonStyle.class));
        continueButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("QS", "continuebuttonia painettiin");
                sendData();
                if (solmu.getMutsi() == null) {
                    sp.setPlayScreenMaxSelviytyjaan();
                } else {
                    sp.setPlayScreen(solmu);
                }
            }
        });

        Table table = new Table();
        table.add(continueButton);

        table.validate();

        return table;
    }

    /**
     * sendData lähettää saadun datan eteenpäin lisäämällä pelaajan selviytymisarvoon, mihin väittämä vaikuttaa, väittämän sliderin arvon.
     * SendData konfirmoi kysymykseen laitetun tiedon, ja
     * kutsuu tiedon lähettämisen jälkeen dispose -metodia.
     */
    public void sendData() {
        for (int i = 0; i < solmunVaittamat.size(); i++) {
            Vaittama v = solmunVaittamat.get(i);
            float uusiArvo = v.getVaikuttavaArvo();
            float vanhaArvo = alkuarvot.get(i);

            if (uusiArvo == vanhaArvo) {        //jos arvoa ei ole muutettu, ei vastausta tarvitse lisätä mihinkään
                continue;
            }

            if (!pelaaja.onkoVastannut(v)) {    //jos kysymykseen ei ole aikaisemmin vastattu, voidaan lisätä vastausten määrää
                pelaaja.lisaaVastaus(solmu, v);
            }
            //muutetaan selviytymisarvoa vain oikean muutoksen verran
            pelaaja.lisaaSelviytymisarvoIndeksissa(v.getMihinSelviytymiskeinoonVaikuttaa(), v.getVaikuttavaArvo() - alkuarvot.get(i));
        }
    }

    public void setSolmu(Solmu solmu) {
        this.solmu = solmu;
        asetaOtsikonTeksti(solmu);
        String solmunID = solmu.getID();
        solmunVaittamat = vaittamat.getYhdenSolmunVaittamat(solmunID);
        alkuarvot = vaittamanKasittelija.paivitaVaittamat(solmunVaittamat);
    }

    public void asetaOtsikonTeksti(Solmu solmu) {
        if (solmu.getMutsi() == null) {
            otsikko.setText("Testaa, millainen selviytyjä olet:");
        } else {
            otsikko.setText(solmu.getMutsi().getOtsikko() + ":\n" + solmu.getSisalto());
        }
    }

    @Override
    public void show() {
        super.show();
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        vaittamanKasittelija.paivita(delta);

        stage.draw();
    }
}