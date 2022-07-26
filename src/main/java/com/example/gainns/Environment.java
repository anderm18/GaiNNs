package com.example.gainns;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Environment extends Application {
	// resize storage param
	double resizeBlockWidth = 6, resizeBlockWidthOffset = resizeBlockWidth / 2;
    double lastX, lastY;
    double pressedMousePosX, pressedMousePosY, shapeLayoutX, shapeLayoutY, sWidth, sHeight;
    private Group overlay = null;
    private AnchorPane root;
    
    // scale change point
    private Rectangle srBnd, srNW, srN, srNE, srE, srSE, srS, srSW, srW;
    private double rotationLengthOffsetY = 0;
    private double rotationLengthOffsetX = 0;
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
    
    private Rectangle createFloor(Scene scene) {
        Rectangle rectangle = new Rectangle(windowWidth, 100);
        rectangle.widthProperty().bind(scene.widthProperty()); //keep as wide as window
        rectangle.setFill(Color.valueOf("#F5E799"));
        return rectangle;
    }

    //Menu bar is good for creating for general menus, not really for drag and drop. We probably
    //should use it to make a general menu (File, Edit, Help, etc).
    
    @Override
    public void start(Stage stage) throws IOException { 	
        this.root = new AnchorPane(); //AnchorPane had better functions then border pane
        
        Scene scene = new Scene(root, windowWidth, windowHeight);
        Rectangle floorRect = createFloor(scene); //floor
        Rectangle sceneRect = new Rectangle(windowWidth, windowHeight); //env range
        sceneRect.widthProperty().bind(scene.widthProperty()); //keep as wide as window
        sceneRect.heightProperty().bind(scene.heightProperty()); //keep as high as window
        sceneRect.setFill(Color.valueOf("#99F0F5"));
        
        // set layer and position for of base env
        HBox floor = new HBox(0, floorRect);
        floor.setViewOrder(3);
        HBox env = new HBox(0, sceneRect);
        env.setViewOrder(4);
        
        // deselecting overlay
        env.setOnMousePressed(me -> select(null));
        floor.setOnMousePressed(me -> select(null));
        
        //menu for shapes
        ShapesMenu shapesMenu = new ShapesMenu(); 
        shapesMenu.createMenu(scene);
        HBox sMenu = new HBox(0, shapesMenu.getMenu().shapeVisualizedList);
        sMenu.setPickOnBounds(false);
        HBox tab = new HBox(0, shapesMenu.getTab()); //tab to close menu
        
        // config layout
        root.getChildren().addAll(env, floor, sMenu, tab, 
        		                  posReporter, elementInEnvReporter, dragAndDropReporter, rotateReporter);
        AnchorPane.setBottomAnchor(floor, 0d); // positioning shapes in scene									
        AnchorPane.setTopAnchor(tab, 120d);
        AnchorPane.setBottomAnchor(posReporter, 65d);
        AnchorPane.setBottomAnchor(dragAndDropReporter, 50d);
        AnchorPane.setBottomAnchor(elementInEnvReporter, 0d);
        AnchorPane.setBottomAnchor(rotateReporter, 110d);
        AnchorPane.setLeftAnchor(tab, scene.getWidth()/2.0 - shapesMenu.getTab().getWidth()/2.0);
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
            	//AnchorPane.setTopAnchor(tab, 0d);
                TranslateTransition tt = new TranslateTransition(Duration.millis(250), tab);
                tt.setByY(-120f);
                tt.setCycleCount(1);
                //tt.setAutoReverse(true);
                tt.play();
            }
            else {
            	//AnchorPane.setTopAnchor(tab, 120d);
                TranslateTransition tt = new TranslateTransition(Duration.millis(250), tab);
                tt.setByY(120f);
                tt.setCycleCount(1);
                tt.play();
            }
         });
        
        
        // Shape drag and drop
        sceneRect.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
            	updateMousePosition(event);
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });

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
        stage.show();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> { // change pos of button(tab) when window changes
    		AnchorPane.setLeftAnchor(tab, ((double)newVal)/2.0 - shapesMenu.getTab().getWidth()/2.0);
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
        	// border
            this.srBnd.setX(this.selectedElement.getLayoutX() - this.rotationLengthOffsetX/2);
            this.srBnd.setY(this.selectedElement.getLayoutY() - this.rotationLengthOffsetY/2);
            this.srBnd.setWidth(this.selectedElement.widthProperty().get() + this.rotationLengthOffsetX);
            this.srBnd.setHeight(this.selectedElement.heightProperty().get() + this.rotationLengthOffsetY);
            // north west
            this.srNW.setX(this.selectedElement.getLayoutX() - this.rotationLengthOffsetX/2);
            this.srNW.setY(this.selectedElement.getLayoutY() - this.rotationLengthOffsetY/2);
            // north
            this.srN.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2) - this.resizeBlockWidthOffset);
            this.srN.setY(this.selectedElement.getLayoutY() - this.rotationLengthOffsetY/2);
            // north east
            this.srNE.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get()) - 
            		        this.resizeBlockWidth + this.rotationLengthOffsetX/2);
            this.srNE.setY(this.selectedElement.getLayoutY() - this.rotationLengthOffsetY/2);
            // east
            this.srE.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get()) - 
            		       this.resizeBlockWidth + this.rotationLengthOffsetX/2);
            this.srE.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get() / 2) - this.resizeBlockWidthOffset);
            // south east
            this.srSE.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get()) - 
            		        this.resizeBlockWidth + this.rotationLengthOffsetX/2);
            this.srSE.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get()) - 
            		        this.resizeBlockWidth + this.rotationLengthOffsetY/2);
            // south
            this.srS.setX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2) - this.resizeBlockWidthOffset);
            this.srS.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get()) - 
            		       this.resizeBlockWidth + this.rotationLengthOffsetY/2);
            // south west
            this.srSW.setX(this.selectedElement.getLayoutX() - this.rotationLengthOffsetX/2);
            this.srSW.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get()) - 
            		        this.resizeBlockWidth + this.rotationLengthOffsetY/2);
            // west
            this.srW.setX(this.selectedElement.getLayoutX() - this.rotationLengthOffsetX/2);
            this.srW.setY((this.selectedElement.getLayoutY() + this.selectedElement.heightProperty().get() / 2) - this.resizeBlockWidthOffset);
            // center dot
            this.srCen.setCenterX(this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2);
            this.srCen.setCenterY(this.selectedElement.getLayoutY()+ this.selectedElement.heightProperty().get() / 2);
            // rotate dot
            this.srRotate.setCenterX((this.selectedElement.getLayoutX() + this.selectedElement.widthProperty().get() / 2));
            this.srRotate.setCenterY(this.selectedElement.getLayoutY() - 10 - this.rotationLengthOffsetY/2);
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
    		if (node == this.srRotate) {
    			setRotate(me.getX(), me.getY(), true);
    			this.srRotate.setCursor(Cursor.OPEN_HAND);
    		}
    	});
        node.setOnMousePressed(me -> {
        	this.pressedMousePosX = me.getX();
        	this.pressedMousePosY = me.getY();
        	this.shapeLayoutX = this.selectedElement.getLayoutX();
        	this.shapeLayoutY = this.selectedElement.getLayoutY();
        	this.sWidth = this.selectedElement.widthProperty().get();
        	this.sHeight = this.selectedElement.heightProperty().get();
        	if (node == this.srRotate) this.srRotate.setCursor(Cursor.CLOSED_HAND);
            // me.consume();
        });
        node.setOnMouseDragged(me -> {
            double dx = (me.getX() - this.pressedMousePosX);
            double dy = (me.getY() - this.pressedMousePosY);
            Object source = me.getSource();
            if (source == this.srBnd) relocate(this.shapeLayoutX + dx, this.shapeLayoutY + dy);
            else if (source == this.srNW) { setHSize(this.shapeLayoutX + dx, true); setVSize(this.shapeLayoutY + dy, true); }
            else if (source == this.srN) setVSize(this.shapeLayoutY + dy, true);
            else if (source == this.srNE) { setHSize(this.shapeLayoutX + this.sWidth + dx, false); setVSize(this.shapeLayoutY + dy, true); }
            else if (source == this.srE) setHSize(this.shapeLayoutX + this.sWidth + dx, false);
            else if (source == this.srSE) { setHSize(this.shapeLayoutX + this.sWidth + dx, false); setVSize(this.shapeLayoutY + this.sHeight + dy, false); }
            else if (source == this.srS) setVSize(this.shapeLayoutY + this.sHeight + dy, false);
            else if (source == this.srSW) { setHSize(this.shapeLayoutX + dx, true); setVSize(this.shapeLayoutY + this.sHeight + dy, false); }
            else if (source == this.srW) setHSize(this.shapeLayoutX + dx, true);
            else if (source == this.srCen) setCenter(this.shapeLayoutX + dx, this.shapeLayoutY + dy);
            else if (source == this.srRotate) setRotate(me.getX(), me.getY(), false);
            me.consume();
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
    	Double degreeFitOffset = this.selectedElement.getRotate() % 180;
    	if ((degreeFitOffset >= 0 && degreeFitOffset < 90)) {
    		this.rotationLengthOffsetY = (this.selectedElement.widthProperty().get() * Math.sin(Math.toRadians(degreeFitOffset)) + 
    				this.selectedElement.heightProperty().get() * Math.cos(Math.toRadians(degreeFitOffset))) - this.selectedElement.heightProperty().get();
    		this.rotationLengthOffsetX = (this.selectedElement.widthProperty().get() * Math.cos(Math.toRadians(degreeFitOffset)) + 
    				this.selectedElement.heightProperty().get() * Math.sin(Math.toRadians(degreeFitOffset))) - this.selectedElement.widthProperty().get();
    	}else if ((degreeFitOffset >= 90 && degreeFitOffset < 180)){
    		this.rotationLengthOffsetY = (this.selectedElement.heightProperty().get() * Math.sin(Math.toRadians(degreeFitOffset - 90)) +
    									  this.selectedElement.widthProperty().get() * Math.cos(Math.toRadians(degreeFitOffset - 90))) - 
    									  this.selectedElement.heightProperty().get();
    		this.rotationLengthOffsetX = (this.selectedElement.widthProperty().get() * Math.sin(Math.toRadians(degreeFitOffset - 90)) +
    									  this.selectedElement.heightProperty().get() * Math.cos(Math.toRadians(degreeFitOffset - 90))) -
    									  this.selectedElement.widthProperty().get();
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
    }
    
    void setCenter(double horizontalParam, double verticalParam) {
    	
    }

    /**
     * function for moving the dragable shape
     * @param x
     * @param y
     */
    public void relocate(double x, double y) {
        double maxX = this.area.getMaxX() - this.srBnd.getWidth() + this.rotationLengthOffsetX/2;
        double maxY = this.area.getMaxY() - this.srBnd.getHeight() + this.rotationLengthOffsetY/2;
        if (x < this.area.getMinX() - this.rotationLengthOffsetX) x = this.area.getMinX() + this.rotationLengthOffsetX/2;
        if (y < this.area.getMinY() - this.rotationLengthOffsetY) y = this.area.getMinY() + this.rotationLengthOffsetY/2;
        if (x > maxX) x = maxX;
        if (y > maxY) y = maxY;
        this.selectedElement.setLayoutX(x);
        this.selectedElement.setLayoutY(y);
    }


	Dragable createElement(double x, double y, double width, double height, Paint fill, String shapeName) {
		  Dragable element = new Dragable(x, y, fill, shapeName, width, height);
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
