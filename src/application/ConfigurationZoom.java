package application;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class ConfigurationZoom {

	public static void replaceComponent( Node original, Node remplacant ) {
		Pane parent = (Pane) original.getParent();

		// on deplace les proprietes du noeud original vers le remplacant
		remplacant.getProperties().putAll( original.getProperties() );
		original.getProperties().clear();

		ObservableList<Node> children = parent.getChildren();
		BorderPane borderPane = (BorderPane) parent;
		if ( borderPane.getTop() == original ) {
			children.remove( original );
			borderPane.setTop( remplacant );

		} else if ( borderPane.getLeft() == original ) {
			children.remove( original );
			borderPane.setLeft( remplacant );

		} else if ( borderPane.getCenter() == original ) {
			children.remove( original );
			borderPane.setCenter( remplacant );

		} else if ( borderPane.getRight() == original ) {
			children.remove( original );
			borderPane.setRight( remplacant );

		} else if ( borderPane.getBottom() == original ) {
			children.remove( original );
			borderPane.setBottom( remplacant );
		}
	}


	/**
	 * configuration du zoom et quelques proprietes graphique du rectangle de selection
	 */
	public static void ZoomConfiguration(XYChart<?, ?> chart,
										 EventHandler<? super MouseEvent> mouseFilter, double Xmin,double Xmax, double Ymin,double Ymax ) {
		StackPane chartPane = new StackPane();

		if ( chart.getParent() != null )
			replaceComponent( chart, chartPane );

		Rectangle selectRect = new Rectangle( 0, 0, 0, 0 );
		selectRect.setFill( Color.DODGERBLUE );
		selectRect.setMouseTransparent( true );
		selectRect.setOpacity( 0.3 );
		selectRect.setStroke( Color.rgb( 0, 0x29, 0x66 ) );
		selectRect.setStrokeType( StrokeType.INSIDE );
		selectRect.setStrokeWidth( 3.0 );
		StackPane.setAlignment( selectRect, Pos.TOP_LEFT );

		chartPane.getChildren().addAll( chart, selectRect );

		ZoomControler zoomControl = new ZoomControler( chartPane, selectRect, chart );
		zoomControl.setMouseFilter( mouseFilter );
		zoomControl.addDoublePrimaryClickAutoRangeHandler( Xmin,Xmax,Ymin,Ymax);
		zoomControl.start();
	}



}