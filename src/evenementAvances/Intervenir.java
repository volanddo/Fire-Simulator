package evenementAvances;

import java.util.ArrayList;
import java.util.LinkedList;

import environment.Carte;
import environment.Case;
import environment.Direction;
import evenementElementaire.DeplaceRobot;
import evenementElementaire.Deverser;
import evenementElementaire.Evenement;
import robot.Robot;
import robot.CalculPlusCourtChemin;

public class Intervenir extends Evenement{

    Robot r;
    LinkedList<Direction> chemin;
    Carte carte;
    private boolean deversementPasCommence = true;
    private boolean premiereExecution = true;
    
    private Case caseIntervention;
    private long initdate;
    CalculPlusCourtChemin calc = null;

    /**
     * Evenement avances qui permet d'intervenir sur une case.
     * Gère le calcul du chemin, le déplacement, et le déversement
     * @param r robot qui intervient
     * @param caseIntervention Case qui possède le feu
     * @param carte Carte où se trouve la case
     * @param date du début d'intervention
     */
    public Intervenir(Robot r, Case caseIntervention, Carte carte, long date){

        super ( date ) ;
        this.initdate = date;
        this.r = r;
        this.carte = carte;
        this.chemin = null;
        this.caseIntervention = caseIntervention;
        r.setBusy();// Au moment où l'on crée l'évènement, le robot ne sera plus disponible par la suite
        
    }
    /**
     * Evenement avancé qui permet d'intervenir sur une case.
     * Gère le déplacement et le déversement
     * @param r robot qui intervient
     * @param caseIntervention Case qui possede le feu
     * @param carte Carte où se trouve la case
     * @param date Date du début d'intervention
     * @param calc Chemin à emprunter par le robot
     */
    public Intervenir(Robot r, Case caseIntervention, Carte carte, long date, CalculPlusCourtChemin calc){

        super ( date ) ;
        this.initdate = date;
        this.r = r;
        this.carte = carte;
        this.chemin = null;
        this.caseIntervention = caseIntervention;
        this.calc = calc;
        r.setBusy();// Au moment où l'on crée l'évènement, le robot ne sera plus disponible par la suite
        
    }

    /** 
     * Execution de intervenir 
     * Peut changer de date suivant la durée de l'intervention
     */
    public void execute() {
        if(premiereExecution){
            if(ReservoirEstVide()){
                return;
            } // stop si pas possible

            calculCheminRobot();  //Resultat dans chemin 
            
            if (chemin.isEmpty() && r.getPosition().getIncendie() == null){
                r.setIdle();
                return;
            }
            premiereExecution = false;
        }

        LancerEvenementEnAttente();

        // On regarde si un chemin a été trouvé, faire le déplacement
        if (!chemin.isEmpty()){
            if(!caseIntervention.getIncendie().estEteind()){
                Evenement e = new DeplaceRobot(r, chemin.removeFirst(), this.date, carte);
                LancerEvenementPuisLeMettreEnAttente(e);
            }
            else{
                r.setIdle();
                if (r.getChef() != null){
                    r.getChef().callToTheChief(r);
                }
                return;
            }
            
        }
        else if (deversementPasCommence){
            deverserEauSiIncendie();
        }
        else{ // On a reussi à éteindre le feu, on appelle le chef pour avoir un nouveau feu
            if (r.getReservoir() > 0) {
                r.setIdle();
                if (r.getChef() != null){
                    r.getChef().callToTheChief(r);
                }
            }
            else if (r.getReservoir() == 0) {
                if (r.getChef() != null){
                    r.getChef().callToReplenishToTheChief(r);
                }
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
        if(calc == null){
            calc = new CalculPlusCourtChemin(carte, r, null, r.getPosition(), caseIntervention);
        }
        ArrayList<Case> lc = calc.getPath();            
        if(lc != null){
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
     * Déverse de l'eau si il y a un incendie
    */
    private void deverserEauSiIncendie(){
        Evenement e = new Deverser(r, this.date);
        if(LancerEvenementPuisLeMettreEnAttente(e)){
            deversementPasCommence = false;
        }
        else{
            //Pas reussi à déverser car feu déjà éteint
            r.setIdle();
            r.getChef().callToTheChief(r);
        }

        
    }
    /**
     * Reset l'évènement à ses valeurs par défaut
     */
    public void resetEvent(){
        this.date = initdate;
        deversementPasCommence = true;
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
