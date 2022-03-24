package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AideControler {
	
	private Stage stage;
	private double x;
	private double y;
	
	@FXML
    private Scene root;
	@FXML
	private Button redimention;

	@FXML
	private Button reduire;

    @FXML
    private Button quitter;
    
    private boolean bool=true;
    
    @FXML
   	   void Quitter(ActionEvent event ) throws IOException {
    	FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxmls/Accueil.fxml"));
        Scene newscene =  loader.load();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(newscene);
        window.show();

   	}
       @FXML
       void Redimentionnement(ActionEvent event) {
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
       void Reduire(ActionEvent event) {
       	stage = (Stage) root.getWindow();
       	stage.setIconified(true);
       	

       }
       @FXML
       void Dragged(MouseEvent event) {
       	stage = (Stage) root.getWindow();
       	stage.setX(event.getScreenX() - x);
       	stage.setY(event.getSceneY() - y);

       }

       @FXML
       void Pressed(MouseEvent event) {
       	x = event.getSceneX();
       	y = event.getSceneY();
        }

}
