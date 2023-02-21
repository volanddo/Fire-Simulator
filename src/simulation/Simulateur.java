package simulation;
import java.awt.Color;

import environment.Carte;
import environment.Case;
import environment.Incendie;
import evenementElementaire.Evenement;
import io.LecteurDonnees;
import robot.Chefpompier;
import robot.Robot;
import gui.GUISimulator;
import gui.ImageElement;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.zip.DataFormatException;


public class Simulateur implements Simulable{
    /** L'interface graphique associée */
    private GUISimulator gui;	
    private DonneesSimulation data;
    private String pathCarte;
    private Chefpompier chef;
    // Coordonné min et max de la simulation
    private int xMin;
    private int tailleCase;
    
    //Date courante
    long dateSimulation;
    PriorityQueue<Evenement> listEvenements;

    PriorityQueue<Evenement> listEvenementsInitial;
    Robot[] robotInitial;
    Incendie[] incendiesInitial;
    
    /** Creer, et lance le simulateur de robot 
     * @params pathCarte    Chemin de la carte utilisé pour la simulation (doit etre dans un format correct)
     * @params width        Largeur de la fenetre affiché
     * @params height       Hauteur de la fenetre affiché
    */
    public Simulateur(String pathCarte, int width, int height){
        this.gui = new GUISimulator(width, height, Color.CYAN, this);
        initSimulation(pathCarte);
        saveInitialConfiguration();
    }
    
    /** Methode next redefini, fait avancer la simulation d'un pas et actualise l'affichage*/
    @Override
    public void next() {
        this.incrementeDate();
        drawCarte();
        if(simulationTerminee()){
            gui.addGraphicalElement(new Rectangle(gui.getWidth()/2 - 10, gui.getHeight()/2- 5, Color.blue, Color.WHITE, 200, 100) );
            gui.addGraphicalElement(new Text(gui.getWidth()/2 - 5, gui.getHeight()/2, Color.red, "Simulation terminé" ));
        }
    }

    /** Remet la simulation dans son état initial*/
    @Override
    public void restart() {
        // Place robot in there initial place.
        for (int i = 0; i< this.robotInitial.length; i++){
            //if a robot is remove from the list
            this.data.getRobots()[i] = this.robotInitial[i];
            this.data.getRobots()[i].reset();
        }
        // Place Incendies in there initial place.
        for (int i = 0; i< this.incendiesInitial.length; i++){
            //if an incendie is remove from the list
            this.data.getIncendies()[i] = this.incendiesInitial[i];
            this.data.getIncendies()[i].reset();
        }
        //restart event
        this.listEvenements.clear();
        this.listEvenements.addAll(listEvenementsInitial);
        for (Evenement e : listEvenements){
            e.resetEvent();
        }
        this.dateSimulation = 0;

        this.drawCarte();
        
    }

    /**
     * Methode qui incremente la date de 1 et lance tout les evenements qui doivent etre lancer
     */
    private void incrementeDate(){
        if (! simulationTerminee()){
            this.dateSimulation += 1;
            Evenement current = this.listEvenements.poll();
            
            while(current != null && current.getDate() <= this.dateSimulation){
                
                current.execute();
                if (current.getDate() > this.dateSimulation){
                    this.listEvenements.add(current);
                }
                current = this.listEvenements.poll();
            }
            if (current !=null && current.getDate() > this.dateSimulation){
                this.listEvenements.add(current);
            }
        }
        
    }

    public void setChef(Chefpompier chef) {
        this.chef = chef;
    }
    public Chefpompier getChef() {
        return chef;
    }

    /**
     * Fonction qui renvoie un boolean true si la simulation est terminé (Plus d'évenement)
     */
    private boolean simulationTerminee(){
        return this.listEvenements.isEmpty();
    }
   
    /**
     *Sauvgarde la configuration initial 
     */
    private void saveInitialConfiguration(){
        //sauvegarde des robots
        robotInitial = new Robot[this.data.getRobots().length];
        for (int i = 0; i< this.data.getRobots().length; i++){
            robotInitial[i] = this.data.getRobots()[i];
        }

        //sauvegarde des Incendies
        incendiesInitial = new Incendie[this.data.getIncendies().length];
        for (int i = 0; i< this.data.getIncendies().length; i++){
            incendiesInitial[i] = this.data.getIncendies()[i];
        }

        //sauvegarde des evenements
        this.listEvenementsInitial = new PriorityQueue<Evenement>();
    }

    /**
     * Affiche la carte, les incendi et les robots a t=0
     */
    private void initSimulation(String pathCarte){
        //Initialisation de la liste d'évenement
        this.pathCarte = pathCarte;
        try {
            this.data = LecteurDonnees.lire(this.pathCarte);
        }catch (FileNotFoundException e) {
            System.out.println("fichier " + this.pathCarte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + this.pathCarte + " invalide: " + e.getMessage());
        }

        this.listEvenements = new PriorityQueue<Evenement>();

        
        drawCarte(); // optimisation possible : ne draw que ce qui a changé
    }

    
    /** Methode qui ajoute un evenement au bonne endroit dans la liste
     * 
     * @params e    Element a ajouter.
     */
    public void ajouteEvenement(Evenement e){
        this.listEvenements.add(e);  
        if(this.dateSimulation == 0){
            this.listEvenementsInitial.add(e);
        }  
    }

