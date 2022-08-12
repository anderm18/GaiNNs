package com.example.gainns;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.animation.Animation;
import javafx.util.Duration;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.World;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

class CustomEvent extends ActionEvent {

}

public class ScenePlayer {

    private final World world;
    private final AnchorPane root;
    // private final ScheduledExecutorService execService;

    /**
     * 
     * @param world
     * @effects assign this world to the current world
     */
    ScenePlayer(World world, AnchorPane root){
        this.world = world;
        this.root = root;
        // this.execService = Executors.newSingleThreadScheduledExecutor();
    }

    /** Environment Player
     * - this environment will play out for (time) seconds and then reset at
     *   by creating the objects again
     * 
     * TODO: class or object as an env to store data
     * 
     */

    private int i = 10;

    public void play(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
        event -> {
            lol();
        }));

        KeyFrame kf = new KeyFrame(Duration.seconds(0),
        event -> {
            //setText(String.valueOf(i--));
            i--;
            System.out.println("Time: " + i);
            if (i <= 0) {
                timeline.stop();
                //for(PhysObj po : entities){
                //    this.world.removeBody(po);
                //}
                // this.world.removeAllBodies();
                // remove all bodies
                i = 10;
            }
        });
        timeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.seconds(1)));
        timeline.setOnFinished(event -> System.out.println("Done!"));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        /* 
        PlayerTask t = new PlayerTask(this.world, this.root);
		
		long delay = 100;
		long repeatPeriod = 1000;
		Timer timer = new Timer();
		timer.schedule(t, delay, repeatPeriod);//Repeat task every 1000ms
        */
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
