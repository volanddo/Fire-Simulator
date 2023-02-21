package evenementAvances;

import java.util.ArrayList;
import java.util.LinkedList;

import environment.Carte;
import environment.Case;
import environment.Direction;
import evenementElementaire.DeplaceRobot;
import evenementElementaire.Evenement;
import evenementElementaire.RemplirRobot;
import robot.*;

public class Remplir extends Evenement{

    Robot r;
    LinkedList<Direction> chemin;
    Carte carte;
    private boolean remplissagePasCommence = true;
    private boolean premiereExecution = true;
    
    private long initdate;

    /**
     * Evenement avancé qui permet de remplir un robot.
     * Gère le calcul du chemin, le déplacement, et le remplissage
     * @param r robot qui intervient
     * @param carte Carte où se trouve la case
     * @param date du début de remplissage
     */
    public Remplir(Robot r, Carte carte, long date){

        super ( date ) ;
        this.initdate = date;
        this.r = r;
        this.carte = carte;
        this.chemin = null;
        r.setBusy();// Au moment où l'on crée l'évènement, le robot ne sera plus disponible par la suite
        
    }

    /** 
     * Exécution de Remplir
     * Peut changer de date suivant la durée de l'intervention
     */
    public void execute() {
        if(premiereExecution){
            if(! ReservoirEstVide()){
                return;
            } // stop si non vide

            calculCheminRobot();  //Resultat dans chemin 
            /*
            if (chemin.isEmpty() && r.remplissageEstPossible(carte, r.getPosition())){
                r.setIdle();
                return;
            } */
            premiereExecution = false;
            //System.out.println(chemin);
        }

        LancerEvenementEnAttente();

        // On regarde si un chemin a été trouvé faire le déplacement
        if (!chemin.isEmpty()){
            Evenement e = new DeplaceRobot(r, chemin.removeFirst(), this.date, carte);
            LancerEvenementPuisLeMettreEnAttente(e);
        }
        else if (remplissagePasCommence){ // Essaye de remplir
            RemplirSiPossible();
        }
        else{ // A fini de remplir
            r.setIdle();
            if (r.getChef() != null){
                r.getChef().callToTheChief(r);
            }
        }

    }   

    /** 
     * Vérifie si un réservoir est vide
     * @return boolean : renvoie true si un réservoir est vide et false sinon
    */
    private boolean ReservoirEstVide(){
        if (r.getReservoir() <= 0){
            r.setIdle();
            return true;
        }
        return false;
    }

    /** 
     * Transforme le chemin du robot en direction et le met dans l'attribut chemin du robot
    */ 
    private void calculCheminRobot(){
        if (this.chemin == null){
            chemin = new LinkedList<Direction>();
        }    
        //Calcul du plus court chemin vers la case
        ArrayList<Case> lc = Carte.getPathToNearerWaterCase(r, carte);
       
               
        if(lc != null && lc.size() > 1){
            //Transformation chemin en direction
            for (int i = 0; i < lc.size() - 1; i++) {
                Case currentCase = lc.get(i);
                Case nextCase = lc.get(i+1);
                Direction d = carte.getDirectionOfNeighbor(currentCase, nextCase);
                if(d!=null){
                    chemin.add(d);
                }
            }
        }
        
    }

    /** 
     * Rempli le robot si c'est possible
    */ 
    private void RemplirSiPossible(){
        Evenement e = new RemplirRobot(r, this.date, carte);
        if(LancerEvenementPuisLeMettreEnAttente(e)){
            remplissagePasCommence = false;
        }
        else{
            //Pas reussi a remplir le robot devient inactif de facon permanente
        }

        
    }

    /**
     * Reset l'évènement à ses valeurs par défaut
     */
    public void resetEvent(){
        this.date = initdate;
        remplissagePasCommence = true;
        premiereExecution = true;
        chemin = null;
        eventAExecuter = null;
    }
    
    @Override
    /** 
     * Donne la date du début de l'intervention au format string
     * @return String
    */
    public String toString() {
        return "Event : Intervenir eau Robot at " + this.date;
    }

    @Override
    /** 
     * Renvoie -1, impossible de connaître le temps d'intervention
     * @return long
    */
    public long getTimeToDo() {
        return -1;
    }
    
}
