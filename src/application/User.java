package application;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {

    private String name;

    private LinkedList<String> listeFonctions ;/** liste des fonctions enregistrees par l'utilisateur */

    public User(String name){
        this.name=name;
        listeFonctions = new LinkedList<>();
    }

    public String getName(){
        return  name;
    }

    public LinkedList<String> getListeFonctions(){
        return  listeFonctions;
    }

    /**
     * verifie si une fonction est deja enregistree par l'utilisateur
     * @param FoncExpression
     * @return
     */
    public Boolean dejaSauvegarde(String FoncExpression){
        int n = 0;
        while(n<listeFonctions.size()) {
            if(listeFonctions.get(n).equalsIgnoreCase(FoncExpression)) return true;
            n++;
        }
        return false;
    }
}