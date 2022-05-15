/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import utils.NotifUtils;

/**
 * FXML Controller class
 *
 * @author Aivaras Kriksciunas
 */
public class MapViewController implements Initializable {

    enum MapAction {
        PLACING_POINT,
        PLACING_LINE,
        EDITING_POINT,
        EDITING_LINE,
        NONE,
    }
    
    @FXML
    private ScrollPane mapView;
    @FXML
    private ImageView mapImage;
    
    @FXML
    private ToggleButton pointUtilsBtn;
    @FXML
    private ToggleButton lineUtilsBtn;
    @FXML
    private ToggleGroup utilSelection;
    
    // Point drawing controls
    @FXML
    private HBox pointOptions;
    @FXML
    private Slider pointSizeControl;
    @FXML
    private ColorPicker pointColorControl;
    
    // Line drawing controls
    @FXML
    private HBox lineOptions;
    @FXML
    private ColorPicker lineColorControl;
    @FXML
    private Slider lineWidthControl;
    // Temporary line
    Line drawingLine;
    
    // Point editing controls 
    @FXML
    private HBox editPointOptions;
    @FXML
    private ColorPicker editPointColorControl;
    @FXML
    private Slider editPointSizeControl;
    @FXML
    private Button removePoint;
    
    // Line editing controls
    @FXML
    private HBox editLineOptions;
    @FXML
    private ColorPicker editLineColorControl;
    @FXML
    private Slider editLineWidthControl;
    @FXML
    private Button removeLine;
    
    // Node that is currently selected for editing
    private Node selectedNode;
    
    private ArrayList<Node> mapElements;
    private Group mapGroup;

    private double scale = 1;
    
