package environment;

public class Case {
    // déclaration des attributs de la classe
    private int ligne;
    private int colonne;
    private NatureTerrain nature;
    private String pathCase;

    private Incendie i = null;

    /** 
     * Une case d'un plateau, est composé de sa postion (ligne, colonne) et de la nature de son terrain
     * @param ligne    ligne de la case
     * @param colonne    colonne de la case
     * @param nature    nature de la case
     */
    public Case(int ligne, int colonne, NatureTerrain nature){
        this.ligne = ligne;
        this.colonne = colonne;
        this.nature = nature;
        switch (nature){
            case EAU:
                this.pathCase = "image/eau.png";
                break;
            case ROCHE:
                this.pathCase = "image/rock.png";
                break;
            case FORET:
                this.pathCase = "image/foret.png";
                break;
            case HABITAT:
                this.pathCase = "image/Habitat.png";
                break;
            case TERRAIN_LIBRE:
                this.pathCase = "image/libre.png";
                break;
        }
    }

    /** 
     * Getter du chemin de l'image de la case
     * @return String 
     */ 
    public String getPathCase() {
        return pathCase;
    }
    
    /** 
     * Setter ajoute un incendie sur la case
     * @param i incendie 
     */
    public void setIncendie(Incendie i){
        this.i = i;
    }

    /** 
     * Getter d'un incendie sur la case; si pas d'incendie, renvoie null
     * @return Incendie
    */
    public Incendie getIncendie(){
        return this.i;
    }

    /** 
     * Vérifie que deux case sont égales (ssi leur position sont égales)
     * @return boolean : renvoie true si elles sont égales sinon false
    */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Case){
            Case c = (Case) obj;
            return c.ligne == this.ligne && c.colonne == this.colonne; 
        }
        return false;
    }
	
    /** 
     * Donne la ligne, la colonne et la nature d'une case au format string
     * @return String
    */
    @Override
	public String toString() {
		return "(lig="+ligne+", col="+colonne+", nat="+nature+")";
	} 

    /** 
     * Donne la ligne de la case
     * @return int
    */
    public int getLigne() {
        return this.ligne;
    }

    /** 
     * Donne la colonne de la case
     * @return int
    */
    public int getColonne() {
        return this.colonne;
    }

    /** 
     * Donne la nature du terrain de la case
     * @return NatureTerrain
    */
    public NatureTerrain getNature() {
        return this.nature;
    }

    /** 
     * Vérifie si la case c est adjacente à celle-ci
     * @param c case que l'on souhaite vérifier
     * @return boolean : renvoie true si la case est adjacente et false sinon
     */
    public boolean estAdjacente(Case c){
        boolean estNord = c.getColonne()   == this.getColonne() && c.getLigne() == this.getLigne() -1;
        boolean estEst  = c.getColonne()-1 == this.getColonne() && c.getLigne() == this.getLigne();
        boolean estSud  = c.getColonne()   == this.getColonne() && c.getLigne() == this.getLigne() +1;
        boolean estOuest= c.getColonne()+1 == this.getColonne() && c.getLigne() == this.getLigne();
        return estNord || estEst || estSud || estOuest;
    }
}