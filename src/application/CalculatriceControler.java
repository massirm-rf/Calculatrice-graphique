package application;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Optional;


public class CalculatriceControler implements Serializable {
	Stage stage;
	@FXML
	private Scene root;

	@FXML
	private Button par1;

	@FXML
	private Button par2;

	@FXML
	private TextArea txt;

	@FXML
	private TextArea txt2;

	@FXML
	private TextArea txt3;
	@FXML
	private Button tracer2;
	@FXML
	private Button tracer3;
	@FXML
	private Button tracer;

	@FXML
	private Button sup;
	@FXML
	private Button sup2;
	@FXML
	private Button sup3;

	@FXML
	private Button auto;

	@FXML
	private LineChart<Double, Double> chart;

	@FXML
	private XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();

	@FXML
	private TextField iMin1;

	@FXML
	private TextField iMax1;

	@FXML
	private TextField iMin2;

	@FXML
	private TextField iMax2;

	@FXML
	private TextField iMin3;

	@FXML
	private TextField iMax3;

	@FXML
	private NumberAxis XAxis;

	@FXML
	private NumberAxis YAxis;

	@FXML
	private Button sauv1;
	@FXML
	private Button sauv2;
	@FXML
	private Button sauv3;

	@FXML
	private Button historique;

	@FXML
	private Button reduire;

	@FXML
	private Button redimention;

	@FXML
	private Button quitter;
	
	@FXML
	private Button nouveau;

	@FXML
	private Line indCF;
	@FXML
	private Line indCG;
	@FXML
	private Line indCH;

	@FXML
	private Text indF;
	@FXML
	private Text indG;
	@FXML
	private Text indH;
	
	
	private boolean bool = true;

	private ListeUsers listeUsers = new ListeUsers();

	private User user;

	private double imin;
	private double imax;
	private int n = 0;

	private double x;
	private double y;

	Parser parser;

	private String[] color = {"blue", "green", "red"};
	static int ind = -1;
	private LinkedList<XYChart.Series<Double, Double>> list1 = new LinkedList<>();
	private LinkedList<XYChart.Series<Double, Double>> list2 = new LinkedList<>();
	private LinkedList<XYChart.Series<Double, Double>> list3 = new LinkedList<>();

	private LinkedList<LinkedList<XYChart.Series<Double, Double>>> listesDeSeries = new LinkedList<>();

	@FXML
	public void tracer() throws InputException {
		
		autoZoom();
		String name = "";
		switch (n) {
			case 0:
				name = txt.getText();
				break;
			case 1:
				name = txt2.getText();
				break;
			case 2:
				name = txt3.getText();
				break;
			default:
				break;
		}


		if (!name.equals("")) {
			parser = new Parser(name.trim());
			try {
				if(imin>imax) {
					genererBoiteDialogue("", "imax<imin", "warning");
					return;
				}
				dessiner2();
			} catch (InputException e) {
				Alert dialog = new Alert(Alert.AlertType.WARNING);
				dialog.setTitle("Alerte");
				dialog.setHeaderText("Merci de revérifier votre saisie");
				dialog.showAndWait();
			}

			listesDeSeries.get(n).forEach(serie -> {
				chart.getData().add(serie);
					serie.getNode().setStyle("-fx-stroke: " + color[n] + ";");
			});
		}
	}

	public void action0() throws InputException {
		n = 0;
		String s = iMin1.getText();
		String s1 = iMax1.getText();
		try {
			imin = (!s.equals("")) ? Double.parseDouble(s) : Double.parseDouble(iMin1.getPromptText());
			imax = (!s1.equals("")) ? Double.parseDouble(s1) : Double.parseDouble(iMax1.getPromptText());
			chart.getData().removeAll(listesDeSeries.get(0));
			listesDeSeries.get(0).clear();
			tracer();
		} catch (NumberFormatException e) {
			genererBoiteDialogue("Dessin echoué", "L'intervalle est non Valide", "warning");
		}
		if (listesDeSeries.get(0).size() != 0) {
			indCF.setVisible(true);
			indF.setVisible(true);
		}
	}

