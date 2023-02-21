package environment;

public class Incendie {
    private Case c;
    private int nbLitrePourEteindre = 0;
    private int nbLitrePourEteindreInit;

    /** 
     * Crée une incendie sur une case c
     * @param c    Case où se situe l'incendie
     * @param nbLitreEteindre  Nombre de litres nécessaire pour éteindre l'incendie
     */
    public Incendie(Case c, int nbLitreEteindre){
        this.c = c;
        c.setIncendie(this);
        this.nbLitrePourEteindre = nbLitreEteindre;
        this.nbLitrePourEteindreInit = nbLitreEteindre;
    }
    /** 
     * Renvoie le chemin du l'image du feu
     * @return String
     */
    public String getPathCase(){
        return "image/fire.png";
    }

    /** 
     * Renvoie la case où se trouve l'incendie
     * @return Case
     */
    public Case getPosition() {
        return c;
    }

    /** 
     * Renvoie le nombre de litres nécessaire pour éteindre l'incendie
     * @return int
     */
    public int getNbLitrePourEteindre() {
        return this.nbLitrePourEteindre;
    }

    /** 
     * Reset l'incendie à son état initial
     */
    public void reset(){
        this.nbLitrePourEteindre = nbLitrePourEteindreInit;
    }
    
    @Override
    /** 
     * Donne la case et le nombre de litres nécessaire pour l'éteindre d'un incendie au format string
     * @return String
    */
	public String toString() {
		return "Incendie [case= " +this.c + ", nbLitreEteindre=" + this.nbLitrePourEteindre +"]";
	}

    @Override
    /** 
     * Vérifie que deux incendies sont égaux (ssi leurs cases respectives sont les mêmes)
     * @return boolean : renvoie true si ils sont égaux et false sinon
    */
    public boolean equals(Object obj) {
        if (obj instanceof Incendie){
            Incendie i = (Incendie) obj;
            return i.c == this.c && i.nbLitrePourEteindre == this.nbLitrePourEteindre; 
        }
        return false;
    }

    /** 
     * Méthode qui reduit le feu d'un incendie, si on verse plus d'eau que nécessaire, set a 0
     * @param nbLitreVerse nombre de litre versé
     */
    public void reduireFeu(int nbLitreVerse){
        this.nbLitrePourEteindre -= nbLitreVerse;
        if (this.nbLitrePourEteindre <= 0){
            this.nbLitrePourEteindre = 0;
        }
        
    }

    /** 
     * Vérifie si un incendie est éteint
     * @return boolean : renvoie true si l'incendie est éteint et false sinon
    */
    public boolean estEteind(){
        return this.nbLitrePourEteindre <= 0;
    }
}