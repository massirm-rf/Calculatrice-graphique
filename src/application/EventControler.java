
package application;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 *  cette class permet de gerer et ajouter un ensemble de d'enregistrement de gestionnaires d'evenement ( Event Handler) sur une
 *  cible (noeud)
 */

public class EventControler {
	private final Node noeud;
	private final List<Registration<? extends Event>> registrations;

	public EventControler(Node noeud ) {
		this.noeud = noeud;
		registrations = new ArrayList<Registration<? extends Event>>();
	}


	public <T extends Event> void addEventHandler( boolean ajoutImmediat, EventType<T> type,
	                                               EventHandler<? super T> handler ) {
		Registration<T> reg = new Registration<T>( type, handler );
		registrations.add( reg );
		if ( ajoutImmediat ) {
			noeud.addEventHandler( type, handler );
			reg.setEnregistre( true );
		}
	}


	public void addAllHandlers() {
		for ( Registration<?> registration : registrations ) {
			if ( !registration.estEnregistre() ) {
				noeud.addEventHandler( (EventType) registration.getType(),
				                        (EventHandler) registration.getHandler() );
				registration.setEnregistre( true );
			}
		}
	}


	private static class Registration<T extends Event> {
		private final EventType<T> type;
		private final EventHandler<? super T> handler;
		private boolean enregistre = false;

		public Registration( EventType<T> type, EventHandler<? super T> handler ) {
			if ( type == null )
			  throw new IllegalArgumentException( " erreur : type nul" );
			if ( handler == null )
			  throw new IllegalArgumentException( "erreur : handler nul" );

			this.type = type;
			this.handler = handler;
		}

		public EventType<T> getType() {
			return type;
		}

		public EventHandler<? super T> getHandler() {
			return handler;
		}

		public boolean estEnregistre() {
			return enregistre;
		}

		public void setEnregistre( boolean enregistre ) {
			this.enregistre = enregistre;
		}
	}
}