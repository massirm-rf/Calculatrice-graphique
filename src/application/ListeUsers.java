package application;

import java.io.*;
import java.util.LinkedList;


/**
 * cette classe permet de gerer la base de donnees d'utilisateurs
 */
public class ListeUsers implements Serializable {
    private LinkedList<User> liste = new LinkedList<User>() ;

    public ListeUsers(){
        try{
            FileInputStream fis = new FileInputStream("Sauvegarde/UsersList");
            ObjectInputStream ois = new ObjectInputStream(fis);
            liste=((LinkedList<User>) ois.readObject());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * ajout et sauvegarde d'un nouvel utilisateur
     * @param user
     */
    public void ajouter(User user) {
        liste.add(user);
        sauvegarder();
    }

    /**
     * verifier si l'utilisateur au nom 'name' existe dans notre BDD
     * @param name
     * @return
     */
    public boolean Existe(String name) {
        int n = 0;
        while(n<liste.size()) {
            if(liste.get(n).getName().equals(name)) return true;
            n++;
        }
        return false;
    }

    /**
     * sauvegarder de nouvelles informations ( utilisateur, fonctions..) sur la BDD
     */
    public void sauvegarder() {
        try {

            FileOutputStream fos = new FileOutputStream("Sauvegarde/UsersList");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(liste);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *    recuperation de l'utilisateur 'name' avec ses information
     * @param name
     * @return
     */
    public User recupUser(String name) {
        int n = 0;
        while(n<liste.size()) {
            if(liste.get(n).getName().equalsIgnoreCase(name)) {
                return liste.get(n);
            }
            n++;
        }
        return null;
    }


    /**
     *    ajouter et sauvegarder une fonction dans la liste de fonctions de l'utilisateur au nom 'username'
     * @param Foncexpression
     * @param username
     */
    public void addFonction(String Foncexpression, String username){
        recupUser(username).getListeFonctions().add(Foncexpression);
        sauvegarder();
    }

    public LinkedList<User> getListe(){
        return  liste;
    }
}
