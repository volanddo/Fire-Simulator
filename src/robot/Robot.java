package robot;
import environment.*;

public abstract class Robot {
    protected Case c;
    protected double v; //vitesse
    protected double reservoir;
    protected String imagePath;
    protected boolean busy;
    protected Chefpompier chef;
    private Case caseInitial;
    private boolean isAnimate;

    /**
     * Constructeur d'un robot
     * @param c Case du robot (egalement celle de la carte)
     * @param imagePath Chemin vers l'image du robot
     */
    public Robot(Case c, String imagePath){
        this.c = c;
        this.caseInitial = c;
        this.imagePath = imagePath;
        this.busy = false;
        this.isAnimate = false;
    } 

    /**
     * Vérifie si le robot est en mouvement
     * @return boolean : renvoie true si le robot est en mouvement et false sinon
     */
    public boolean getIsAnimate(){
        return this.isAnimate;
    }
    
    public void setIsAnimate(boolean isAnimate){
        this.isAnimate = isAnimate;
    }
    public Chefpompier getChef() {
        return chef;
    }

    public void setChef(Chefpompier chef) {
        this.chef = chef;
    }

    public void setBusy() {
        this.busy = true;
    }

    public void setIdle() {
        this.busy = false;
    }

    /**
     *  Getter de l'image du robot
     * @return String
     */
    public String getPathCase(){
        return imagePath;
    }

    /**
     * Vérifie si le robot est occupé
     * @return boolean : renvoie true si le robot est en occupé et false sinon
     */
    public boolean isBusy() {
        return this.busy;
    }

    /**
     * Indique si un remplissage est possible pour la case actuel 
     * @param c Carte sur laquel évolue le robot
    */
    public abstract boolean remplissageEstPossible(Carte carte, Case casee);
    
    /* GETTER */
    
    /** 
     * Renvoie la vitesse (km/h) a laquelle le robot évolue sur un terrain n
     * Si return 0; alors le terrain n'est pas praticable pour le robot
     * @param n Nature du terrain sur lequel on souhaite connaitre la vitesse
    */
    public abstract double getVitesse(NatureTerrain n);

    /**
     * Donne le temps de remplissage complet du reservoir (en seconde)
     */
    public abstract double getTempsRemplissage();

    /** 
     * Donne le debit du robot en L/s 
     */
    public abstract double getExtinctionRatio();

    /**
     * Donne la capacité max du reservoir en L 
     */
    public abstract double getMaxCapaciteReservoir();

    /**
     * Donne le type de robot sous forme de chaine de caractere
     */
    public abstract String getTypeRobot();

    
    /**
     * Getter de la case actuel du robot 
     */
    public Case getPosition(){
        return this.c;
    }

    /**
     * Getter du Path du robot 
     */
    public String getImagePath() {
        return imagePath;
    }
    /**
     *  Getter de la capacité d'eau disponible dans le reservoir
     */
    public double getReservoir(){ return this.reservoir; }

    // getter abstract public double getVitesse(NatureTerrain n);

    /* SETTER */
    /** 
     * Set la position du robot sur la case NextCase à condition que cette case soit adjacente et sur un terrain adapté 
     * @param nextCase La prochaine case du robot 
     *        CETTE CASE DOIT ETRE UNE REFERENCE D'UNE CASE DE LA CARTE
    */
    public void setPosition(Case nextCase){
        // verification adjacent
        if (nextCase == null ){
            System.err.println("Erreur : la case ne se situe pas dans le plateau ou est null");
        }
        else if (this.c.estAdjacente(nextCase) && getVitesse(nextCase.getNature()) != 0){
            this.c = nextCase;
        }
        else{
            System.err.println("Erreur : "+this.c + " ne peut pas avoir " + nextCase + " comme case suivante");
        }
    }    
    /** 
     * Set la position du robot à case sans vérification
     * @param case Nouvelle case du robot
     */
    public void reset(){
        this.c = caseInitial;
        this.reservoir = getMaxCapaciteReservoir();
        this.setIdle();
        this.setChef(null);
    }

    /** 
     * Enlève "volume" litres au réservoir 
     * Si le volume d'eau est plus important que le réservoir, n'enlève que le volume contenu
     * @param volume Volume d'eau deversé sur le feu
    */
    public boolean deverserEeau(double volume){
        if (volume <0){
            throw new AssertionError("utilisation volume négatif");
        }
        if (this.reservoir > 0){
            if (this.reservoir == Double.POSITIVE_INFINITY) {
                if (this.getPosition().getIncendie() != null){
                    this.getPosition().getIncendie().reduireFeu(this.getPosition().getIncendie().getNbLitrePourEteindre());
                    return true;
                }
                return false;
            }
            if (this.reservoir >=volume){
                this.reservoir = this.reservoir - volume;
                if (this.getPosition().getIncendie() != null){
                    this.getPosition().getIncendie().reduireFeu((int)volume);
                    return true;
                }
            }
            else{
                if (this.getPosition().getIncendie() != null){
                    this.getPosition().getIncendie().reduireFeu((int)this.reservoir);
                    this.reservoir = 0;
                    return true;
                }
            }
            
        }
        return false;
    }

    /**  Rempli le réservoir a sa capacité maximale 
     *  @param c Carte sur laquelle évolue le robot. Permet de connaître les cases aux alentours.
    */
    public void remplirReservoir(Carte carte){
        if (remplissageEstPossible(carte, this.c)){
            this.reservoir = getMaxCapaciteReservoir();
        }
        else{
            System.err.println("Erreur : Impossible de remplir le reservoir");
        }
    }

    /** 
     * Renvoie le temps pour éteindre le feu
     * @param incendie incendie que l'on souhaite éteindre
     * @return long
    */
    public long getTimeExtinguish(Incendie incendie) {
        if (this.getReservoir() == Double.POSITIVE_INFINITY) {
            return (long) (incendie.getNbLitrePourEteindre()/this.getExtinctionRatio());
        }
        return (long) (this.getMaxCapaciteReservoir()/this.getExtinctionRatio());
    }

    @Override
    /** 
     * Donne la case, la vitesse et le nombre de litre dans le réservoir du robot au format String
     * @return String
    */
    public String toString() {
        return getTypeRobot() + " : ( "+c + ", vitesse : "+v+" Litres dans le réservoir : " +reservoir+" )";
    }

}
