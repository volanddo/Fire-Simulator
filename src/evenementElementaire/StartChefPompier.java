package evenementElementaire;

import robot.Chefpompier;
import simulation.Simulateur;

public class StartChefPompier extends Evenement{

    Chefpompier chefpomp;
    Simulateur sim;
    /**Evènement qui lance le chef pompier
     * @param sim Simulation où le chef pompier va inervenir
     * @param date date d'intervention du chef pompier
     */
    public StartChefPompier(Simulateur sim, long date){
        super(date);
        this.chefpomp = new Chefpompier(sim.getCarte(), sim.getRobots(), sim.getIncendie(), sim);
        this.sim = sim;
        sim.setChef(chefpomp);
    }

    @Override
    /** 
     * Met en route le chef pompoer
    */
    public void execute() {
        chefpomp.AdvancedFireExtinguisher();
    }

    @Override
    /** 
     * Renvoie le temps de la mise en route du chef pompier
     * @return long
    */
    public long getTimeToDo() {
        return 0;
    }

    @Override
    /**
     * Reset l'évènement à ses valeurs par défaut
     */
    public void resetEvent() {
        this.chefpomp = new Chefpompier(sim.getCarte(), sim.getRobots(), sim.getIncendie(), sim);
        sim.setChef(chefpomp);
    }
    
}
