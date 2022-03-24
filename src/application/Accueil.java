package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent ;
import java.io.IOException;
import java.util.Optional;


public class Accueil{
    @FXML
    private AnchorPane TitbleBar;
    @FXML
    private Scene root;

    @FXML
    private Button redimention;

    @FXML
    private Button reduire;

    @FXML
    private Button quitter;
    @FXML
    private Button connect;

    @FXML
    private Button help;
    Stage stage;
    
    private boolean bool = true;

    private ListeUsers liste= new ListeUsers();

    private User user;

    private double x;
    private double y;

    @FXML
    private void connect(ActionEvent event) throws IOException {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Username");
        dialog.setHeaderText("Entrez un nom d'utilisateur valide:");
        dialog.setContentText("Username:");
        Optional<String> reponse= dialog.showAndWait();

        if (reponse.isPresent()){
            String name = reponse.get();
            if(name!=null && !name.trim().equalsIgnoreCase("")){
                if(!liste.Existe(name)) {
                    user =new User(name);
                    liste.ajouter(user);
                }else {
                    user=liste.recupUser(name);
                }

                FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxmls/Calculatrice.fxml"));
                Scene newscene =  loader.load();
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(newscene);
                window.show();

                CalculatriceControler calc= loader.getController();
                calc.setUser(user);
                // calc.setUsername(name);


            }else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Attention : nom d'utilisateur invalide");
                alert.showAndWait();
            }
        }else{
            return;
        }

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




    @FXML
    public void help(ActionEvent event) throws IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxmls/PageAide.fxml"));
        Scene newscene =  loader.load();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(newscene);
        window.show();

    }



}
