package evenementElementaire;

import environment.Carte;
import environment.Direction;
import robot.Robot;

public class DeplaceRobot extends Evenement{
    
    private Direction direction;
    private Robot r;
    private Carte c;
    private boolean premierDeplacement = true;
    private long initdate;

    /**
     * Evènement de déplacement élementaire, débute à date et se fini après la traversée de la case actuelle
     * @param r robot à déplacer
     * @param d direction du déplacement
     * @param date date à laquelle effectuer l'évènement
     * @param c carte où se passe le déplacement
     */
    public DeplaceRobot(Robot r, Direction d, long date, Carte c){
        super ( date ) ;
        this.initdate = date;
        this.direction = d;
        this.r = r;
        this.c = c;
    }
    
    /** 
     * Exécute le déplacement du robot
    */
    public void execute() {
        //Si on execute pour la premiere fois et que l'on peut se déplacer sur la case suivante
        if(this.c.getVoisin(r.getPosition(), this.direction) == null){
            return ;
        }
        if(premierDeplacement && r.getVitesse(this.c.getVoisin(r.getPosition(), this.direction).getNature()) != 0F){
            this.date += getTimeToDo();
            premierDeplacement = false;
        }
        else{
            r.setPosition(this.c.getVoisin(r.getPosition(), this.direction));
        }
    }

    /**
     * Reset l'évènement à ses valeurs par défaut
     */
    public void resetEvent(){
        date = initdate;
        premierDeplacement = true;
    }


    @Override
    /** 
     * Donne la direction et la date du début du déplacement au format string
     * @return String
    */
    public String toString() {
        return "Event : Deplace Robot : " + direction + " at " + this.date;
    }

    /** 
     * Renvoie le temps du déplacement
     * @return long
    */
    public long getTimeToDo(){
        if(this.c.getVoisin(r.getPosition(), this.direction) == null){
            return 0;
        }
        if (r.getVitesse(this.c.getVoisin(r.getPosition(), this.direction).getNature()) == 0F){
            return 0;
        }
        return c.getTailleCases() / (long) (r.getVitesse(r.getPosition().getNature())/3.6);
    }
}
