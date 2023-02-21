package evenementElementaire;

/* Classe Evenement */
public abstract class Evenement implements Comparable{
    
    protected long date;
    protected Evenement eventAExecuter;
    /** 
     * Crée un nouvel évènement qui doit se produire à date
     * @param date     Date à laquelle se produit l'évènement
     */
    public Evenement(long date){
        this.date = date;
    }

    /** 
     * Getter de Date
     * @return long
     */
    public long getDate() {
        return date;
    }
    
    /** 
     * Exécuter l'évènement
     */
    public abstract void execute();

    /**
     * Temps d'exécution des évènements élementaires, -1 pour les évènements avancés  
     */
    public abstract long getTimeToDo();

    /**
     * Reset l'evenement a ses valeurs par defauts 
     */
    public abstract void resetEvent();

    /** 
     * Permet de comparer 2 objets
     * @return int : vaut 0 si c'est égal, 1 si c'est inférieur strict, -1 si c'est supérieur strict
    */
    public int compareTo(Object o) {
        if (o instanceof Evenement){
            Evenement e = (Evenement) o;
            if (e.date == this.date){
                return 0;
            }
            else if (e.date > this.date){
                return -1;
            }
            else{
                return 1;
            }
        }
        return -1;
    }

    /** 
     * Lance les évènements en attente
    */
    protected void LancerEvenementEnAttente(){
        if (eventAExecuter != null){
            eventAExecuter.execute();
        }
    }

    /** 
     * Lance les évènements puis les met en attente
    */
    protected boolean LancerEvenementPuisLeMettreEnAttente(Evenement e){
        e.execute();
        if (e.getTimeToDo() != 0){
            this.eventAExecuter = e;
            this.date += eventAExecuter.getTimeToDo();
            return true;
        }
        return false;
    }

}