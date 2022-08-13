package com.example.gainns;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.World;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.event.EventHandler;

class ObjectEvent {

    private ArrayList<PhysObj> entities;
    private World world;
    private AnchorPane root;

    public ObjectEvent(World world, AnchorPane root){
        this.world = world;
        this.root = root;
        this.entities = new ArrayList<PhysObj>();
    }

    public void generateEnvironment(){
        org.dyn4j.geometry.Rectangle rect = new org.dyn4j.geometry.Rectangle(1, 1);
        BodyFixture f = new BodyFixture(rect);
        f.setDensity(1.2);
        f.setFriction(0.8);
        f.setRestitution(0.4);
        PhysObj.setMainPane(this.root);
        entities.clear();
        for(int i = 0; i < 3; i++) {
            PhysObj rectangle = new PhysObj(new Image("file:img/smile.png"), f, 900, 250);
            this.world.addBody(rectangle);
            entities.add(rectangle);
            
        }
        for(int x = 0; x < 13; x++){
            System.out.println(this.root.getChildren().get(x));
        }
    }

    public void resetEnvironment() {
        if(entities != null){
            for(PhysObj po : entities){
                this.world.removeBody(po);
                this.root.getChildren().remove(po.getImage());
            }
        }
    }
}

public class ScenePlayer {

    private final World world;
    private final AnchorPane root;
    private Timeline timeline;
    private int timer;
    private Label time;
    private ObjectEvent objectEvent;
    private boolean started;

    /**
     * 
     * @param world
     * @effects assign this world to the current world
     */
    ScenePlayer(World world, AnchorPane root){
        this.world = world;
        this.root = root;
        this.time = new Label();
        this.started = false;
        this.root.getChildren().add(this.time);
        this.time.setTextFill(Color.BLACK);
        this.time.setStyle("-fx-font-size: 8em;");
        this.time.setTranslateX(2 * Settings.SCALE);
        this.time.setTranslateY(2 * Settings.SCALE);
        this.objectEvent = new ObjectEvent(this.world, this.root);
        this.timeline = new Timeline();
        this.timer = 5;
        setupTimer();
    }

    void setupTimer(){
        KeyFrame kf = new KeyFrame(Duration.seconds(1),
            event -> {
                this.time.setText(timer + "");
                if (timer == 5){
                    this.objectEvent.generateEnvironment();
                } else if (timer <= 0) {
                    //this.time.setText("");
                    this.objectEvent.resetEnvironment();
                    this.timer = 5 + 1;
                }
                timer--;
            });
        
        timeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /** Environment Player
     * - this environment will play out for (time) seconds and then reset at
     *   by creating the objects again
     * 
     * TODO: class or object as an env to store data
     * 
     */
    public void play(){
        if(started){
            timeline.stop();
            this.time.setText("");
            this.timer = 5;
        } else {
            timeline.play();
        }
    }
}
