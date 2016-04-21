package fi.ymcafinland.demo.logiikka;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * Created by xvixvi on 16.4.2016.
 */
public class Vaittama {

    private String teksti;
    private String solmunID;
    private float arvo;
    private boolean checked;

    public Vaittama(String txt, String solmunID) {
        this.teksti = txt;
        this.solmunID = solmunID;
        arvo = 1f;
        checked = false;
    }

    public String getTeksti() {
        return teksti;
    }

    public String getSolmunID() {
        return solmunID;
    }

    public float getArvo() {
        return this.arvo;
    }

    public void setArvo(float arvo) {
        this.arvo = arvo;
    }
    public void setChecked(boolean checked){
        this.checked = checked;
    }
    public boolean getChecked(){
        return checked;
    }
}
