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
import javafx.stage.Screen;
import javafx.stage.Stage;
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

public class Environment extends Application {
	// resize storage param
	double resizeBlockWidth = 6, resizeBlockWidthOffset = resizeBlockWidth / 2;
	double lastX, lastY;
	double pressedMousePosX, pressedMousePosY, shapeLayoutX, shapeLayoutY, sWidth, sHeight;
	Group overlay = null;
	World world;
	AnchorPane root;

	Random random = new Random();
	double rnd(double from, double to) {
		return to + (from - to) * random.nextDouble();
	}

	// scale change point
	private Rectangle srBnd, srNW, srN, srNE, srE, srSE, srS, srSW, srW;
	private Circle srCen, srRotate;
	private Dragable selectedElement; 
    
	//temp debug
	private final Label posReporter = new Label();
	private final Label elementInEnvReporter = new Label();
	private final Label dragAndDropReporter = new Label();
	private final Label rotateReporter = new Label();
	
	// mouse tracker
	private double mousePosXTraker = 0;
	private double mousePosYTraker = 0;
	private List<Dragable> shapesInEnv = new ArrayList<Dragable>();
	
	// resolution offset
	private double windowWidth = Screen.getPrimary().getBounds().getWidth()-400;
    private double windowHeight = Screen.getPrimary().getBounds().getHeight()-300;
    
    // hard limit: shape will not reach ground
    Rectangle2D area = new Rectangle2D(0, 0, windowWidth, windowHeight-100);
    ShapesMenu shapesMenu = new ShapesMenu();
    
    // A container to store all pressed keys
    private Set<KeyCode> pressedKeys = new HashSet<>();
    
    private Rectangle createFloor(Scene scene) {
        Rectangle rectangle = new Rectangle(windowWidth, 100);
        rectangle.widthProperty().bind(scene.widthProperty()); //keep as wide as window
        rectangle.setFill(Color.valueOf("#F5E799"));
        return rectangle;
    }
    
    // Copy and Paste Shapes: record the copied shapes
    private Dragable copiedShape = null;

