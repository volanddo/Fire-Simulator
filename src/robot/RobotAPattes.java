package robot;
import environment.Carte;
import environment.Case;
import environment.Direction;
import environment.NatureTerrain;

/* Classe Robot à chenilles hérité de robot
 * Dans les fichier : PATTES
*/

public class RobotAPattes extends Robot{
    /**
    * Crée un robot à pattes
    * @param c Case sur laquelle est positionné le robot (inutile de préciser une nature)
    */
    public RobotAPattes(Case c){
        super(c, "image/pattes.png");
        this.v = 30;
        this.reservoir = getMaxCapaciteReservoir();
    }
    
    /** 
     * Renvoie la vitesse à laquelle le robot évolue sur un terrain n
     * Si return 0; alors le terrain n'est pas praticable pour le robot
     * @param n Nature du terrain sur lequel on souhaite connaître la vitesse
     * @return double
     */
    public double getVitesse(NatureTerrain n){
        switch(n){
            case EAU:
                return 0;
            case ROCHE:
                return 10;
            default:
                return this.v;
        }
    }

    /** 
     * Donne le temps de remplissage complet du réservoir
     * @return double
     */   
    public double getTempsRemplissage(){
        return 0F;
    }

    /** 
     * Donne le débit du robot en L/s 
     * @return double
     */
    public double getExtinctionRatio(){
        return 10/1;
    }

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'extérieur
     * @param c  : Carte sur laquelle évolue le robot
     * @return boolean
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
        return Double.POSITIVE_INFINITY;
    }

    /** 
     * Donne le type de robot sous forme de chaine de caractere
     * @return String
     */
    public String getTypeRobot(){
        return "Robot à pattes";
    }
}
