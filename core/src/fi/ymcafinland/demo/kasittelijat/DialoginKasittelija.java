package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by xvixvi on 22.6.2016.
 */
public class DialoginKasittelija {

    private ArrayList<Dialog> dialogit;
    private Dialog d;
    private Skin skin;
    public boolean DIALOG_FLAG = false;

    public DialoginKasittelija(Verkko verkko, Skin skin) {
        this.skin = skin;
        d = new Dialog("dialogi", skin, "windowStyle");

        dialogit = new ArrayList<>();
        for (Solmu s : verkko.getSolmut()) {
            dialogit.add(luoDialogi(s));
        }
    }

    public void poistaDialogit() {
        for (Dialog d : dialogit) {
            d.setVisible(false);
            d.clear();
            d.remove();
        }
        d.setVisible(false);
        d.clear();
        DIALOG_FLAG = false;
        d.remove();

    }

    private Dialog luoDialogi(Solmu solmu) {
        Dialog dialogi = new Dialog(solmu.getOtsikko(), skin.get("windowStyle", com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle.class));
        dialogi.getTitleLabel().setFontScale(0.5f);

        //keskitetään dialogi ja skaalataan suuremmaksi
        dialogi.align(Align.center);
        dialogi.setOrigin(Align.center);

        asetaSisalto(solmu, dialogi);

        return dialogi;
    }

    private void asetaSisalto(Solmu solmu, Dialog dialogi) {
        Table contentTable = dialogi.getContentTable();
        String text = solmu.getDialoginSisalto();
        if(text == null) return;
        Label sisalto = new Label(text, skin, "sisalto");
        int length = text.length();
        if(length < 25) sisalto.setFontScale(0.8f);
        else if(length >= 25 && length < 110) sisalto.setFontScale(0.35f);
        else{ sisalto.setFontScale(0.2f);
        }


        sisalto.setAlignment(Align.center);
        sisalto.setWrap(true);
        dialogi.getContentTable().center().add(sisalto).minSize(220,300).row();

        dialogi.setModal(false);
        dialogi.layout();
        contentTable.setVisible(true);
    }

    public void naytaDialogi(Stage stage, Solmu solmu, float xKoordinaatti, float yKoordinaatti, float PPtoKP) {
//        int indeksi = Integer.parseInt(solmu.getID());
//        Dialog d = dialogit.get(indeksi - 1);
        d = luoDialogi(solmu);
        if(!DIALOG_FLAG) {
            DIALOG_FLAG = true;
            d.show(stage);
        }
        d.setWidth(SelviytyjanPurjeet.V_WIDTH / 1.7f);
        d.setHeight(SelviytyjanPurjeet.V_HEIGHT / 2f);

        d.setPosition(xKoordinaatti, yKoordinaatti);
        d.setRotation(PPtoKP - 90);
    }
}
