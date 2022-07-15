package com.example.gainns;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;

public class PhysObj extends Body {

    static ArrayList<PhysObj> bodies = new ArrayList<PhysObj>();

    static Pane mainPane;
    ImageView iv = null;

    public PhysObj() {	// for non visible physics objects
        super();
        bodies.add(this);
    }

    public PhysObj(Image i) {
        this();		// do all the non visible stuff first
        iv = new ImageView();
        iv.setImage(i);
        mainPane.getChildren().add(iv);
    }

    // must be called once before adding any visual objects
    public static void setMainPane(Pane mp) {
        mainPane = mp;
    }

    // updates all visual items depending on they dynamic component (body)
    public static void update() {
        Iterator<PhysObj> pi = bodies.iterator();
        while (pi.hasNext()) {
            PhysObj po = pi.next();
            if (po.iv != null) { // only attemt to update if it has a visual
                Transform t = po.getTransform();
                po.iv.setTranslateX(t.getTranslationX()*Settings.SCALE);
                po.iv.setTranslateY(t.getTranslationY()*Settings.SCALE);
                po.iv.setRotate(po.transform.getRotation().toRadians()/0.017453292519943295);
            }
        }
    }
}