    //Menu bar is good for creating for general menus, not really for drag and drop. We probably
    //should use it to make a general menu (File, Edit, Help, etc). 
    @Override
    public void start(Stage stage) throws IOException { 	
        this.root = new AnchorPane(); //AnchorPane had better functions then border pane

        Scene scene = new Scene(root, windowWidth, windowHeight);
        // Listen for keys
        handleKeyboardShortcut(scene);
                
        this.handleKeyboardShortcut(scene);
        
        Rectangle floorRect = createFloor(scene); //floor
        org.dyn4j.geometry.Rectangle physicsRect = new org.dyn4j.geometry.Rectangle(20, 1);//(floorRect.getWidth()/Settings.SCALE, floorRect.getHeight()/Settings.SCALE);
        PhysObj floorphys = new PhysObj();
        floorphys.addFixture(new BodyFixture(physicsRect));
        floorphys.setMass(MassType.INFINITE);

        Rectangle sceneRect = new Rectangle(windowWidth, windowHeight); //env range
        sceneRect.widthProperty().bind(scene.widthProperty()); //keep as wide as window
        sceneRect.heightProperty().bind(scene.heightProperty()); //keep as high as window
        sceneRect.setFill(Color.valueOf("#99F0F5"));
        PhysObj.setMainPane(this.root);
        // set layer and position for of base env
        HBox floor = new HBox(0, floorRect);
        floor.setViewOrder(3);
        HBox env = new HBox(0, sceneRect);
        env.setViewOrder(4);
        this.world = new World();
        this.world.setGravity(0, 9.81);
        floorphys.translate(500/Settings.SCALE, 465/Settings.SCALE); //465 more or less lines up with floor
        this.world.addBody(floorphys);

        Image img = new Image("file:img/smile.png");
        org.dyn4j.geometry.Rectangle rect = new org.dyn4j.geometry.Rectangle(1, 1);
        BodyFixture f = new BodyFixture(rect);
        f.setDensity(1.2);
        f.setFriction(0.8);
        f.setRestitution(0.4);
        for(int i = 0; i < 3; i++) {
            PhysObj rectangle = new PhysObj(img, f, 900, 250);
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

        
        // deselecting overlay
        env.setOnMousePressed(me -> select(null));
        floor.setOnMousePressed(me -> select(null));

        //menu for shapes
        shapesMenu.createMenu(scene);
        HBox sMenu = new HBox(0, shapesMenu.getMenu().shapeVisualizedList);
        sMenu.setPickOnBounds(false);
        HBox tab = new HBox(0, shapesMenu.getTab()); //tab to close menu
        HBox changeMenu = new HBox(0, shapesMenu.getChangeMenuTab());
        
        // config layout
        root.getChildren().addAll(env, floor, sMenu, tab, changeMenu, 
        		                  posReporter, elementInEnvReporter, dragAndDropReporter, rotateReporter);
        AnchorPane.setBottomAnchor(floor, 0d); // positioning shapes in scene									
        AnchorPane.setTopAnchor(tab, 120d);
        AnchorPane.setTopAnchor(changeMenu, 120d);
        AnchorPane.setBottomAnchor(posReporter, 65d);
        AnchorPane.setBottomAnchor(dragAndDropReporter, 50d);
        AnchorPane.setBottomAnchor(elementInEnvReporter, 0d);
        AnchorPane.setBottomAnchor(rotateReporter, 110d);
        AnchorPane.setLeftAnchor(tab, ((double)scene.getWidth())/1.98);
        AnchorPane.setLeftAnchor(changeMenu, ((double)scene.getWidth())/2.28);

        AnchorPane.setTopAnchor(sMenu, 0d);
        
        // track mouse info all the time
        root.setOnMousePressed(e -> {
            updateMousePosition(e);
        });
        root.setOnMouseDragged(e -> {
            updateMousePosition(e);
        });
        root.setOnMouseMoved(e -> {
            updateMousePosition(e);
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMousePressed(e -> {
            updateMousePosition(e);
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMouseDragged(e -> {
            updateMousePosition(e);
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMouseMoved(e -> {
            updateMousePosition(e);
        });
        
       
        // HIDE/SHOW
        shapesMenu.getTab().setOnAction(value ->  { //button(tab) pressed
            if(shapesMenu.tabPressed()) { // if hidden
                TranslateTransition tabShow = new TranslateTransition(Duration.millis(250), tab);
                tabShow.setByY(-120f);
                tabShow.setCycleCount(1);
                tabShow.play();
                TranslateTransition changeMenuShow = new TranslateTransition(Duration.millis(250), changeMenu);
                changeMenuShow.setByY(-120f);
                changeMenuShow.setCycleCount(1);
                changeMenuShow.play();
            }
            else {
                TranslateTransition tabHide = new TranslateTransition(Duration.millis(250), tab);
                tabHide.setByY(120f);
                tabHide.setCycleCount(1);
                tabHide.play();
                TranslateTransition changeMenuHide = new TranslateTransition(Duration.millis(250), changeMenu);
                changeMenuHide.setByY(120f);
                changeMenuHide.setCycleCount(1);
                changeMenuHide.play();
            }
         });

         // CHANGE MENU
         shapesMenu.getChangeMenuTab().setOnAction(value ->  {
            shapesMenu.changeMenu();
         });
        
        
        // Shape drag and drop
        sceneRect.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
            	updateMousePosition(event);
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });
        
        // Copy shape
        KeyCombination copyShapeKeyCombo = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        Runnable copyShapeRunnable = () -> {
        	boolean DEBUG = false;
        	if (DEBUG) System.out.println("Accelerator Ctrl + C pressed");
        	
        	if (this.selectedElement != null) {
        		this.copiedShape = this.selectedElement;
        		if (DEBUG) {
        			System.out.println("Shape copied");
            		System.out.println("Shape name: " + this.copiedShape.getShapeName());
        		}
        	} 
        	else if (DEBUG) System.out.println("No shape selected");
        };
        scene.getAccelerators().put(copyShapeKeyCombo, copyShapeRunnable);
        
        // Paste shape
        KeyCombination pasteShapeKeyCombo = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
        Runnable pasteShapeRunnable = () -> {
        	boolean DEBUG = true;
        	if (this.area.getMinX() <= this.mousePosXTraker && this.mousePosXTraker <= this.area.getMaxX()
        		&& this.area.getMinY() <= this.mousePosYTraker && this.mousePosYTraker <= this.area.getMaxY()) {
        		
        		if (DEBUG) System.out.println("Accelerator Ctrl + V pressed");
	        	// first, create a deep copy of the copied shape using the reference to the old shape
	        	Dragable pastingShape = createElement(mousePosXTraker, mousePosYTraker, copiedShape);
	        	// then add that deep copy into the environment where the mouse is
	        	// the function below is a hint for adding shapes to environment
	        	shapesInEnv.add(pastingShape);
	        	elementInEnvReporter.setText("Current element count in env: " + shapesInEnv.size());
	        	root.getChildren().add(pastingShape);
        	}
        	else {
        		if (DEBUG) System.out.println("Detected mouse out of bounds");
        	}

        };
        scene.getAccelerators().put(pasteShapeKeyCombo, pasteShapeRunnable);

        // drop event - create shape
        sceneRect.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            String shapeInfo = db.getString().replace("[", "!!").replace("]", "!!"); // avoid parsing bug in JAVA 1.8 for windows
            double shapeParam0 = Double.parseDouble(db.getString().split(", ")[2].split("=")[1]);
            double shapeParam1 = Double.parseDouble(shapeInfo.split(", ")[3].split("=")[0].equals("fill")? "0":shapeInfo.split(", ")[3].split("=")[1]); 
            
            Dragable newAddedElement = createElement(mousePosXTraker, mousePosYTraker, shapeParam0, shapeParam1, Color.web(shapeInfo.split("fill=")[1].split("!!")[0]), shapeInfo.split("!!")[0]);
            shapesInEnv.add(newAddedElement);
            elementInEnvReporter.setText("Current element count in env: " + shapesInEnv.size());           
            root.getChildren().add(newAddedElement);
            
            if (db.hasString()) {
            	dragAndDropReporter.setText("Dropped: " + db.getString());
            	dragAndDropReporter.setTextFill(Color.web(shapeInfo.split("fill=")[1].split("!!")[0]));
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
        
        // set up window
        stage.setTitle("GaiNNs");
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.show();
        
        AnchorPane.setLeftAnchor(tab, ((double) stage.getWidth())/2.0);
        AnchorPane.setLeftAnchor(changeMenu, ((double) stage.getWidth())/2.0 - shapesMenu.getChangeMenuTab().getWidth());
        stage.widthProperty().addListener((obs, oldVal, newVal) -> { // change pos of button(tab) when window changes
    		AnchorPane.setLeftAnchor(tab, ((double)newVal)/2.0);
    		AnchorPane.setLeftAnchor(changeMenu, ((double)newVal)/2.0 - shapesMenu.getChangeMenuTab().getWidth());
    	});
        gameLoop.start();
    }
    
    private void handleKeyboardShortcut(Scene scene) {
    	boolean DEBUG = false;
    	
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//    		KeyCombination copyShapeKeyCombo = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
//    		KeyCombination pasteShapeKeyCombo = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
            // KeyCombination codeCombo = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

            @Override
            public void handle(KeyEvent event) {
//                if (copyShapeKeyCombo.match(event)) {
//                    System.out.println("Ctrl + C pressed");
//                } 
//                if (pasteShapeKeyCombo.match(event)) {
//                    System.out.println("Ctrl + V pressed");
//                }
                if (event.getCode() == KeyCode.DELETE && selectedElement != null) {
            		if (DEBUG) System.out.println("DELETE pressed");
            		root.getChildren().remove(overlay);
            		root.getChildren().remove(selectedElement);
            		shapesInEnv.remove(selectedElement);
            		overlay = null;
            		selectedElement = null;
            	}
                pressedKeys.add(event.getCode());
            }
        }); 
    	
        scene.setOnKeyReleased(e -> {
        	pressedKeys.remove(e.getCode());
        	if (DEBUG) System.out.println("released");
        });
        
		
	}


	public static void main(String[] args) {
        launch();
    }
    
    /**
     * for dev, update the mouse position and 
     * display in main window
     * @param event the mouse event, store all pos info
     */
    public void updateMousePosition(InputEvent event) {
    	if (event instanceof MouseEvent ) {
			MouseEvent eventInstance = (MouseEvent) event;
    		mousePosXTraker = eventInstance.getX();
            mousePosYTraker = eventInstance.getY();
            posReporter.setText("Debug Info:\n(x: " + eventInstance.getX() + ", y: " + eventInstance.getY() + ") \n" );
    	}else if (event instanceof DragEvent) {
    		DragEvent eventInstance = (DragEvent) event;
    		mousePosXTraker = eventInstance.getX();
            mousePosYTraker = eventInstance.getY();
            posReporter.setText("Debug Info:\n(x: " + eventInstance.getX() + ", y: " + eventInstance.getY() + ") \n" );
    	}
    	
    }
    
    /**
     * helper function for event when a shape in env
     * is selected, generate overlay
     * @param element the selected shape
     */
    void select(Dragable element) {
        if (this.overlay == null && element != null) iniOverlay();
        if (element != this.selectedElement) {
        	this.overlay.setVisible(element != null);
            if (element != null) element.toFront();
            this.selectedElement = element;
            updateOverlay();
        }
        
    }

    /**
     *  helper function used when overlay is null
     *  and need to create and add to window
     */
    void iniOverlay() {
    	this.overlay = new Group();
        overlay.setVisible(false);
    	this.srBnd = new Rectangle();
    	this.srBnd.setStroke(Color.BLACK);
    	this.srBnd.setStrokeType(StrokeType.INSIDE);
    	this.srBnd.setStrokeWidth(1);
    	this.srBnd.getStrokeDashArray().addAll(2d, 4d);
    	this.srBnd.setFill(Color.TRANSPARENT);
        handleMouse(this.srBnd);
        this.srNW = srCreate(Cursor.NW_RESIZE);
        this.srN = srCreate(Cursor.N_RESIZE);
        this.srNE = srCreate(Cursor.NE_RESIZE);
        this.srE = srCreate(Cursor.E_RESIZE);
        this.srSE = srCreate(Cursor.SE_RESIZE);
        this.srS = srCreate(Cursor.S_RESIZE);
        this.srSW = srCreate(Cursor.SW_RESIZE);
        this.srW = srCreate(Cursor.W_RESIZE);
        this.srCen = srCreate(Cursor.CROSSHAIR, true);
        this.srRotate = srCreate(Cursor.OPEN_HAND, false);
        this.overlay.getChildren().addAll(this.srBnd,
										  this.srNW, this.srN, 
										  this.srNE, this.srE, 
										  this.srSE, this.srS, 
									      this.srSW, this.srW, 
									      this.srCen, this.srRotate);
        this.root.getChildren().add(this.overlay);
        this.overlay.setViewOrder(1);
        
    }

    /**
     * helper function used when shape event triggered
     * update overlay position and size
     */
    void updateOverlay() {
        if (this.selectedElement != null) {
        	this.srBnd.setX(this.selectedElement.getLayoutX() - this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srBnd.setY(this.selectedElement.getLayoutY() - this.selectedElement.getRotationLengthOffsetY()/2);
        	this.srBnd.setWidth(this.selectedElement.widthProperty().get() + this.selectedElement.getRotationLengthOffsetX());
        	this.srBnd.setHeight(this.selectedElement.heightProperty().get() + this.selectedElement.getRotationLengthOffsetY());
        	// north west
        	this.srNW.setX(this.selectedElement.getLayoutX() - this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srNW.setY(this.selectedElement.getLayoutY() - this.selectedElement.getRotationLengthOffsetY()/2);
        	// north
        	this.srN.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2) - this.resizeBlockWidthOffset);
        	this.srN.setY(this.selectedElement.getLayoutY() - this.selectedElement.getRotationLengthOffsetY()/2);
        	// north east
        	this.srNE.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get()) - 
        					this.resizeBlockWidth + this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srNE.setY(this.selectedElement.getLayoutY() - this.selectedElement.getRotationLengthOffsetY()/2);
        	// east
        	this.srE.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get()) - 
        				   this.resizeBlockWidth + this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srE.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get() / 2) - this.resizeBlockWidthOffset);
        	// south east
        	this.srSE.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get()) - 
        					this.resizeBlockWidth + this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srSE.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get()) - 
        					this.resizeBlockWidth + this.selectedElement.getRotationLengthOffsetY()/2);
        	// south
        	this.srS.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2) - this.resizeBlockWidthOffset);
        	this.srS.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get()) - 
        				   this.resizeBlockWidth + this.selectedElement.getRotationLengthOffsetY()/2);
        	// south west
        	this.srSW.setX(this.selectedElement.getLayoutX() - this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srSW.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get()) - 
        					this.resizeBlockWidth + this.selectedElement.getRotationLengthOffsetY()/2);
        	// west
        	this.srW.setX(this.selectedElement.getLayoutX() - this.selectedElement.getRotationLengthOffsetX()/2);
        	this.srW.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get() / 2) - this.resizeBlockWidthOffset);
        	
        	// center dot
        	this.srCen.setCenterX(this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2);
        	this.srCen.setCenterY(this.selectedElement.getLayoutY()+ this.selectedElement.heightProperty().get() / 2);
        	
        	// rotate dot
        	this.srRotate.setCenterX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2));
        	this.srRotate.setCenterY(this.selectedElement.getLayoutY() - 10 - this.selectedElement.getRotationLengthOffsetY()/2);
        }
      }

    /**
     * constructor of square overlay point
     * @param cursor    set the cursor style when 
     * 				    cursor pass this point
     * @return a rectangle represents the overlay point
     * 		   in a specific position
     */
    Rectangle srCreate(Cursor cursor) {
        Rectangle rectangle = new Rectangle(this.resizeBlockWidth, this.resizeBlockWidth, Color.BLACK);
        rectangle.setCursor(cursor);
        handleMouse(rectangle);
        return rectangle;
     }
    
    /**
     * constructor of circle overlay point
     * @param cursor    set the cursor style when 
     * 				    cursor pass this point
     * @param isCenter  true if needs to create 
     *                  center overlay point, 
     *                  no fill in this case
     * @return a circle represents the overlay point
     * 		   in a specific position
     */
    Circle srCreate(Cursor cursor, boolean isCenter) {
    	Circle circle;
    	if (isCenter) {
    		circle = new Circle(this.resizeBlockWidth/2);
    		circle.setStroke(Color.BLACK);
    		circle.setFill(Color.TRANSPARENT);
    	}else {
    		circle = new Circle(this.resizeBlockWidth/2, Color.BLACK);
    	}
    	
    	circle.setCursor(cursor);
        handleMouse(circle);
        return circle;
    }

    /**
     * event manager for all overlay element 
     * @param node the element to set these action
     */
    void handleMouse(Node node) {
    	node.setOnMouseReleased(me -> {
    		if ((node == this.srRotate) && (this.selectedElement.isCharMenuShowing() == shapesMenu.isCharMenuShowing())) {
    			setRotate(me.getX(), me.getY(), true);
    			this.srRotate.setCursor(Cursor.OPEN_HAND);
    		}
    	});
        node.setOnMousePressed(me -> {
            if (this.selectedElement.isCharMenuShowing() == shapesMenu.isCharMenuShowing()) {
        	    this.pressedMousePosX = me.getX();
        	    this.pressedMousePosY = me.getY();
        	    this.shapeLayoutX = this.selectedElement.getLayoutX();
        	    this.shapeLayoutY = this.selectedElement.getLayoutY();
        	    this.sWidth = this.selectedElement.widthProperty().get();
        	    this.sHeight = this.selectedElement.heightProperty().get();
        	    if (node == this.srRotate) this.srRotate.setCursor(Cursor.CLOSED_HAND);
                // me.consume();
            }
        });
        
        node.setOnMouseDragged(me -> {

            if (this.selectedElement.isCharMenuShowing() == shapesMenu.isCharMenuShowing()) {
                double dx = (me.getX() - this.pressedMousePosX);
                double dy = (me.getY() - this.pressedMousePosY);
                Object source = me.getSource();
                if (source == this.srBnd) relocate(this.shapeLayoutX + dx, this.shapeLayoutY + dy);
                else if (source == this.srNW) {
            	    if (pressedKeys.contains(KeyCode.SHIFT)) {
            		    System.out.println("SHIFT key pressed");
            		    double ratio = this.sHeight / this.sWidth;
            		    setHSize(this.shapeLayoutX + dx, true); 
            		    setVSize(this.shapeLayoutY + dx * ratio, true);
            	    } else {
            		    setHSize(this.shapeLayoutX + dx, true); 
            		    setVSize(this.shapeLayoutY + dy, true);
            	    }
                }
                else if (source == this.srN) setVSize(this.shapeLayoutY + dy, true);
                else if (source == this.srNE) { 
            	    if (pressedKeys.contains(KeyCode.SHIFT)) {
            		    double ratio = this.sHeight / this.sWidth;
            		    setHSize(this.shapeLayoutX + this.sWidth + dx, false);
            		    setVSize(this.shapeLayoutY + dx * ratio * -1, true);
            	    } else {
            		    setHSize(this.shapeLayoutX + this.sWidth + dx, false);
            		    setVSize(this.shapeLayoutY + dy, true); 
            	    }
                }
                else if (source == this.srE) setHSize(this.shapeLayoutX + this.sWidth + dx, false);
                else if (source == this.srSE) {	
            	    if (pressedKeys.contains(KeyCode.SHIFT)) {
            		    double ratio = this.sHeight / this.sWidth;
            		    setHSize(this.shapeLayoutX + this.sWidth + dx, false); 
            		    setVSize(this.shapeLayoutY + this.sHeight + dx * ratio, false);
            	    } else {
            		    setHSize(this.shapeLayoutX + this.sWidth + dx, false); 
            		    setVSize(this.shapeLayoutY + this.sHeight + dy, false);
            	    }
                }
                else if (source == this.srS) setVSize(this.shapeLayoutY + this.sHeight + dy, false);
                else if (source == this.srSW) { 
            	    if (pressedKeys.contains(KeyCode.SHIFT)) {
            		    double ratio = this.sHeight / this.sWidth;
            		    setHSize(this.shapeLayoutX + dx, true); 
            		    setVSize(this.shapeLayoutY + this.sHeight + dx * ratio * -1, false);
            	    } else {
            		    setHSize(this.shapeLayoutX + dx, true); 
            		    setVSize(this.shapeLayoutY + this.sHeight + dy, false);
            	    }
                }
                else if (source == this.srW) setHSize(this.shapeLayoutX + dx, true);
                else if (source == this.srCen) setCenter(this.shapeLayoutX + dx, this.shapeLayoutY + dy);
                else if (source == this.srRotate) setRotate(me.getX(), me.getY(), false);
                else if (source == this.srW) setHSize(this.shapeLayoutX + dx, true);
                else if (source == this.srCen) setCenter(this.shapeLayoutX + dx, this.shapeLayoutY + dy);
                else if (source == this.srRotate) setRotate(me.getX(), me.getY(), false);
                me.consume();
            }

            
        });        

      }

    /**
     * function handle the rotation event for dragable shape
     * @param horizontalParam the center of shape in x
     * @param verticalParam   the center of shape in y
     * @param needStore       true if the updated angle needs
     * 						  to save in element
     */
    void setRotate(double horizontalParam, double verticalParam, boolean needStore) {
    	// set centre
    	// TODO: editable center based on srCen
    	double centerShapeX = this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2;
    	double centerShapeY = this.selectedElement.getLayoutY() + this.selectedElement.widthProperty().get() / 2;
    	
    	double degree = (Math.toDegrees(Math.atan((horizontalParam - centerShapeX)/(centerShapeY- verticalParam)))
    					+ this.selectedElement.getRotationDegree())%360;
    	// update rotation of shape
    	if (centerShapeY >= verticalParam){ // First and forth quadrant
    		this.selectedElement.setRotate(degree> 0? degree: 360 + degree, needStore);
    	}else {
    		this.selectedElement.setRotate((Math.toDegrees(Math.toRadians(180 + degree))), needStore);
    	}
    	// set cursor
    	Image image = textToImage(Math.round(this.selectedElement.getRotate()) + "°");
    	this.srRotate.setCursor(new ImageCursor(image, image.getWidth() / 2, image.getHeight() /2));
    	
    	// for dev, update debug info
    	this.rotateReporter.setText("Current cursor position: "+horizontalParam + " " + verticalParam
    							   +"\nCenter point:"+centerShapeX+" " + centerShapeY
    			                   +"\nCurrent angle: " + this.selectedElement.getRotate() + "°");
    	
    	// set boarder
    	borderOffsetCorrection();
    	
    }
    
    /**
     * helper function used when a shape change 
     * its shape irregularly and the border sizes
     * needs trigonometric update
     */
    private void borderOffsetCorrection() {
    	Double degreeFitOffset = this.selectedElement.getRotate() % 180;
    	if ((degreeFitOffset >= 0 && degreeFitOffset < 90)) {
    		this.selectedElement.setRotationLengthOffsetY((this.selectedElement.widthProperty().get() * Math.sin(Math.toRadians(degreeFitOffset)) + 
    				this.selectedElement.heightProperty().get() * Math.cos(Math.toRadians(degreeFitOffset))) - this.selectedElement.heightProperty().get());
    		this.selectedElement.setRotationLengthOffsetX((this.selectedElement.widthProperty().get() * Math.cos(Math.toRadians(degreeFitOffset)) + 
    				this.selectedElement.heightProperty().get() * Math.sin(Math.toRadians(degreeFitOffset))) - this.selectedElement.widthProperty().get());
    	}else if ((degreeFitOffset >= 90 && degreeFitOffset < 180)){
    		this.selectedElement.setRotationLengthOffsetY((this.selectedElement.heightProperty().get() * Math.sin(Math.toRadians(degreeFitOffset - 90)) +
    									  this.selectedElement.widthProperty().get() * Math.cos(Math.toRadians(degreeFitOffset - 90))) - 
    									  this.selectedElement.heightProperty().get());
    		this.selectedElement.setRotationLengthOffsetX((this.selectedElement.widthProperty().get() * Math.sin(Math.toRadians(degreeFitOffset - 90)) +
    									  this.selectedElement.heightProperty().get() * Math.cos(Math.toRadians(degreeFitOffset - 90))) -
    									  this.selectedElement.widthProperty().get());
    	}
		
	}

	/**
     * Helper function to generate image based on string
     * @param inputText the input string
     * @return a image containing inputText
     */
    private WritableImage textToImage(String inputText) {
        Text textImageContent = new Text(inputText);
        textImageContent.setFont(Font.font ("Verdana", 20));
        return textImageContent.snapshot(null, null);
    }
    
    /**
     * function for changing the horizontal length of shape
     * @param horizontalParam
     * @param needOffset
     */
    void setHSize(double horizontalParam, boolean needOffset) {
        double x = this.selectedElement.getLayoutX(), w = this.selectedElement.widthProperty().get(), width;
        double hardLimitForResize = this.resizeBlockWidth * 3;
        if (horizontalParam < this.area.getMinX()) horizontalParam = this.area.getMinX();
        if (horizontalParam > this.area.getMaxX()) horizontalParam = this.area.getMaxX();
        if (needOffset) {
            width = w + x - horizontalParam;
            if (width < hardLimitForResize) { width = hardLimitForResize; horizontalParam = x + w - hardLimitForResize; }
            this.selectedElement.setLayoutX(horizontalParam);
        } else {
            width = horizontalParam - x;
            if (width < hardLimitForResize) width = hardLimitForResize;
        }
        this.selectedElement.widthProperty().set(width);
        this.selectedElement.setShapeNameCirclesAndEllipses();
        borderOffsetCorrection();
    }

    /**
     * function for changing the vertical length of shape
     * @param verticalParam
     * @param needOffset
     */
    void setVSize(double verticalParam, boolean needOffset) {
        double y = this.selectedElement.getLayoutY(), h = this.selectedElement.heightProperty().get(), height;
        double hardLimitForResize = this.resizeBlockWidth * 3;
        if (verticalParam < this.area.getMinY()) verticalParam = this.area.getMinY();
        if (verticalParam > this.area.getMaxY()) verticalParam = this.area.getMaxY();
        if (needOffset) {
            height = h + y - verticalParam;
            if (height < hardLimitForResize) { height = hardLimitForResize; verticalParam = y + h - hardLimitForResize; }
            this.selectedElement.setLayoutY(verticalParam);
        } else {
            height = verticalParam - y;
            if (height < hardLimitForResize) height = hardLimitForResize;
        }
        this.selectedElement.heightProperty().set(height);
        this.selectedElement.setShapeNameCirclesAndEllipses();
        borderOffsetCorrection();
    }
    
    void setCenter(double horizontalParam, double verticalParam) {
    	// TODO
    }

    /**
     * function for moving the dragable shape
     * @param x
     * @param y
     */
    public void relocate(double x, double y) {
    	double maxX = this.area.getMaxX() - this.srBnd.getWidth() + this.selectedElement.getRotationLengthOffsetX()/2;
        double maxY = this.area.getMaxY() - this.srBnd.getHeight() + this.selectedElement.getRotationLengthOffsetY()/2;
        if (x < this.area.getMinX() + this.selectedElement.getRotationLengthOffsetX()/2) x = this.area.getMinX() + this.selectedElement.getRotationLengthOffsetX()/2;
        if (y < this.area.getMinY() + this.selectedElement.getRotationLengthOffsetY()/2) y = this.area.getMinY() + this.selectedElement.getRotationLengthOffsetY()/2;
        if (x > maxX) x = maxX;
        if (y > maxY) y = maxY;
        this.selectedElement.setLayoutX(x);
        this.selectedElement.setLayoutY(y);
    }


	Dragable createElement(double x, double y, double width, double height, Paint fill, String shapeName) {
		  Dragable element = new Dragable(x, y, fill, shapeName, width, height, shapesMenu.isCharMenuShowing());
	      element.setOnMousePressed(me -> {
		      select(element);
		      element.setViewOrder(2);
		      srBnd.fireEvent(me);
		      me.consume();
	      });
	      element.setOnMouseDragged(me -> srBnd.fireEvent(me));
	      element.setOnMouseReleased(me -> srBnd.fireEvent(me));
	      element.boundsInParentProperty().addListener((v, o, n) -> updateOverlay());
	      return element;
	 }
	Dragable createElement(double x, double y, Dragable copied_element) {
		Dragable element = new Dragable(x, y, copied_element);
		element.setOnMousePressed(me -> {
		      select(element);
		      element.setViewOrder(2);
		      srBnd.fireEvent(me);
		      me.consume();
		});
		element.setOnMouseDragged(me -> srBnd.fireEvent(me));
		element.setOnMouseReleased(me -> srBnd.fireEvent(me));
		element.boundsInParentProperty().addListener((v, o, n) -> updateOverlay());
		return element;
	}
}