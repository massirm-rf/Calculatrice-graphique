
package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.lang.reflect.InvocationTargetException;

public class ZoomControler extends CalculatriceControler{

	public static final EventHandler<MouseEvent> DEFAULT_FILTER = new EventHandler<MouseEvent>() {
		@Override
		public void handle( MouseEvent mouseEvent ) {
			if ( mouseEvent.getButton() != MouseButton.PRIMARY )
				mouseEvent.consume();
		}
	};

	private EventHandler<? super MouseEvent> mouseFilter = DEFAULT_FILTER;

	/** layout (x,y) du rectangle de selection */
	private final SimpleDoubleProperty rectX = new SimpleDoubleProperty();
	private final SimpleDoubleProperty rectY = new SimpleDoubleProperty();

	/** si vrai, le rectangle du zoom est selectionne */
	private final SimpleBooleanProperty selecting = new SimpleBooleanProperty( false );

	/** le nombre de millisecondes que prend l'animation de zoom. */
	private final DoubleProperty zoomDurationMillis = new SimpleDoubleProperty( 750.0 );

	/** si vrai, on peut zoomer */
	private final BooleanProperty zoomAnimated = new SimpleBooleanProperty( true );

	/** si vrai, on zoom avec la roulette de la souris */
	private final BooleanProperty mouseWheelZoomAllowed = new SimpleBooleanProperty( true );
	
	private AxeDuZoom zoomAxe = AxeDuZoom.None;



	private final EventControler handlerManager;

	private final Rectangle selectRect;
	private final Axis<?> xAxis;
	private final DoubleProperty xAxisLowerBoundProperty;
	private final DoubleProperty xAxisUpperBoundProperty;
	private final Axis<?> yAxis;
	private final DoubleProperty yAxisLowerBoundProperty;
	private final DoubleProperty yAxisUpperBoundProperty;
	private final ChartCoordonnee chartCoordonnee;

	private final Timeline zoomAnimation = new Timeline();


