package robot;
import environment.Carte;
import environment.Case;
import environment.Direction;
import environment.NatureTerrain;

public class RobotARoues extends Robot{
    /**
    * Crée un robot à roues
    * @param c Case sur laquelle est positionné le robot (inutile de préciser une nature)
    */
    public RobotARoues(Case c){
        super(c, "image/roues.png");
        this.v = 80;
        this.reservoir = getMaxCapaciteReservoir();
    }
    /** 
     * Renvoie la vitesse a laquelle le robot évolue sur un terrain n
     * Si return 0; alors le terrain n'est pas praticable pour le robot
     * @param n Nature du terrain sur lequel on souhaite connaitre la vitesse
     * @return double
    */
    public double getVitesse(NatureTerrain n){
        switch(n){
            case TERRAIN_LIBRE:
                return this.v;
            case HABITAT:
                return this.v;
            default:
                return 0;
        }
    }

    /** 
     * Donne le temps de remplissage complet du reservoir 
     * @return double
     */
    public double getTempsRemplissage(){
        return 10F;
    }

    /** 
     * Donne le débit du robot en L/s 
     * @return double
     */
    public double getExtinctionRatio(){
        return 100/5;
    }

    /** 
     * Indique si un remplissage est possible pour la case actuelle
     * @param c Carte sur laquelle évolue le robot
     * @return booloean : Renvoie true si le remplissage est possible et false sinon
    */
    public boolean remplissageEstPossible(Carte carte, Case casee){
        if (casee.getNature() == NatureTerrain.EAU){
            return false;
        }
        Direction d[] = {Direction.NORD, Direction.EST, Direction.SUD, Direction.OUEST};
        for (Direction dir : d){
            if(carte.voisinExiste(casee, dir)){
                if (carte.getVoisin(casee, dir).getNature() == NatureTerrain.EAU){
                    return true;
                }
            }
        }
        return false;
    }

    /** 
     * Donne la capacité max du reservoir en L
     * @return double
     */
    public double getMaxCapaciteReservoir(){
        return 5000F;
    }
    
    /** 
     * Donne le type de robot sous forme de chaine de caractere
     * @return String
     */
    public String getTypeRobot(){
        return "Robot à roues";
    }
}
