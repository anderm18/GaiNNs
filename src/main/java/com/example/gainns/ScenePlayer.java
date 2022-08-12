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

class PlayerEvent implements EventHandler<ActionEvent> {

    private ArrayList<PhysObj> entities;
    private World world;
    private AnchorPane root;

    public PlayerEvent(World world, AnchorPane root){
        this.world = world;
        this.root = root;
    }

    public void setEntities(ArrayList<PhysObj> entities){ this.entities = entities; }

    public void begin(){
        Image img = new Image("file:img/smile.png");
        org.dyn4j.geometry.Rectangle rect = new org.dyn4j.geometry.Rectangle(1, 1);
        BodyFixture f = new BodyFixture(rect);
        f.setDensity(1.2);
        f.setFriction(0.8);
        f.setRestitution(0.4);
        PhysObj.setMainPane(this.root);
        entities.clear();
        for(int i = 0; i < 3; i++) {
            PhysObj rectangle = new PhysObj(img, f, 900, 250);
            this.world.addBody(rectangle);
            entities.add(rectangle);
        }
    }

    @Override
    public void handle(ActionEvent action) {
        if(entities != null){
            for(PhysObj po : entities){
                this.world.removeBody(po);    
            }
            System.out.println("End");   
        }
    }
}

public class ScenePlayer {

    private final World world;
    private final AnchorPane root;
    private Timeline timeline;
    private int timer;
    private Label time;
    private PlayerEvent objectEvent;
    private boolean started;
    // private final ScheduledExecutorService execService;

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
        this.time.setTextFill(Color.WHITE);
        this.time.setStyle("-fx-font-size: 8em;");
        this.objectEvent = new PlayerEvent(this.world, this.root);
        this.timeline = new Timeline();
        this.timer = 5;
        // this.execService = Executors.newSingleThreadScheduledExecutor();
        setupTimer();
    }

    void setupTimer(){
        KeyFrame kf = new KeyFrame(Duration.seconds(0),
            event -> {
                this.time.setText(timer + "");
                if (timer <= 0) {
                    //this.time.setText("");
                    this.timer = 5 + 1;
                }
                timer--;
            });
        KeyFrame kfstart = new KeyFrame(Duration.seconds(0),
            event -> {
                this.objectEvent.setEntities(instance());
            });
        KeyFrame kfend = new KeyFrame(Duration.seconds(0), this.objectEvent);

        // in order (BEGIN) -> (TIMER) -> (END) [REPEATED]
        timeline.getKeyFrames().add(kfstart);
        for(int x = 0; x < 5; x++){
            timeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.seconds(1)));
        }
        timeline.getKeyFrames().add(kfend);
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

        /*
        lol();
        this.timer = 10;
        this.time.setText(timer + "");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
        event -> {
            lol();
        }));

        KeyFrame kf = new KeyFrame(Duration.seconds(0),
        event -> {
            lol();
            timer--;
            this.time.setText(timer + "");
            if (timer <= 0) {
                timeline.stop();
                this.time.setText("");
                //for(PhysObj po : entities){
                //    this.world.removeBody(po);    
                //}
                // this.world.removeAllBodies();
                // remove all bodies
                timer = 10;
            }
        });
        timeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.seconds(1)));
        timeline.setOnFinished(event -> System.out.println("Done!"));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        */
    }

    ArrayList<PhysObj> instance(){
        // Create objects
        Image img = new Image("file:img/smile.png");
        org.dyn4j.geometry.Rectangle rect = new org.dyn4j.geometry.Rectangle(1, 1);
        BodyFixture f = new BodyFixture(rect);
        f.setDensity(1.2);
        f.setFriction(0.8);
        f.setRestitution(0.4);
        PhysObj.setMainPane(this.root);
        ArrayList<PhysObj> objs = new ArrayList<PhysObj>();
        for(int i = 0; i < 3; i++) {
            PhysObj rectangle = new PhysObj(img, f, 900, 250);
            this.world.addBody(rectangle);
            objs.add(rectangle);
        }
        return objs;
    }

    void lol(){
        // Create objects
        Image img = new Image("file:img/smile.png");
        org.dyn4j.geometry.Rectangle rect = new org.dyn4j.geometry.Rectangle(1, 1);
        BodyFixture f = new BodyFixture(rect);
        f.setDensity(1.2);
        f.setFriction(0.8);
        f.setRestitution(0.4);
        PhysObj.setMainPane(this.root);
        for(int i = 0; i < 3; i++) {
            PhysObj rectangle = new PhysObj(img, f, 900, 250);
            this.world.addBody(rectangle);
        }
    }
    
}
