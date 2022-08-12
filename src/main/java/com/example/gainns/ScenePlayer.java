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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

class PlayerTask extends TimerTask {

    private ArrayList<PhysObj> entities;
    private World world;

    void setWorld(World other){ this.world = other; }
    void setEntities(ArrayList<PhysObj> entities){ this.entities = entities; }

    @Override
    public void run() {
        for(PhysObj po : entities){
            this.world.removeBody(po);    
        }
    }
}

public class ScenePlayer {

    private final World world;
    private final AnchorPane root;
    //private Timeline timeline;
    //private int timer;
    private Label time;
    private PlayerTask task;
    // private final ScheduledExecutorService execService;

    /**
     * 
     * @param world
     * @effects assign this world to the current world
     */
    ScenePlayer(World world, AnchorPane root){
        this.world = world;
        this.root = root;
        //this.time = new Label();
        //this.root.getChildren().add(this.time);
        //this.time.setTextFill(Color.WHITE);
        //this.time.setStyle("-fx-font-size: 8em;");
        this.task = new PlayerTask();
        this.task.setWorld(world);
        // this.execService = Executors.newSingleThreadScheduledExecutor();
    }

    /** Environment Player
     * - this environment will play out for (time) seconds and then reset at
     *   by creating the objects again
     * 
     * TODO: class or object as an env to store data
     * 
     */

    public void play(){

        lol();

        Timeline fiveSecondsWonder = new Timeline(
                 new KeyFrame(Duration.seconds(3), 
                 new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("this is called every 3 seconds on world thread");
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();


        Timer timer = new Timer();
        this.task.setEntities(instance());
        timer.schedule(task, 0, 5000);

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
