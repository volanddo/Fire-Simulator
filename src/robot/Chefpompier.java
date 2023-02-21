package robot;

import environment.Carte;
import environment.Incendie;
import evenementAvances.Intervenir;
import evenementAvances.Remplir;
import simulation.Simulateur;

public class Chefpompier {
    
    private Simulateur simulateur;
    private Carte carte;
    private Robot[] robots;
    private Incendie[] incendies;

    /** 
    * Initialise la simulation en envoyant chaque robot sur le feu atteignable le plus proche
    * Si le robot ne possède pas de feu atteignable proche, il ne sera plus jamais utilisé
    */
    public void AdvancedFireExtinguisher() {
        if (IsThereStillAFire(incendies)){
            for (Robot robot : getRobots()) {
                robot.setChef(this);
                CalculPlusCourtChemin calc = getFireNearestRobot(robot);
            
                if (calc != null){
                    getSim().ajouteEvenement(new Intervenir(robot, calc.getCaseArrivee(), getCarte(), getSim().getDateSimu(), calc));
                }  
            
            }
        }
    }

    /** 
    * Appel au chef pompier pour obtenir sa prochaine affectation.
    * Si le robot appelant ne peut être affecté à aucun feu, il ne sera plus jamais utilisé
    * @param robot : robot qui appelle le chef pompier pour avoir sa prochaine affectation
    */
    public void callToTheChief(Robot robot){
        if (IsThereStillAFire(incendies)){
            CalculPlusCourtChemin calc = getFireNearestRobot(robot);
            
            if (calc != null){
                getSim().ajouteEvenement(new Intervenir(robot, calc.getCaseArrivee(), getCarte(), getSim().getDateSimu(), calc));
            }  
        }
    }

    /**
    * Appel au chef pompier pour lui notifier que le robot appelant va se remplir.
    * @param Robot : robot qui appelle le chef pompier pour savoir où il peut se remplir
    */
    public void callToReplenishToTheChief(Robot robot) {
        getSim().ajouteEvenement(new Remplir(robot, getCarte(), getSim().getDateSimu()));
    }

    /** 
    * Permet de savoir si il reste un feu à éteindre.
    * @param incendies : Les incendies qui sont en cours sur la map
    * @return boolean : true si il reste un incendie à éteindre, false sinon
    */
    public boolean IsThereStillAFire(Incendie[] incendies) {
        for (Incendie incendie : incendies) {
            if (incendie.getNbLitrePourEteindre() > 0) {
                return true;
            }
        }
        return false;
    }

    /** 
    * Renvoie si il existe le feu le plus proche du robot.
    * @param robot : robot pour lequel on calcul l'incendie le plus proche
    * @return Incendie : null si il n'y a pas d'incendie proche atteignable, Incendie sinon
    */
    public CalculPlusCourtChemin getFireNearestRobot(Robot robot) {
        long time = -1;
        CalculPlusCourtChemin shortestPath = null;
        for (Incendie incendie : getIncendies()) {
            if (incendie.getNbLitrePourEteindre() > 0 && robot.getVitesse(incendie.getPosition().getNature() )!=0) { // si incendie pas eteint
                CalculPlusCourtChemin shortestPathTemp = new CalculPlusCourtChemin(getCarte(), robot, getSim(), robot.getPosition(),    incendie.getPosition());
                if (shortestPathTemp.getPath() != null &&(time > (long) shortestPathTemp.getTime() || time == -1)) {
                    time = (long) shortestPathTemp.getTime();
                    shortestPath = shortestPathTemp;
                }
            }
        }
        return shortestPath;
    }

    /** 
    * Constructeur de Classe
    */
    public Chefpompier(Carte carte, Robot[] robots, Incendie[] incendies, Simulateur sim) {
        this.carte = carte;
        this.robots = robots;
        this.incendies = incendies;
        this.simulateur = sim;
    }

    /** 
    * Donne les robots que gére le chef pompier
    * @return Robot[]
    */
    public Robot[] getRobots() {
        return this.robots;
    }

    /** 
    * Donne les incendies que gére le chef pompier
    * @return Incendie[]
    */
    public Incendie[] getIncendies() {
        return this.incendies;
    }

    /** 
    * Donne la carte sur laquel travaille le chef pompier
    * @return Carte
    */
    public Carte getCarte() {
        return this.carte;
    }

    /** 
    * Donne le simulateur utilisé
    * @return Simulateur
    */
    public Simulateur getSim() {
        return this.simulateur;
    }
}