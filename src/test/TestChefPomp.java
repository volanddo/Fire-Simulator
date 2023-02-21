package test;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import evenementElementaire.*;
import simulation.*;


public class TestChefPomp {
    
    public static void main(String[] args) throws FileNotFoundException, DataFormatException {
        String path = args[0];
    	Simulateur sim = new Simulateur(path, 1100, 1500);
    	
        sim.ajouteEvenement(new StartChefPompier(sim, 0));
    }

}