	/**
	 *  Constructeur qui prend en argument :
	 * 	 Pane : le parent des autres arguments
	 * 	 un Rectangle avec un layout (X,Y) aligne avec le graphe (chart)
	 * 	 une Chart qui nous permet de controler les deux axes X,Y
	 */
	public <X,Y> ZoomControler(Pane chartPane, Rectangle selectRect, XYChart<X,Y> chart ) {
		this.selectRect = selectRect;
		this.xAxis = chart.getXAxis();
		this.xAxisLowerBoundProperty = getLowerBoundProperty(xAxis);
		this.xAxisUpperBoundProperty = getUpperBoundProperty(xAxis);
		this.yAxis = chart.getYAxis();
		this.yAxisLowerBoundProperty = getLowerBoundProperty(yAxis);
		this.yAxisUpperBoundProperty = getUpperBoundProperty(yAxis);

		if (
				xAxisLowerBoundProperty == null || xAxisUpperBoundProperty == null ||
						yAxisLowerBoundProperty == null || yAxisUpperBoundProperty == null
		) {
			throw new IllegalArgumentException("erreur type d'axe");
		}

		chartCoordonnee = new ChartCoordonnee( chart, chartPane );

		handlerManager = new EventControler( chartPane );

		/**
		 * dans ce qui suit on ajoute tous les evenements de la souris
		 */

		handlerManager.addEventHandler( false, MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( passesFilter( mouseEvent ) )
					MousePressed( mouseEvent );
			}
		} );

		handlerManager.addEventHandler( false, MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( passesFilter( mouseEvent ) )
					DragStart();
			}
		} );

		/**
		 * on ne verifie pas le filtre ici, soit nous avons deja commence le DRAG, soit non
		 */
		handlerManager.addEventHandler( false, MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				MouseDragged( mouseEvent );
			}
		} );

		handlerManager.addEventHandler( false, MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				MouseReleased();
			}
		} );

		handlerManager.addEventHandler( false, ScrollEvent.ANY, new MouseWheelZoom() );
	}

	public void setMouseFilter( EventHandler<? super MouseEvent> mouseFilter ) {
		this.mouseFilter = mouseFilter;
	}

	/**
	 * permet de gerer le zoom et ajouter tous les controleurs d'evenements
	 */

	public void start() {
		handlerManager.addAllHandlers();

		selectRect.widthProperty().bind( rectX.subtract( selectRect.translateXProperty() ) );
		selectRect.heightProperty().bind( rectY.subtract( selectRect.translateYProperty() ) );
		selectRect.visibleProperty().bind( selecting );
	}


	private boolean passesFilter( MouseEvent event ) {
		if ( mouseFilter != null ) {
			MouseEvent cloned = (MouseEvent) event.clone();
			mouseFilter.handle( cloned );
			 if ( cloned.isConsumed() )
				return false;
		}

		return true;
	}

	private void MousePressed(MouseEvent mouseEvent ) {

		/**
		 * on recupere les coordonnees de l'evenement de la souris
		 */
		double x = mouseEvent.getX();
		double y = mouseEvent.getY();

		Rectangle2D plotArea = chartCoordonnee.getGrapheArea();  // la zone de tracage
		EventZone eventZone = new EventZone( chartCoordonnee, x, y ); // zone de l'evenement
		zoomAxe = eventZone.getZoomAxe();  // axe de l'evenement

		/**
		 * on configure le rectangle selon l'axe du zoom
		 */
		if ( zoomAxe == AxeDuZoom.HorVer) {
			selectRect.setTranslateX( x );
			selectRect.setTranslateY( y );
			rectX.set( x );
			rectY.set( y );

		} else if ( zoomAxe == AxeDuZoom.Horizontal ) {
			selectRect.setTranslateX( x );
			selectRect.setTranslateY( plotArea.getMinY() );
			rectX.set( x );
			rectY.set( plotArea.getMaxY() );

		} else if ( zoomAxe == AxeDuZoom.Vertical ) {
			selectRect.setTranslateX( plotArea.getMinX() );
			selectRect.setTranslateY( y );
			rectX.set( plotArea.getMaxX() );
			rectY.set( y );
		}
	}


	private void DragStart() {
		if ( zoomAxe != AxeDuZoom.None )
			selecting.set( true );
	}

	/**
	 * on presse et on glisse la souris au meme temps
	 * @param mouseEvent
	 */
	private void MouseDragged( MouseEvent mouseEvent ) {
		if ( !selecting.get() )
			return;

		Rectangle2D plotArea = chartCoordonnee.getGrapheArea();

		if ( zoomAxe == AxeDuZoom.HorVer || zoomAxe == AxeDuZoom.Horizontal ) {
			double x = mouseEvent.getX();

			/** on presse */
			x = Math.max( x, selectRect.getTranslateX() );

			/** on glisse la souris pour selectionner */
			x = Math.min( x, plotArea.getMaxX() );
			rectX.set( x );
		}

		if ( zoomAxe == AxeDuZoom.HorVer || zoomAxe == AxeDuZoom.Vertical ) {
			double y = mouseEvent.getY();

			/** on presse */
			y = Math.max( y, selectRect.getTranslateY() );

			/** on glisse la souris pour selectionner */
			y = Math.min( y, plotArea.getMaxY() );
			rectY.set( y );
		}
	}

	/**
	 * quand on relache la souris
	 */
	private void MouseReleased() {
		if ( !selecting.get() )
			return;


		if ( selectRect.getWidth() == 0.0 || selectRect.getHeight() == 0.0 ) { /** rien n'est selectionne */
			selecting.set( false );
			return;
		}
		/**
		 * on recupere le rectangle selectionne
		 */
		Rectangle2D zoomWindow = chartCoordonnee.getDataCoordinates(
				selectRect.getTranslateX(), selectRect.getTranslateY(),
				rectX.get(), rectY.get()
		);

		xAxis.setAutoRanging( false );
		yAxis.setAutoRanging( false );
		if ( zoomAnimated.get() ) { /** configuration des deux axes selon les dimension du rectangle selectionne */
			zoomAnimation.stop();
			zoomAnimation.getKeyFrames().setAll(
					new KeyFrame( Duration.ZERO,
							new KeyValue( xAxisLowerBoundProperty, getXAxisLowerBound() ),
							new KeyValue( xAxisUpperBoundProperty, getXAxisUpperBound() ),
							new KeyValue( yAxisLowerBoundProperty, getYAxisLowerBound() ),
							new KeyValue( yAxisUpperBoundProperty, getYAxisUpperBound() )
					),
					new KeyFrame( Duration.millis( zoomDurationMillis.get() ),
							new KeyValue( xAxisLowerBoundProperty, zoomWindow.getMinX() ),
							new KeyValue( xAxisUpperBoundProperty, zoomWindow.getMaxX() ),
							new KeyValue( yAxisLowerBoundProperty, zoomWindow.getMinY() ),
							new KeyValue( yAxisUpperBoundProperty, zoomWindow.getMaxY() )
					)
			);
			zoomAnimation.play();
		} else {
			zoomAnimation.stop();
			setXAxisLowerBound( zoomWindow.getMinX() );
			setXAxisUpperBound( zoomWindow.getMaxX() );
			setYAxisLowerBound( zoomWindow.getMinY() );
			setYAxisUpperBound( zoomWindow.getMaxY() );
		}

		selecting.set( false );
	}

	/**
	 * retourne 0.0 quand le zoom/dezoom est effectue sur l'axe X ou Y
	 * Sinon retourne (val - min) / (max - min)
	 */
	private static double getBalance( double val, double min, double max ) {
		if ( val <= min )
			return 0.0;

		return (val - min) / (max - min);
	}

	/**
	 * le zoom avec la roulette de la souris
	 */
	private class MouseWheelZoom implements EventHandler<ScrollEvent> {
		private boolean scroll = false;

		@Override
		public void handle( ScrollEvent event ) {
			EventType<? extends Event> eventType = event.getEventType();
			if ( eventType == ScrollEvent.SCROLL_STARTED ) {
				scroll = true;
			} else if ( eventType == ScrollEvent.SCROLL_FINISHED ) {
				scroll = false;

			} else if ( eventType == ScrollEvent.SCROLL && mouseWheelZoomAllowed.get() && !scroll
					&& !event.isInertia() && event.getDeltaY() != 0 && event.getTouchCount() == 0 ) {

				/**
				 * recuperer les coordonnee de l'evenement  par rapport a MouseEvent
				 */
				double eventX = event.getX();
				double eventY = event.getY();

				EventZone eventZone = new EventZone( chartCoordonnee, eventX, eventY );
				AxeDuZoom axeZoom = eventZone.getZoomAxe(  );

				if ( axeZoom == AxeDuZoom.None )
					return;

				zoomAnimation.stop();

				
				Point2D dataCoords = chartCoordonnee.getDataCoordinates( eventX, eventY );

				double xZoomBalance = getBalance( dataCoords.getX(),
						getXAxisLowerBound(), getXAxisUpperBound() );
				double yZoomBalance = getBalance( dataCoords.getY(),
						getYAxisLowerBound(), getYAxisUpperBound() );

				/** event.getDeltaY() > 0 : zoom => direction= -1.0 => zoomAmount <0 => les valeurs des axes --
				 *  event.getDeltaY() < 0 : dezoom => direction= 1.0  => zoomAmount >0 => les valeurs des axes ++
				 */

				/** signum du nombre de pixels pour faire défiler */
				double direction = -Math.signum( event.getDeltaY() );
				double zoomAmount = 0.3 * direction;

				if ( axeZoom == AxeDuZoom.HorVer || axeZoom == AxeDuZoom.Horizontal ) {
					double xZoomDelta = ( getXAxisUpperBound() - getXAxisLowerBound() ) * zoomAmount;
					xAxis.setAutoRanging( false );
					setXAxisLowerBound( getXAxisLowerBound() - xZoomDelta * xZoomBalance );
					setXAxisUpperBound( getXAxisUpperBound() + xZoomDelta * ( 1 - xZoomBalance ) );
				}

				if ( axeZoom == AxeDuZoom.HorVer || axeZoom == AxeDuZoom.Vertical ) {
					double yZoomDelta = ( getYAxisUpperBound() - getYAxisLowerBound() ) * zoomAmount;
					yAxis.setAutoRanging( false );
					setYAxisLowerBound( getYAxisLowerBound() - yZoomDelta * yZoomBalance );
					setYAxisUpperBound( getYAxisUpperBound() + yZoomDelta * ( 1 - yZoomBalance ) );
				}
			}
		}
	}

	/**
	 * ajouter le gestionnaire de double clic pour effectuer un autoRanging
	 */
	public  EventHandler<MouseEvent> addDoublePrimaryClickAutoRangeHandler( double Xmin,double Xmax, double Ymin,double Ymax) {
		EventHandler<MouseEvent> handler = getDoublePrimaryClickAutoRangeHandler(Xmin,Xmax,Ymin,Ymax );
		chartCoordonnee.getNode().addEventHandler( MouseEvent.MOUSE_CLICKED, handler );
		return handler;
	}

	/**
	 * efectuer un autoZoom lors d'un double clic avec le bouton gauche de la souris
	 */
	public  EventHandler<MouseEvent> getDoublePrimaryClickAutoRangeHandler(  double Xmin,double Xmax, double Ymin,double Ymax ) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent event ) {
				if ( event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY ) {
					double x = event.getX();
					double y = event.getY();
					if (!chartCoordonnee.getXAxisArea().contains(x, y)){
						setYAxisLowerBound(Ymin);
						setYAxisUpperBound(Ymax);
					}
					if ( !chartCoordonnee.getYAxisArea().contains( x, y ) ) {
						setXAxisLowerBound(Xmin);
						setXAxisUpperBound(Xmax);
					}
				}
			}
		};
	}

	


	private double getXAxisLowerBound() {
		return xAxisLowerBoundProperty.get();
	}

	private void setXAxisLowerBound(double val) {
		xAxisLowerBoundProperty.set(val);
	}

	private double getXAxisUpperBound() {
		return xAxisUpperBoundProperty.get();
	}

	private void setXAxisUpperBound(double val) {
		xAxisUpperBoundProperty.set(val);
	}

	private double getYAxisLowerBound() {
		return yAxisLowerBoundProperty.get();
	}

	private void setYAxisLowerBound(double val) {
		yAxisLowerBoundProperty.set(val);
	}

	private double getYAxisUpperBound() {
		return yAxisUpperBoundProperty.get();
	}

	private void setYAxisUpperBound(double val) {
		yAxisUpperBoundProperty.set(val);
	}

	public static <T> DoubleProperty getLowerBoundProperty( Axis<T> axis ) {
		return axis instanceof ValueAxis ?
				((ValueAxis<?>) axis).lowerBoundProperty() :
				toDoubleProperty(axis, ZoomControler.<T>getProperty(axis, "lowerBoundProperty") );
	}

	public static <T> DoubleProperty getUpperBoundProperty( Axis<T> axis ) {
		return axis instanceof ValueAxis ?
				((ValueAxis<?>) axis).upperBoundProperty() :
				toDoubleProperty(axis, ZoomControler.<T>getProperty(axis, "upperBoundProperty") );
	}

	private static <T> DoubleProperty toDoubleProperty( final Axis<T> axis, final Property<T> property ) {
		final ChangeListener<Number>[] doubleChangeListenerAry = new ChangeListener[1];
		final ChangeListener<T>[] realValListenerAry = new ChangeListener[1];

		final DoubleProperty result = new SimpleDoubleProperty() {
			private final Object[] listeners = new Object[] {
					doubleChangeListenerAry, realValListenerAry
			};
		};

		doubleChangeListenerAry[0] = new ChangeListener<Number>() {
			@Override
			public void changed( ObservableValue<? extends Number> observable, Number oldValue, Number newValue ) {
				property.removeListener( realValListenerAry[0] );
				property.setValue( axis.toRealValue(
						newValue == null ? null : newValue.doubleValue() )
				);
				property.addListener( realValListenerAry[0] );
			}
		};
		result.addListener(doubleChangeListenerAry[0]);

		realValListenerAry[0] = new ChangeListener<T>() {
			@Override
			public void changed( ObservableValue<? extends T> observable, T oldValue, T newValue ) {
				result.removeListener( doubleChangeListenerAry[0] );
				result.setValue( axis.toNumericValue( newValue ) );
				result.addListener( doubleChangeListenerAry[0] );
			}
		};
		property.addListener(realValListenerAry[0]);

		return result;
	}

	private static <T> Property<T> getProperty( Object object, String method ) {
		try {
			Object result = object.getClass().getMethod(method).invoke(object);
			return result instanceof Property ? (Property<T>) result : null;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored ) {}

		return null;
	}

}
