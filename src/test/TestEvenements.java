package test;

import environment.Direction;
import evenementAvances.Intervenir;
import evenementAvances.Remplir;
import evenementElementaire.DeplaceRobot;
import evenementElementaire.Evenement;
import simulation.Simulateur;

public class TestEvenements {
    public static void main(String[] args) {

            //Deplacement simple et sorti de map
            Simulateur s0 = new Simulateur("cartes/carteSujet.map", 500, 800);
            scenario0(s0);
            
            //test evenement avancé ainsi que remplir et deverser
            Simulateur s1 = new Simulateur("cartes/carteSujet.map", 500, 800);
            scenario1(s1);

            //Test tout les deplacements et evenements simultané 
            Simulateur s2 = new Simulateur("cartes/carteSujet.map", 500, 800);
            scenario2(s2);
    }
    private static void scenario0(Simulateur s){
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[0], Direction.NORD, 1, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[0], Direction.NORD, 2, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[0], Direction.NORD, 3, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[0], Direction.NORD, 4, s.getCarte()));
    }

    private static void scenario1(Simulateur s){
        
        Evenement intervention = new Intervenir(
            s.getRobots()[0],
            s.getCarte().getCase(5, 5),
            s.getCarte(),
            1);
        s.ajouteEvenement(intervention);
         
        Evenement interventionR1 = new Intervenir(
            s.getRobots()[1],
            s.getCarte().getCase(5, 5),
            s.getCarte(),
            1);
        s.ajouteEvenement(interventionR1);


        
        Evenement interventionR2 = new Intervenir(
        s.getRobots()[2],
        s.getCarte().getCase(7, 7),
        s.getCarte(),
        1);
        s.ajouteEvenement(interventionR2);
        
        Evenement remplissage = new Remplir(
            s.getRobots()[0], 
            s.getCarte(),1500);
        
        s.ajouteEvenement(remplissage);

        Evenement remplissage2 = new Remplir(
            s.getRobots()[1], 
            s.getCarte(),1400);
        
        s.ajouteEvenement(remplissage2);
        
        Evenement intervention2 = new Intervenir(
            s.getRobots()[0],
            s.getCarte().getCase(6, 0),
            s.getCarte(),5000
        );

        s.ajouteEvenement(intervention2);
        
    }

    private static void scenario2(Simulateur s){
        System.out.println(s.getRobots().length);
        
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.NORD, 1, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.EST, 1, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.SUD, 2, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.OUEST, 2, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.OUEST, 5, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.OUEST, 6, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.EST, 7, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.EST, 7, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.EST, 7, s.getCarte()));
        s.ajouteEvenement(new DeplaceRobot(s.getRobots()[1], Direction.EST, 7, s.getCarte()));

        
    }
}
