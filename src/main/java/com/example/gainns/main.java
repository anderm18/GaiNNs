package com.example.gainns;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;
import javafx.animation.AnimationTimer;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
import org.dyn4j.world.World;

import java.util.Random;

public class main extends Application {

    Pane mainPane;
    World world;
    Scene scene;

    Random random = new Random();

    double rnd(double from, double to) {
        return to + (from - to) * random.nextDouble();
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();

        // the scale and translate mean 0,0 is in the centre of the screen
        // at the bottom with height increasing up the screen
        mainPane = new Pane();
        Scale s = new Scale(1, -1);
        Translate t = new Translate(Settings.SCENE_WIDTH/2,-Settings.SCENE_HEIGHT);
        mainPane.getTransforms().addAll(s,t);

        root.getChildren().add( mainPane );

        scene = new Scene( root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        PhysObj.setMainPane(mainPane);

        primaryStage.setScene( scene );
        primaryStage.show();

        Image img = new Image("file:img/smile.png");

        world = new World();

        // create the floor
        Rectangle floorRect = new Rectangle(15.0, 1.0);
        PhysObj floor = new PhysObj(); // invisible no image...
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);

        // move the floor down a bit
        floor.translate(0.0, -1.0);
        this.world.addBody(floor);

        Rectangle rectShape = new Rectangle(1.0, 1.0);

        for (int i=0; i<32; i++) {
            PhysObj rectangle = new PhysObj(img);
            BodyFixture f = new BodyFixture(rectShape);
            f.setDensity(1.2);
            f.setFriction(0.8);
            f.setRestitution(0.4);
            rectangle.addFixture(f);
            rectangle.setMass();
            rectangle.translate(rnd(-3,3), 9.0+rnd(-4,2));
            rectangle.getTransform().setRotation(rnd(-3.141,3.141));
            this.world.addBody(rectangle);
        }

        AnimationTimer gameLoop = new AnimationTimer() {

            long last;

            @Override
            public void handle(long now) { // now is in nanoseconds
                float delta = 1f / (1000.0f / ((now-last) / 1000000));  // seems long winded but avoids precision issues
                world.updatev(delta);
                PhysObj.update();

                last = now;
            }

        };
        gameLoop.start();

    }


    public static void main(String[] args) {
        launch(args);
    }

}