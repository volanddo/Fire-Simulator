package evenementElementaire;

import robot.Robot;

public class Deverser extends Evenement{

    private Robot r;
    private long initdate;
    private boolean premierDeplacement = true;
    private long timeToDo = 0;

    /**
     * Evènement de déversement élementaire, débute à date et se fini une fois le réservoir rempli
     * @param r robot à déplacer
     * @param date date à laquelle effectuer l'évènement
     */
    public Deverser(Robot r, long date){
        super ( date ) ;
        initdate = date;
        this.r = r;

        //On regarge si incendie a etteindre
        if(r.getPosition().getIncendie() != null && !r.getPosition().getIncendie().estEteind()){
            timeToDo = r.getTimeExtinguish(r.getPosition().getIncendie());
        }
    }
    

    /** 
     * Exécute le déversement
    */
    public void execute() {
        if(timeToDo == 0){
            return ;
        }
        if(premierDeplacement){
            this.date += timeToDo;
            premierDeplacement = false;
        }
        else{
            if (r.getPosition().getIncendie() != null){
                r.deverserEeau(r.getReservoir());
            }
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
     * Donne la date du début d'intervention du robot r au format string
     * @return String
    */
    public String toString() {
        return "Event : Deverser eau sur feu: " + r + " at " + this.date;
    }

    /** 
     * Renvoie le temps du déversement
     * @return long
    */
    public long getTimeToDo(){
        return timeToDo;
    }
    
}
