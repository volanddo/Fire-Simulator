package evenementElementaire;

import environment.Carte;
import robot.Robot;

public class RemplirRobot extends Evenement{
    private Robot r;
    private Carte c;
    private boolean premierDeplacement = true;
    private long initdate;
    /**
     * Evènement qui rempli le robot s'il est vide et sur la bonne case
     * @param r robot à remplir
     * @param date date de remplissage
     * @pararm c carte où se passe la simulation.
     */
    public RemplirRobot(Robot r, long date, Carte c){
        super ( date ) ;
        this.initdate = date;
        this.r = r;
        this.c = c;
    }
    
    /** 
     * Exécute le remplissage du robot r
    */
    public void execute() {
        if(premierDeplacement){
            if(r.remplissageEstPossible(c,r.getPosition())){
                this.date += getTimeToDo();
                premierDeplacement = false;
            }
        }
        else{
            r.remplirReservoir(c);
        }
    }

    @Override
    /** 
     * Donne la date du début du remplissage du robot r au format string
     * @return String
    */
    public String toString() {
        return "Event : Rempli robot: " + r + " at " + this.date;
    }

    /** 
     * Renvoie le temps du remplissage
     * @return long
    */
    public long getTimeToDo(){
        if(!r.remplissageEstPossible(c,r.getPosition())){
            return 0;
        }
        return (long) r.getTempsRemplissage();
    }
    /**
     * Reset l'évènement à ses valeurs par défaut
     */
    public void resetEvent(){
        date = initdate;
    }

}
