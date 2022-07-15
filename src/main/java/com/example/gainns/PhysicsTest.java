package com.example.gainns;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.geometry.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PhysicsTest extends Stage {

    //protected final St
    Pane mainPane = new Pane();
    Scale s = new Scale(1, -1);
    Translate t = new Translate(Settings.SCENE_WIDTH/2, -Settings.SCENE_HEIGHT);



    protected abstract void initializeWorld();







}