    public void printEventList() {
        System.out.println(this.listEvenements);
    }


    public Robot[] getRobots(){
        return this.data.getRobots();
    }

    public Carte getCarte(){
        return this.data.getCarte();
    }

    /** Retourne le pas de temps d'exécution du simulateur */
    public int getSimStep() {
        return 1; 
    }

    public long getDateSimu() {
        return this.dateSimulation;
    }

    /**
     * Converti un une coordonnée de la map, en pixel a afficher
     * @param x colonne du tableau
     * @param y ligne du tableau
     * @return x dans le repere de la fenetre en pixel
     */
    public int convertIsoX(int x, int y){
        
        int tileSize = this.tailleCase;
        int cartePosX = tileSize*x;
        int cartePosY = tileSize*y;

        return xMin + (cartePosX - cartePosY)/2;
    }
    /**
     * Converti un une coordonnée de la map, en pixel a afficher
     * @param x colonne du tableau
     * @param y ligne du tableau
     * @return y dans le repere de la fenetre en pixel
     */
    public int convertIsoY(int x, int y){
        int tileSize = this.tailleCase;
        int cartePosX = tileSize*x;
        int cartePosY = tileSize*y;

        return (cartePosX + cartePosY)/10*3;
    }

    /** Actualise l'affichage dans la fenetre */
    private void drawCarte(){
        if (gui.getWidth() < gui.getHeight()){
            this.tailleCase = (gui.getWidth()-20)/(data.getCarte().getNbColonnes());
            this.xMin = tailleCase*data.getCarte().getNbColonnes()/2 - tailleCase/2;
        }
        else{
            this.tailleCase = (10*(gui.getHeight()-20)/(data.getCarte().getNbLignes()))/8;
            this.xMin = gui.getWidth()/2 - this.tailleCase/2 -20;
        }
        
        this.gui.reset();	// clear the window
        gui.addGraphicalElement(new ImageElement(0, 0, "image/bg.png", 2*gui.getWidth(),2*gui.getHeight(), null));
        
        Carte carte = this.data.getCarte();
        for(int y = 0; y < carte.getNbLignes(); y++){
            for(int x = 0; x < carte.getNbColonnes(); x++){
                paintCase(carte.getCase(y, x));
                if (carte.getCase(y, x).getIncendie() != null){
                    paintIncendie(carte.getCase(y, x).getIncendie());
                }
            }
        }
        for(int i = 0; i < this.data.getRobots().length; i++){
            paintRobot(this.data.getRobots()[i]);
        }
    }

    /*
     * Methode qui affiche une case C sur la fenetre graphique a la bonne place.
     * @params c Case a afficher
     */
    private void paintCase(Case c){
        /* Case : 100*100 */
        
        int x = convertIsoX(c.getColonne(), c.getLigne());
        int y = convertIsoY(c.getColonne(), c.getLigne());

        gui.addGraphicalElement(new ImageElement(x, y, c.getPathCase(), this.tailleCase, this.tailleCase, null));
        gui.addGraphicalElement(new Text(100,10,Color.white, "" + dateSimulation));
        /* FINIR Maxence*/
    }
    
    /*
     * Methode qui affiche un robot r sur la fenetre graphique a la bonne place.
     * @params r robot a afficher
     */
    private void paintRobot(Robot r){
        int x = convertIsoX(r.getPosition().getColonne(), r.getPosition().getLigne());
        int y = convertIsoY(r.getPosition().getColonne(), r.getPosition().getLigne());
        if(!r.getIsAnimate()){
            gui.addGraphicalElement(new ImageElement(x, y, r.getPathCase(), this.tailleCase, this.tailleCase, null));
            gui.addGraphicalElement(new Text(x+tailleCase/2,y+tailleCase/4,Color.blue, ""+r.getReservoir() ));
        }
        
    }

    /*
     * Methode qui affiche un incendie i sur la fenetre graphique a la bonne place.
     * @params i incendie a afficher
     */
    private void paintIncendie(Incendie i){
        if(!i.estEteind()){
            int x = convertIsoX(i.getPosition().getColonne(), i.getPosition().getLigne());
            int y = convertIsoY(i.getPosition().getColonne(), i.getPosition().getLigne());
            
           gui.addGraphicalElement(new ImageElement(x, y, i.getPathCase(), this.tailleCase, this.tailleCase, null));
           gui.addGraphicalElement(new Text(x+tailleCase/2,y+tailleCase/4,Color.red, ""+i.getNbLitrePourEteindre() ));
        }
        
    }

    
    public Incendie[] getIncendie() {
        return this.data.getIncendies();
    }
    
}