	public void action1() throws InputException {
		n = 1;
		String s = iMin2.getText();
		String s1 = iMax2.getText();
		try {
			imin = (!s.equals("")) ? Double.parseDouble(s) : Double.parseDouble(iMin2.getPromptText());
			imax = (!s1.equals("")) ? Double.parseDouble(s1) : Double.parseDouble(iMax2.getPromptText());
			chart.getData().removeAll(listesDeSeries.get(1));
			listesDeSeries.get(1).clear();
			tracer();
		} catch (NumberFormatException e) {
			genererBoiteDialogue("Dessin echoué", "L'intervalle est non Valide", "warning");
		}
		if (listesDeSeries.get(1).size() != 0) {
			indCG.setVisible(true);
			indG.setVisible(true);
		}
	}

	public void action2() throws InputException {
		n = 2;
		String s = iMin3.getText();
		String s1 = iMax3.getText();
		try {
			imin = (!s.equals("")) ? Double.parseDouble(s) : Double.parseDouble(iMin3.getPromptText());
			imax = (!s1.equals("")) ? Double.parseDouble(s1) : Double.parseDouble(iMax3.getPromptText());
			chart.getData().removeAll(listesDeSeries.get(2));
			listesDeSeries.get(2).clear();
			tracer();
		} catch (NumberFormatException e) {
			genererBoiteDialogue("Dessin echoué", "L'intervalle est non Valide", "warning");
		}
		if (listesDeSeries.get(2).size() != 0) {
			indCH.setVisible(true);
			indH.setVisible(true);
		}
	}

	public void suppGraph() {
		if (listesDeSeries.size() != 0) {
			chart.getData().removeAll(listesDeSeries.get(0));
			listesDeSeries.get(0).clear();
			txt.clear();
			iMin1.clear();
			iMax1.clear();
		}
		indCF.setVisible(false);
		indF.setVisible(false);
	}

	public void suppGraph2() {
		if (listesDeSeries.size() > 1) {
			chart.getData().removeAll(listesDeSeries.get(1));
			listesDeSeries.get(1).clear();
			txt2.clear();
			iMin2.clear();
			iMax2.clear();
		}
		indCG.setVisible(false);
		indG.setVisible(false);
	}

	public void suppGraph3() {
		if (listesDeSeries.size() > 2) {
			chart.getData().removeAll(listesDeSeries.get(2));
			listesDeSeries.get(2).clear();
			txt3.clear();
			iMin3.clear();
			iMax3.clear();
		}
		indCH.setVisible(false);
		indH.setVisible(false);
	}

	@SuppressWarnings("unlikely-arg-type")
	@FXML
	public void NouvellePage() {
		chart.getData().removeAll(listesDeSeries.get(0));
		chart.getData().removeAll(listesDeSeries.get(1));
		chart.getData().removeAll(listesDeSeries.get(2));
		txt.clear();
		txt2.clear();
		txt3.clear();
		iMin1.clear();
		iMin2.clear();
		iMin3.clear();
		iMax1.clear();
		iMax2.clear();
		iMax3.clear();
		indCF.setVisible(false);
		indF.setVisible(false);

		indCG.setVisible(false);
		indG.setVisible(false);

		indCH.setVisible(false);
		indH.setVisible(false);

	}

	@FXML
	public void Quitter() {
		stage = (Stage) root.getWindow();
		stage.close();

	}
	@FXML
	void Redimentionnement() {
        if (bool) {
               stage = (Stage) root.getWindow();
               stage.setFullScreenExitHint("");
               stage.setFullScreen(true);
               bool=false;
           }
           else {
               stage = (Stage) root.getWindow();
               stage.setFullScreenExitHint("");
               stage.setFullScreen(false);
               bool=true;
           }

       }

	@FXML
	void Reduire() {
		stage = (Stage) root.getWindow();
		stage.setIconified(true);


	}

	@FXML
	void dragged(MouseEvent event) {
		stage = (Stage) root.getWindow();
		stage.setX(event.getScreenX() - x);
		stage.setY(event.getSceneY() - y);

	}

	@FXML
	void pressed(MouseEvent event) {
		x = event.getSceneX();
		y = event.getSceneY();
	}

