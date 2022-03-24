package application;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;


/**
 *  cette classe permet de fournir des informations ( des coordonnees) sur les zones du " XYChart <?,?> chart "
 *  et aussi dans le systeme de coordonees  du noeud de reference
 *.
 */


public class ChartCoordonnee {
	private final XYChart<?,?> chart;
	private final Node referenceNoeud;

	// Constructeur

	public ChartCoordonnee(XYChart<?, ?> chart, Node referenceNoeud ) {
		this.chart = chart;
		this.referenceNoeud = referenceNoeud;
	}


	public XYChart<?, ?> getChart() {
		return chart;
	}

	public Point2D getDataCoordinates( double x, double y ) {
		Axis xAxis = chart.getXAxis();
		Axis yAxis = chart.getYAxis();

		double xStart = getNewX( xAxis, referenceNoeud);
		double yStart = getNewY( yAxis, referenceNoeud);

		return new Point2D(
				xAxis.toNumericValue( xAxis.getValueForDisplay( x - xStart ) ),
		    yAxis.toNumericValue( yAxis.getValueForDisplay( y - yStart ) )
		);
	}

	/**
	 * 	retourne un rectangle avec les idmensions passees en argument
	 * 	on l'utilise pour recuperer le rectangle de selection ( zoom)
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return
	 */

	public Rectangle2D getDataCoordinates( double minX, double minY, double maxX, double maxY ) {
		if ( minX > maxX || minY > maxY ) {
			throw new IllegalArgumentException( "min > max for X and/or Y" );
		}

		Axis xAxis = chart.getXAxis();
		Axis yAxis = chart.getYAxis();

		double xStart = getNewX( xAxis, referenceNoeud);
		double yStart = getNewY( yAxis, referenceNoeud);

		double minDataX = xAxis.toNumericValue( xAxis.getValueForDisplay( minX - xStart ) );
		double maxDataX = xAxis.toNumericValue( xAxis.getValueForDisplay( maxX - xStart ) );

		double minDataY = yAxis.toNumericValue( yAxis.getValueForDisplay( maxY - yStart ) );
		double maxDataY = yAxis.toNumericValue( yAxis.getValueForDisplay( minY - yStart ) );

		return new Rectangle2D( minDataX,
		                        minDataY,
		                        maxDataX - minDataX,
		                        maxDataY - minDataY );
	}


	/**
	 * 	Renvoie la zone de tracage du graphe dans l'espace de coordonnees de la reference.
	 */
	public Rectangle2D getGrapheArea() {
		Axis<?> xAxis = chart.getXAxis();
		Axis<?> yAxis = chart.getYAxis();

		double xStart = getNewX( xAxis, referenceNoeud);
		double yStart = getNewY( yAxis, referenceNoeud);

		double width = xAxis.getWidth();
		double height = yAxis.getHeight();

		return new Rectangle2D( xStart, yStart, width, height );
	}


	/**
	 *	Renvoie la zone de l'axe X dans l'espace de coordonnees de la reference.
	 */
	public Rectangle2D getXAxisArea() {
		return getComponentArea( chart.getXAxis() );
	}

	/**
	 * Renvoie la zone de l'axe Y dans l'espace de coordonnees de la reference.
	 */
	public Rectangle2D getYAxisArea() {
		return getComponentArea( chart.getYAxis() );
	}

	private Rectangle2D getComponentArea( Region childRegion ) {
		double xStart = getNewX( childRegion, referenceNoeud);
		double yStart = getNewY( childRegion, referenceNoeud);

		return new Rectangle2D( xStart, yStart, childRegion.getWidth(), childRegion.getHeight() );
	}

	/**
	 *  Permet de trouver la coordonnee de X dans le systeme de  Parent telsque l'axe X=0 dans le systeme de coordonnee
	 * 	 de Child
	 * @param Child
	 * @param Parent
	 * @return
	 */

	public static double getNewX(Node Child, Node Parent ) {
		double newX = 0.0;
		Node inst = Child;
		while ( inst != Parent ) {
			newX += inst.getLocalToParentTransform().getTx();
			inst = inst.getParent();
			if ( inst == null )
				throw new IllegalArgumentException( "'Child' n'est pas un fils du noeud 'Parent'" );
		}
		return newX;
	}


	/**
	 * Permet de trouver la coordonnee de Y dans le systeme de  Parent telsque l'axe Y=0 dans le systeme de coordonnee
	 * de Child
	 * @param Child
	 * @param Parent
	 * @return
	 */

	public static double getNewY(Node Child, Node Parent ) {
		double newY = 0.0;
		Node inst = Child;
		while ( inst != Parent ) {
			newY += inst.getLocalToParentTransform().getTy();
			inst = inst.getParent();
			if ( inst == null )
				throw new IllegalArgumentException( "'Child' n'est pas un fils du noeud 'Parent" );
		}

		return newY;
	}

	public  Node getNode(){
		return referenceNoeud;
	}
}
