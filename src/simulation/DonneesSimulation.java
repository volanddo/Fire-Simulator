package simulation;

import environment.Carte;
import environment.Incendie;
import robot.Robot;

/*
 * Classe DonneesSimulation
 * Contient toute les données relative a une simulation
 */

public class DonneesSimulation{
    private Carte carte;
    private Incendie[] incendies;
    private Robot[] robots;

    /**
    * DonneesSimulation
    * Creer un objet donnée simulation.
    * A noté que aucune instanciation n'est faite. 
    * Les objets passé en argument doivent etre crée au préalable.
    * @param c         Carte utilisé pour la simulation
    * @param incendies Incendie present sur la carte 
    * @param robots    Robot present dans la simulation
    */
    public DonneesSimulation(Carte c, Incendie[] incendies, Robot[] robots){
        this.carte = c;
        this.incendies = incendies;
        this.robots = robots;   
    }

    public Carte getCarte() {
        return carte;
    }

    public Incendie[] getIncendies() {
        return incendies;
    }

    public Robot[] getRobots() {
        return robots;
    }

    @Override
    public String toString() {
        return "donnée simulation : \n" + this.carte+"\n" + this.incendies+"\n" + this.robots+"\n";
    }

}