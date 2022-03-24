
package application;

import javafx.event.EventHandler;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

/**  cette classe permet de gerer les evenement relies à MouseDragging qui nous permet de deplacer le graphe */
public class PanControler {

	/** filtre par defaut ( le meme que pour le zoomController) */
	public static final EventHandler<MouseEvent> DEFAULT_FILTER = ZoomControler.DEFAULT_FILTER;

	private final EventControler handlerManager;

	private final ValueAxis<?> xAxis;
	private final ValueAxis<?> yAxis;
	private final ChartCoordonnee chartCoordonnee;

	private AxeDuZoom axeDuZoom = AxeDuZoom.None;

	private EventHandler<? super MouseEvent> mouseFilter = DEFAULT_FILTER;

	private boolean dragging = false;

	private boolean wasXAnimated;
	private boolean wasYAnimated;

	private double lastX;
	private double lastY;

	public PanControler(XYChart<?, ?> chart ) {
		handlerManager = new EventControler( chart );
		xAxis = (ValueAxis<?>) chart.getXAxis();
		yAxis = (ValueAxis<?>) chart.getYAxis();
		chartCoordonnee = new ChartCoordonnee( chart, chart );
		/**
		 * dans ce qui suit on ajoute tous les evenements de la souris
		 */
		handlerManager.addEventHandler( false, MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( passesFilter( mouseEvent ) )
					startDrag( mouseEvent );
			}
		} );

		handlerManager.addEventHandler( false, MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				drag( mouseEvent );
			}
		} );

		handlerManager.addEventHandler( false, MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				release();
			}
		} );
	}

	public void setMouseFilter( EventHandler<? super MouseEvent> mouseFilter ) {
		this.mouseFilter = mouseFilter;
	}

	public void start() {
		handlerManager.addAllHandlers();
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

	/** quand on commence le deplacement (DRAGGING) */
	private void startDrag( MouseEvent event ) {
		EventZone eventZone = new EventZone(chartCoordonnee, event.getX(), event.getY() );
		axeDuZoom = eventZone.getZoomAxe(  );

		if (axeDuZoom != AxeDuZoom.None) {
			lastX = event.getX();
			lastY = event.getY();

			wasXAnimated = xAxis.getAnimated();
			wasYAnimated = yAxis.getAnimated();

			xAxis.setAnimated( false );
			xAxis.setAutoRanging( false );
			yAxis.setAnimated( false );
			yAxis.setAutoRanging( false );

			dragging = true;
		}
	}

	/** deplacement du graphe et calcul des valeurs des axes X et Y selon l'axe sur lequel on effectue le MouseDragging */
	private void drag( MouseEvent event ) {
		if ( !dragging )
			return;

		/**  deplacement du graphe horizontalement */
		if ( axeDuZoom == AxeDuZoom.HorVer || axeDuZoom == AxeDuZoom.Horizontal) {
			double dX = ( event.getX() - lastX ) / -xAxis.getScale();
			lastX = event.getX();
			xAxis.setAutoRanging( false );
			xAxis.setLowerBound( xAxis.getLowerBound() + dX );
			xAxis.setUpperBound( xAxis.getUpperBound() + dX );
		}

		/** deplacement du graphe verticalement */
		if ( axeDuZoom == AxeDuZoom.HorVer || axeDuZoom==AxeDuZoom.Vertical ){
			double dY = ( event.getY() - lastY ) / -yAxis.getScale();
			lastY = event.getY();
			yAxis.setAutoRanging( false );
			yAxis.setLowerBound( yAxis.getLowerBound() + dY );
			yAxis.setUpperBound( yAxis.getUpperBound() + dY );
		}
	}
	 /**  quand on relache la souris  */
	private void release() {
		if ( !dragging )
			return;

		dragging = false;

		xAxis.setAnimated( wasXAnimated );
		yAxis.setAnimated( wasYAnimated );
	}
}
