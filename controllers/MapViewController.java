/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
        PLACING_ARC,
        PLACING_TEXT,
        EDITING_POINT,
        EDITING_LINE,
        EDITING_ARC,
        EDITING_TEXT,
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
    private ToggleButton arcUtilsBtn;
    @FXML
    private ToggleButton textUtilsBtn;
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
    
    // Line editing controls
    @FXML
    private HBox editLineOptions;
    @FXML
    private ColorPicker editLineColorControl;
    @FXML
    private Slider editLineWidthControl;
    
    // Arc drawing controls
    @FXML
    private HBox arcOptions;
    @FXML
    private ColorPicker arcColorControl;
    @FXML
    private Slider arcWidthControl;
    // Temporary arc
    Arc drawingArc;
    
    // Text drawing controls
    @FXML
    private VBox textOptions;
    @FXML
    private ColorPicker textColorControl;
    @FXML
    private Slider textSizeControl;
    @FXML
    private TextField textValueControl;
    private Text drawingText;
    
    // Arc edit controls
    @FXML
    private HBox editArcOptions;
    @FXML
    private ColorPicker editArcColorControl;
    @FXML
    private Slider editArcWidthControl;
    
    // Text edit controls
    @FXML
    private VBox editTextOptions;
    @FXML
    private ColorPicker editTextColorControl;
    @FXML
    private Slider editTextSizeControl;
    @FXML
    private TextField editTextValueControl;
    
    // Node that is currently selected for editing
    private Node selectedNode;
    
    private ArrayList<Node> mapElements;
    private Group mapGroup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        graphics = canvas.getGraphicsContext2D();
        
        mapElements = new ArrayList<>();
        
        // Create a wrapping group, that is only used to help the underlying group
        // 'hold its shape' while it is being scaled and moved
        // Otherwise while zooming the map parts of the map will become unreachable
        Group wrapGroup = new Group();
        mapGroup = new Group();
        mapGroup.getChildren().add( mapImage );
        wrapGroup.getChildren().add( mapGroup );
        
        mapView.setContent( wrapGroup );
        
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
        
        // Bind listeners to arc editing controls
        editArcColorControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Arc ) {
                ((Arc)selectedNode).setStroke( nv );
            }
        });
        editArcWidthControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Arc ) {
                ((Arc)selectedNode).setStrokeWidth( nv.doubleValue() );
            }
        });
        
        // Bind listeners to text editing controls
        editTextColorControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Text ) {
                ((Text)selectedNode).setFill( nv );
            }
        });
        editTextSizeControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Text ) {
                ((Text)selectedNode).setFont( new Font( "System", nv.doubleValue() ) );
            }
        });
        editTextValueControl.textProperty().addListener( ( a, o, nv ) -> {
            if ( selectedNode instanceof Text ) {
                ((Text)selectedNode).setText( nv );
            }
        });
        
        // Bind listeners to text drawing controls
        textColorControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( drawingText != null ) {
                drawingText.setFill( nv );
            }
        });
        textSizeControl.valueProperty().addListener( ( a, o, nv ) -> {
            if ( drawingText != null ) {
                drawingText.setFont( new Font( "System", nv.doubleValue() ) );
            }
        });
        textValueControl.textProperty().addListener( ( a, o, nv ) -> {
            if ( drawingText != null ) {
                drawingText.setText( nv );
            }
        });
    }    
    
    @FXML
    private void onZoomOut(ActionEvent event) {
        zoom( -1 );
    }

    @FXML
    private void onZoomIn(ActionEvent event) {
        zoom( 1 );
    }
    
    @FXML
    private void onRemoveNode(ActionEvent event) {
        if ( selectedNode != null ) {
            mapGroup.getChildren().remove( selectedNode );
            currentAction.set( MapAction.NONE );
        }
    }
    
    private void onUtilChange( Toggle btn ) {
        if ( btn == pointUtilsBtn ) {
            currentAction.set( MapAction.PLACING_POINT );
        }
        else if ( btn == lineUtilsBtn ) {
            currentAction.set( MapAction.PLACING_LINE );
        }
        else if ( btn == arcUtilsBtn ) {
            currentAction.set( MapAction.PLACING_ARC );
        }
        else if ( btn == textUtilsBtn ) {
            currentAction.set( MapAction.PLACING_TEXT );
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
    
    private void addArc( double x, double y ) {
        if ( drawingArc == null ) {
            drawingArc = new Arc();
            drawingArc.setCenterX( x );
            drawingArc.setCenterY( y );
            drawingArc.setRadiusX( 10 );
            drawingArc.setRadiusY( 10 );
            drawingArc.setType( ArcType.OPEN );
            drawingArc.setStartAngle(0.0f); 
            drawingArc.setLength(180.0f); 
            drawingArc.setStroke( arcColorControl.getValue() );
            drawingArc.setStrokeWidth( arcWidthControl.getValue() );
            drawingArc.setFill( Color.TRANSPARENT );
            
            mapGroup.getChildren().add( drawingArc );
            mapGroup.setOnMouseMoved( a -> updateArcEnd( a ) );
        }
        else {
            drawingArc.setOnMouseClicked( ev -> onArcClicked( ev ) );
            drawingArc = null;
            mapGroup.setOnMouseMoved( null );
        }
    }
    
    private void startPlacingText() {
        drawingText = new Text();
        textValueControl.setText( "Text" );
        drawingText.setX( 0 );
        drawingText.setY( 0 );
        drawingText.setFill( textColorControl.getValue() );
        
        drawingText.setFont( new Font( "System", textSizeControl.getValue() ) );
        
        mapGroup.getChildren().add( drawingText );
        mapGroup.setOnMouseMoved( a -> updateText( a ) );
    }

    private void onActionChange( MapAction action ) {
        if ( action != MapAction.EDITING_POINT && selectedNode != null ) {
            setSelectedNode( null );
        }
        
        setNodeVisibility( pointOptions, false );
        setNodeVisibility( lineOptions, false );
        setNodeVisibility( editPointOptions, false );
        setNodeVisibility( editLineOptions, false );
        setNodeVisibility( arcOptions, false );
        setNodeVisibility( editArcOptions, false );
        setNodeVisibility( textOptions, false );
        setNodeVisibility( editTextOptions, false );
        
        if ( null != action ) switch (action) {
            case PLACING_POINT:
                setNodeVisibility( pointOptions, true );
                break;
            case PLACING_LINE:
                setNodeVisibility( lineOptions, true );
                break;
            case EDITING_POINT:
                setNodeVisibility( editPointOptions, true );
                break;
            case EDITING_LINE:
                setNodeVisibility( editLineOptions, true );
                break;
            case PLACING_ARC:
                setNodeVisibility( arcOptions, true );
                break;
            case EDITING_ARC:
                setNodeVisibility( editArcOptions, true );
                break;
            case PLACING_TEXT:
                setNodeVisibility( textOptions, true );
                startPlacingText();
                break;
            case EDITING_TEXT:
                setNodeVisibility( editTextOptions, true );
                break;
            default:
                break;
        }
    }
    
    private void setNodeVisibility( Node node, boolean isVisible ) {
        node.setVisible( isVisible );
        node.setManaged( isVisible );
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
        else if ( currentAction.get() == MapAction.PLACING_ARC ) {
            addArc( event.getX(), event.getY() );
        }
        else if ( currentAction.get() == MapAction.PLACING_TEXT ) {
            if ( drawingText.getText().length() > 0 ) {
                drawingText.setOnMouseClicked( e -> onTextClicked( e ) );
                drawingText = null;
                mapGroup.setOnMouseMoved( null );
                currentAction.set( MapAction.NONE );
                textUtilsBtn.setSelected( false );
            }
        }
    }
    
    private void updateLineEnd( MouseEvent event ) {
        drawingLine.setEndX( event.getX() );
        drawingLine.setEndY( event.getY() );
    }
    
    private void updateArcEnd( MouseEvent event ) {
        drawingArc.setRadiusX( Math.abs( drawingArc.getCenterX() - event.getX() ) );
        drawingArc.setRadiusY( Math.abs( drawingArc.getCenterY() - event.getY() ) );
    }
    
    private void updateText( MouseEvent event ) {
        drawingText.setX( event.getX() );
        drawingText.setY( event.getY() );
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
        selectedNode.getStyleClass().add( "selected-line" );
        
        editLineColorControl.setValue( (Color)c.getStroke());
        editLineWidthControl.setValue( c.getStrokeWidth() );
    }
    
    private void onArcClicked( MouseEvent event ) {
        if ( currentAction.get() != MapAction.NONE && !isEditing() ) return;
        
        Arc c = (Arc)event.getTarget();
        
        currentAction.set( MapAction.EDITING_ARC );
        
        setSelectedNode( (Node)event.getTarget() );
        selectedNode.getStyleClass().add( "selected-line" );
        
        editArcColorControl.setValue( (Color)c.getStroke());
        editArcWidthControl.setValue( c.getStrokeWidth() );
    }
    
    private void onTextClicked( MouseEvent event ) {
        if ( currentAction.get() != MapAction.NONE && !isEditing() ) return;
        
        Text t = (Text)event.getTarget();
        
        currentAction.set( MapAction.EDITING_TEXT );
        setSelectedNode( t );
        selectedNode.getStyleClass().add( "selected-line" );
        
        editTextColorControl.setValue( (Color)t.getFill() );
        editTextSizeControl.setValue( t.getFont().getSize() );
        editTextValueControl.setText( t.getText() );
    }
    
    private void setSelectedNode( Node nd ) {
        if ( selectedNode != null ) {
            selectedNode.getStyleClass().remove( "selected" );
            selectedNode.getStyleClass().remove( "selected-line" );
        }
        selectedNode = nd;
    }
    
    private boolean isEditing() {
        return currentAction.get() == MapAction.EDITING_LINE || 
                currentAction.get() == MapAction.EDITING_POINT ||
                currentAction.get() == MapAction.EDITING_ARC ||
                currentAction.get() == MapAction.EDITING_TEXT;
    }
    
}
