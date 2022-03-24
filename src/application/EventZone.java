package application;

/**
 * cette classe permet de savoir la zone de l'evenement ( sur l'axe X ou Y ou dans la zone de tracage du graphe
 */

public class EventZone {
	private final ChartCoordonnee chartCoordonnee;
	private final double x;
	private final double y;

	public EventZone(ChartCoordonnee chartInfo, double x, double y ) {
		this.chartCoordonnee = chartInfo;
		this.x = x;
		this.y = y;
	}


	public boolean isInXAxis() {
		return chartCoordonnee.getXAxisArea().contains( x, y );
	}


	public boolean isInYAxis() {
		return chartCoordonnee.getYAxisArea().contains( x, y );
	}


	public boolean isInGrapheZone() { return chartCoordonnee.getGrapheArea().contains( x, y );
	}

	public AxeDuZoom getZoomAxe( ) {
		if ( this.isInXAxis() )
			return AxeDuZoom.Horizontal;
		else if ( this.isInYAxis() )
			return AxeDuZoom.Vertical;
		else if ( this.isInGrapheZone() )
			return AxeDuZoom.HorVer;
		else
			return AxeDuZoom.None;
	}
}