	private boolean contains(char[] tab, char c) {
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] == c) return true;
		}
		return false;
	}


	@FXML
	void autoZoom() {
		XAxis.setLowerBound(upperLower()[0]);
		YAxis.setLowerBound(-10);
		XAxis.setUpperBound(upperLower()[1]);
		YAxis.setUpperBound(10);
	}

	@FXML
	public void initialize() {

		listesDeSeries.add(list1);
		listesDeSeries.add(list2);
		listesDeSeries.add(list3);
		
		setInfosBullesDuration();

		/** si on veux deplacer le graphe on clique sur la touche droite de la souris et on glisse */
		PanControler panner = new PanControler(chart);
		panner.setMouseFilter(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() == MouseButton.SECONDARY) {
					//System.out.print("pan");
				} else {
					mouseEvent.consume();
				}
			}
		});
		panner.start();

		/**
		 * 	si on veux zoomer on utilise la touche gauche de la souris ou on zoom directement sur l'ecran
		 */

		ConfigurationZoom.ZoomConfiguration(chart, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() != MouseButton.PRIMARY)
					mouseEvent.consume();
			}
		}, upperLower()[2], upperLower()[3], upperLower()[2], upperLower()[3]);


		sauv1.setOnAction(event -> {
			n = 0;
			sauvegardeFonction(txt.getText());
		});
		sauv2.setOnAction(event -> {
			n = 1;
			sauvegardeFonction(txt2.getText());
		});
		sauv3.setOnAction(event -> {
			n = 2;
			sauvegardeFonction(txt3.getText());
		});
		historique.setOnAction(event -> afficheHistorique());
	}

	public void dessiner2() throws InputException {
        parser.parsing2();
        boolean bool = true;
        boolean flag = true;
        Double x = (double) 0;
        double pas = (imax - imin) <= 10 ? 0.001 : 0.01;
        double round = (imax - imin) <= 10 ? 1000.0 : 100.0;
        double i = Math.round((imin + pas) * round) / round;
        while (i <= imax) {
            x = parser.calculer(Math.round(i * round) / round);
            if (x <= 50 && x >= -50) { 
                bool = false;
                double prev = parser.calculer(i - pas);
                if (flag) {
                    series = new XYChart.Series<Double, Double>();
                    listesDeSeries.get(n).add(series);
                    if (prev > 50) {
                        prev = 50;
                    } else if (prev < -50) {
                        prev = -50;
                    }
                    
                    double ind = Math.round((i - pas) * round) / round;
                    listesDeSeries.get(n).getLast().getData().add(new XYChart.Data<Double, Double>(ind, prev));
                    flag = false;
                }
                listesDeSeries.get(n).getLast().getData().add(new XYChart.Data<Double, Double>(i, x));
            } else {
                flag = true;
                double around = x;
                if (!bool) {
                    bool = true;
                    if (listesDeSeries.get(n).size() == 0) {
                        listesDeSeries.get(n).add(new XYChart.Series<Double, Double>());
                    }
                    double prev = parser.calculer(i - pas);
                    if (prev<0) {
                    	around = -50;
                    }else {
                    	around  = 50;
                    }
                    /*if (x > 100) {
                        around = 100;
                    } else if (x < -100) {
                        around = -100;
                    }*/
                    listesDeSeries.get(n).getLast().getData().add(new XYChart.Data<Double, Double>(Math.round((i-pas) * round) / round, around));
                }
            }
            i += pas;
            i = Math.round(i * round) / round;
        }
    }
	
	public double [] upperLower(){

		double [] res = new double[4];
		double min1 = (!iMin1.getText().equals("") ) ?  Double.parseDouble(iMin1.getText()) :  Double.parseDouble(iMin1.getPromptText());
		double min2 = (!iMin2.getText().equals("") ) ?  Double.parseDouble(iMin2.getText()) :  Double.parseDouble(iMin2.getPromptText());
		double min3 = (!iMin3.getText().equals("") ) ?  Double.parseDouble(iMin3.getText()) :  Double.parseDouble(iMin3.getPromptText());
		double max1 = (!iMax1.getText().equals("")) ?  Double.parseDouble(iMax1.getText()) :  Double.parseDouble(iMax1.getPromptText());
		double max2 = (!iMax2.getText().equals("")) ?  Double.parseDouble(iMax2.getText()) :  Double.parseDouble(iMax2.getPromptText());
		double max3 = (!iMax3.getText().equals("")) ?  Double.parseDouble(iMax3.getText()) :  Double.parseDouble(iMax3.getPromptText());

		if(!txt.getText().equals("")){
			if( !txt2.getText().equals("") && !txt3.getText().equals("")){
				res[0] = Math.max(min1,Math.max(min2,min3));
				res[1] = Math.min(max1,Math.min(max2,max3));
				res[2] = Math.min(min1,Math.min(min2,min3));
				res[3] = Math.max(max1,Math.max(max2,max3));
			}
			else if(!txt2.getText().equals("")){
				res[0] = Math.max(min1,min2);
				res[1] = Math.min(max1,max2);
				res[2] = Math.min(min1,min2);
				res[3] = Math.max(max1,max2);
			}
			else if (!txt3.getText().equals("")){
				res[0] = Math.max(min1,min3);
				res[1] = Math.min(max1,max3);
				res[2] = Math.min(min1,min3);
				res[3] = Math.max(max1,max3);
			}
			else  {
				res[0] = min1;
				res[1] = max1;
				res[2] = min1;
				res[3] = max1;
			}

		}
		else if(!txt2.getText().equals("")){
			if(!txt3.getText().equals("")){
				res[0] = Math.max(min3,min2);
				res[1] = Math.min(max3,max2);
				res[2] = Math.min(min3,min2);
				res[3] = Math.max(max3,max2);
			}
			else{
				res[0] = min2;
				res[1] = max2;
				res[2] = min2;
				res[3] = max2;
			}
		}
		else{
			res[0] = min3;
			res[1] = max3;
			res[2] = min3;
			res[3] = max3;
		}
		return res;
	}

	public void setUser(User newUser) {
		user = newUser;
	}


	public void sauvegardeFonction(String FoncExpression) {
		if (FoncExpression != null && !FoncExpression.trim().equalsIgnoreCase("")) {
			try {
				parser = new Parser(FoncExpression);
				parser.parsing2();
				double x = parser.calculer(1.0);
			} catch (InputException e) {
				//alert.setTitle("Alerte");
				genererBoiteDialogue("Sauvegarde échouée ", "Le format de la fonction est invalide", "warning");
				return;
			}
			if (!listeUsers.recupUser(user.getName()).dejaSauvegarde(FoncExpression)) {
				listeUsers.addFonction(FoncExpression, user.getName());
				genererBoiteDialogue("sauvegarde réussie ! ", "La fonction a ete bien sauvegardee", "Information");
			} else {
				genererBoiteDialogue("Attention : la fonction est deja enregistree, retrouvez la dans votre historique", "", "warning");
			}
		}else{
			genererBoiteDialogue("Attention !", "Le format de la fonction est invalide", "warning");
			return;
		}
	}


	public void afficheHistorique() {

		LinkedList<String> listeFonctions = listeUsers.recupUser(user.getName()).getListeFonctions();
		if (listeFonctions.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information Historique");
			alert.setHeaderText(null);
			alert.setContentText("Votre historique de fonctions est vide");
			alert.showAndWait();
		} else {
			ChoiceDialog<String> dialog = new ChoiceDialog<String>(listeFonctions.get(listeFonctions.size() - 1), listeFonctions);
			dialog.setTitle("Mon historique");
			dialog.setHeaderText("Selectionnez une fonction qui sera ensuite chargee dans le champs F(x):");
			dialog.setContentText("Fonction:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				this.txt.setText(result.get());
				return;
			}
		}
	}

	public void genererBoiteDialogue(String headerText, String message, String typeAlerte) {
		Alert dialog;
		if (typeAlerte.equals("warning")) dialog = new Alert(Alert.AlertType.WARNING);
		else dialog = new Alert(Alert.AlertType.INFORMATION);
		dialog.setTitle(typeAlerte);
		dialog.setHeaderText(headerText);
		dialog.setContentText(message);
		dialog.showAndWait();
	}
	
	public void setInfosBullesDuration() {
		tracer.getTooltip().setShowDelay(new Duration(0.0001));
		tracer2.getTooltip().setShowDelay(new Duration(0.0001));
		tracer3.getTooltip().setShowDelay(new Duration(0.0001));
		sup.getTooltip().setShowDelay(new Duration(0.0001));
		sup2.getTooltip().setShowDelay(new Duration(0.0001));
		sup3.getTooltip().setShowDelay(new Duration(0.0001));
		sauv1.getTooltip().setShowDelay(new Duration(0.0001));
		sauv2.getTooltip().setShowDelay(new Duration(0.0001));
		sauv3.getTooltip().setShowDelay(new Duration(0.0001));
		nouveau.getTooltip().setShowDelay(new Duration(0.0001));
	}
}