    private ObjectProperty<MapAction> currentAction;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            mapImage.setImage( new Image( "carta_nautica.jpg" ) );
        }
        catch ( Exception e ) {
            NotifUtils.showError( "Resource not found", "The required resources for the map are missing. Verify the installation and try again." );
        }
        
        mapElements = new ArrayList<>();
        
        // Create a wrapping group, that is only used to help the underlying group
        // 'hold its shape' while it is being scaled and moved
        // Otherwise while zooming the map parts of the map will become unreachable
        Group wrapGroup = new Group();
        mapGroup = new Group();
        mapGroup.getChildren().add( mapImage );
        wrapGroup.getChildren().add( mapGroup );
        
        mapView.setContent( wrapGroup );
        addCircle( 200, 200 );
        
        // Center map
        mapView.setHvalue( mapView.getHmax() / 2 );
        mapView.setVvalue( mapView.getVmax() / 2 );
        
        currentAction = new SimpleObjectProperty();
        currentAction.addListener( ( a, oldVal, newVal ) -> onActionChange( newVal ));
        currentAction.set( MapAction.NONE );
        
        utilSelection.selectedToggleProperty().addListener( ( a, old, newVal ) -> onUtilChange( newVal ) );
        
        mapGroup.setOnMouseClicked( ev -> onMapMouseUp( ev ) );
        
        // Bind listeners to point editing controls
        editPointColorControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Circle ) {
                ((Circle)selectedNode).setFill(nv );
            }
        });
        editPointSizeControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Circle ) {
                ((Circle)selectedNode).setRadius( nv.doubleValue() );
            }
        });
        removePoint.setOnAction( a -> {
            if ( selectedNode != null ) {
                mapGroup.getChildren().remove( selectedNode );
                currentAction.set( MapAction.NONE );
            }
        });
        
        // Bind listeners to line editing controls
        editLineColorControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Line ) {
                ((Line)selectedNode).setStroke( nv );
            }
        });
        editLineWidthControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Line ) {
                ((Line)selectedNode).setStrokeWidth( nv.doubleValue() );
            }
        });
        removeLine.setOnAction( a -> {
            if ( selectedNode != null ) {
                mapGroup.getChildren().remove( selectedNode );
                currentAction.set( MapAction.NONE );
            }
        });
    }    

    @FXML
    private void onZoom( ScrollEvent event ) {
        zoom( event.getDeltaY() ); 
    }
    
    @FXML
    private void onZoomOut(ActionEvent event) {
        zoom( -1 );
    }

    @FXML
    private void onZoomIn(ActionEvent event) {
        zoom( 1 );
    }
    
    private void onUtilChange( Toggle btn ) {
        if ( btn == pointUtilsBtn ) {
            currentAction.set( MapAction.PLACING_POINT );
        }
        else if ( btn == lineUtilsBtn ) {
            currentAction.set( MapAction.PLACING_LINE );
        }
        else {
            currentAction.set( MapAction.NONE );
        }
    }
    
    private void zoom( double direction ) {
        if ( direction > 0 ) {
            scale += 0.1;
        }
        else if ( direction < 0 ) {
            scale -= 0.1;
        }
        
        scale = Math.max( 0, scale );
        scale = Math.min( 5, scale );
        
        double scrollH = mapView.getHvalue();
        double scrollV = mapView.getVvalue();
        
        mapGroup.setScaleX( scale );
        mapGroup.setScaleY( scale );
        
        mapView.setVvalue( scrollV );
        mapView.setHvalue( scrollH );
        
        mapView.setHmax( mapGroup.getBoundsInParent().getHeight() );
    }

    private void addCircle( double x, double y ) {
        Circle circle = new Circle();
        circle.setCenterX( x );
        circle.setCenterY( y );
        circle.setRadius( pointSizeControl.getValue() );
        circle.setFill( pointColorControl.getValue() );
        circle.setOnMouseClicked( ev -> onPointClicked( ev ) );
        
        try {
            mapGroup.getChildren().add( circle );
        }
        catch ( Exception e ) {
            System.out.println( e.toString() );
        }
    }
    
    private void addLine( double x, double y ) {
        if ( drawingLine == null ) {
            drawingLine = new Line();
            drawingLine.setStartX( x );
            drawingLine.setStartY( y );
            drawingLine.setEndX( x );
            drawingLine.setEndY( y );
            drawingLine.setStroke( lineColorControl.getValue() );
            drawingLine.setStrokeWidth( lineWidthControl.getValue() );
            
            mapGroup.getChildren().add( drawingLine );
            mapGroup.setOnMouseMoved( a -> updateLineEnd( a ) );
        }
        else {
            drawingLine.setOnMouseClicked( ev -> onLineClicked( ev ) );
            drawingLine = null;
            mapGroup.setOnMouseMoved( null );
        }
    }

    private void onActionChange( MapAction action ) {
        if ( action != MapAction.EDITING_POINT && selectedNode != null ) {
            setSelectedNode( null );
        }
        
        pointOptions.setVisible( false );
        pointOptions.setManaged( false );
        lineOptions.setVisible( false );
        lineOptions.setManaged( false );
        editPointOptions.setVisible( false );
        editPointOptions.setManaged( false );
        editLineOptions.setVisible( false );
        editLineOptions.setManaged( false );
        
        if ( action == MapAction.PLACING_POINT ) {
            pointOptions.setVisible( true );
            pointOptions.setManaged( true );
        }
        else if ( action == MapAction.PLACING_LINE ) {
            lineOptions.setVisible( true );
            lineOptions.setManaged( true );
        }
        else if ( action == MapAction.EDITING_POINT ) {
            editPointOptions.setVisible( true );
            editPointOptions.setManaged( true );
        }
        else if ( action == MapAction.EDITING_LINE ) {
            editLineOptions.setVisible( true );
            editLineOptions.setManaged( true );
        }
    }
    
    private void onMapMouseUp(MouseEvent event) {
        
        if ( !event.isStillSincePress() ) {
            // Drag detected
            return;
        }
        
        if ( currentAction.get() == MapAction.PLACING_POINT ) {
            addCircle( event.getX(), event.getY() );
        }
        else if ( currentAction.get() == MapAction.PLACING_LINE ) {
            addLine( event.getX(), event.getY() );
        }
    }
    
    private void updateLineEnd( MouseEvent event ) {
        drawingLine.setEndX( event.getX() );
        drawingLine.setEndY( event.getY() );
    }
    
    private void onPointClicked( MouseEvent event ) {
        if ( currentAction.get() != MapAction.NONE && !isEditing() ) return;
        
        Circle c = (Circle)event.getTarget();
        
        currentAction.set( MapAction.EDITING_POINT );
        
        setSelectedNode( (Node)event.getTarget() );
        selectedNode.getStyleClass().add( "selected" );
        
        editPointColorControl.setValue( (Color)c.getFill() );
        editPointSizeControl.setValue( c.getRadius() );
    }
    
    private void onLineClicked( MouseEvent event ) {
        if ( currentAction.get() != MapAction.NONE && !isEditing() ) return;
        
        Line c = (Line)event.getTarget();
        
        currentAction.set( MapAction.EDITING_LINE );
        
        setSelectedNode( (Node)event.getTarget() );
        selectedNode.getStyleClass().add( "selected" );
        
        editLineColorControl.setValue( (Color)c.getStroke());
        editLineWidthControl.setValue( c.getStrokeWidth() );
    }
    
    private void setSelectedNode( Node nd ) {
        if ( selectedNode != null ) {
            selectedNode.getStyleClass().remove( "selected" );
        }
        selectedNode = nd;
    }
    
    private boolean isEditing() {
        return currentAction.get() == MapAction.EDITING_LINE || 
                currentAction.get() == MapAction.EDITING_POINT;
    }
}
