package robot;
import environment.*;
import evenementElementaire.*;

import java.util.*;
import simulation.*;

public class CalculPlusCourtChemin {
	
	private Carte carte;
	private Robot robot;
	private Simulateur simulateur;
	private Case caseDepart;
	private Case caseArrivee;
	private double time_to_travel;
	private ArrayList<Case> chemin;

	/** 
    * Constructeur de la classe
    */
	public CalculPlusCourtChemin(Carte carte, Robot robot, Simulateur simulateur, Case caseDepart, Case caseArrivee) {
		this.carte = carte;
		this.robot = robot;
		this.simulateur = simulateur;
		this.caseDepart = caseDepart;
		this.caseArrivee = caseArrivee;
		SetSorthestPathAndTime();
	}

	/** 
    * Détermine le chemin entre la case de départ et la case d'arrivée.
	* Ces données sont obtenables en faisant getTime() et getPath().
    */
	public void SetSorthestPathAndTime() {
		TupleOfHashMap tuple = Dijkstra(getCarte(), getRobot(), getCaseDepart(), getCaseArrivee());
		if (tuple == null) { // cas où il n'y a pas de chemin possible entre ces deux cases
			setPath(null); 
			setTime((double) -1);
			return;
		}
		HashMap<Case, Case> predecesseur = tuple.predecesseur;
		Case currentCase = getCaseArrivee();
		ArrayList<Case> chemin = new ArrayList<Case>();
		double time = 0;
		while (!currentCase.equals(getCaseDepart())) {
			chemin.add(0, currentCase); // Ajoute l'élément au début de la liste
			time = time + EdgeCost(currentCase, robot, getCarte().getTailleCases());
			currentCase = predecesseur.get(currentCase);
		}
		time = time + EdgeCost(currentCase, robot, getCarte().getTailleCases());
		chemin.add(0, currentCase); // on ajoute la première case
		setTime(time);
		setPath(chemin);
	}

	/** 
    * Implémente un algorithme de Dijkstra modifié.
    * @param carte : Carte sur laquelle on réalise l'algorithme
	* @param robot : robot pour lequel on cherche le chemin
	* @param caseDepart : case à partir duquel on commence
	* @param caseArrivee : case pour laquel on veut aller
    * @return TupleOfHashMap : Une classe ayant pour attribut HashMap&lt;Case, Case&gt; prédecesseur et HashMap&lt;Case, Double&gt; tableDesCouts
    */
	public TupleOfHashMap Dijkstra(Carte carte, Robot robot, Case caseDepart, Case caseArrivee) {
		HashMap<Case, Case> predecesseur = new HashMap<Case, Case>();
		HashMap<Case, Double> tableDesCouts = InitializeCaseCostValue(carte);
		Set<Case> caseAVisitee = new HashSet<Case>();
		Set<Case> caseVisitee = InitializeVisitedCase(carte, robot); // On dit que les cases où la vitesse est nulle sont visitées : on n'y passera jamais
		int tailleCase = carte.getTailleCases();
		Case caseCourante = caseDepart;
		caseAVisitee.add(caseCourante);
		tableDesCouts.put(caseCourante, (double) 0); // On met 0 heure pour atteindre la case de départ
		
		while (!caseCourante.equals(caseArrivee) && caseAVisitee.size() != 0) { // Si on est arrivé sur la case finale ou qu'on a tout visité, on s'arrête 
			caseCourante = getCaseWithMinCost(tableDesCouts, caseAVisitee);
			for (Direction d : Direction.values()) {
				Case neighbor = carte.getVoisin(caseCourante, d);
				if (neighbor == null) continue;
				if (!caseVisitee.contains(neighbor)) { // Si la case a été visitée, pas la peine de revenir dessus puisque le coût ne fera qu'augmenter
					double currentNeighborCost = tableDesCouts.get(neighbor);
					double possibleNewNeighborCost = tableDesCouts.get(caseCourante) + EdgeCost(caseCourante, robot, tailleCase);
					if (possibleNewNeighborCost < currentNeighborCost) {
						tableDesCouts.put(neighbor, possibleNewNeighborCost);
						predecesseur.put(neighbor, caseCourante);
					}
					caseAVisitee.add(neighbor);
				}	
			}
			caseAVisitee.remove(caseCourante);
			caseVisitee.add(caseCourante);
		}
		if (!caseCourante.equals(caseArrivee)) return null; // cas où on n'a pas atteint la case finale
		return new TupleOfHashMap(predecesseur, tableDesCouts);
	}
	
	/** 
    * Initialise la liste des cases visitées.
	* Toutes les cases sur laquel le robot ne peut se déplacer sont mises dans la liste.
    * @param carte : Carte sur laquelle on réalise l'algorithme
	* @param robot : robot pour lequel on veut un chemin
    * @return Set&lt;Case&gt; : liste des cases visitées initiale
    */
	public Set<Case> InitializeVisitedCase(Carte carte, Robot robot) {
		Set<Case> caseVisitee = new HashSet<Case>();
		for (int i = 0; i < carte.getNbLignes(); i++) {
			for (int j = 0; j < carte.getNbColonnes(); j++) {
				if (robot.getVitesse(carte.getCase(i, j).getNature()) == 0) { // Si la vitesse sur la case est 0, on ne pourra jamais la visitée donc on ne la met pas dans la liste
					caseVisitee.add(carte.getCase(i, j));
				}
			}
		}
		return caseVisitee;
	}
	
	/** 
    * Initialise la table des coûts c'est-à-dire le temps mis pour aller de la case de départ à une case quelconque à plus l'infini
    * @param carte : Carte sur laquelle on réalise l'algorithme
    * @return HashMap&lt;Case, Double&gt; : La table des coûts
    */
	public HashMap<Case, Double> InitializeCaseCostValue(Carte carte) {
		HashMap<Case, Double> table = new HashMap<Case, Double>();
		for (int i = 0; i < carte.getNbLignes(); i++) {
			for (int j = 0; j < carte.getNbColonnes(); j++) {
				Case currentCase = carte.getCase(i, j);
				table.put(currentCase, Double.POSITIVE_INFINITY);
			}
		}
		return table;
	}
	
	/** 
    * Cherche la case parmis les cases non visitées qui minimise le temps à parcourir pour y aller depuis la case de départ
    * @param tableDesCouts : HashMap donnant le coût pour aller de la case de départ à une quelconque case
	* @param caseAVisitee : liste de case qu'il faut visiter
    * @return Case : case non encore visitée minimisant le temps de parcourt entre la case de départ et cette case
    */
	public Case getCaseWithMinCost(HashMap<Case, Double> tableDesCouts, Set<Case> caseAVisitee) {
		Case minCase = caseAVisitee.iterator().next();
		for (Case currentCase : caseAVisitee) {
			if (tableDesCouts.get(minCase) > tableDesCouts.get(currentCase)) {
				minCase = currentCase;
			}
		}
		return minCase;
	}
	
	/** 
    * Donne le temps pour se déplacer sur une case
    * @param ccase : case où il faut se déplacer
	* @param tailleCases : la taille de la case sur laquelle on veut se déplacer
    * @return static double : le temps de déplacement sur la case
    */
	public static double EdgeCost(Case ccase, Robot Robot, int tailleCases) {
		if (Robot.getVitesse(ccase.getNature()) == 0) {
			return Double.POSITIVE_INFINITY;
		}
		return (tailleCases/(Robot.getVitesse(ccase.getNature())*1000))*3600;
	}
	
	/** 
    * Donne la case de départ pour le chemin à calculer
	* @return Case
    */
	public Case getCaseDepart() {
		return this.caseDepart;
	}
	
	/** 
    * Donne la case de d'arrivée pour le chemin à calculer
	* @return Case
    */
	public Case getCaseArrivee() {
		return this.caseArrivee;
	}
	
	/** 
    * Donne la carte sur laquel le chemin sera calculer
    * @return Carte
    */
	public Carte getCarte() {
		return this.carte;
	}
	
	/** 
    * Donne le robot pour qui on veut calculer le chemin
	* @return Robot
    */
	public Robot getRobot() {
		return this.robot;
	}
	
	/** 
    * Donne le temps de déplacement sur le chemin
	* @return double
    */
	public double getTime() {
		return this.time_to_travel;
	}
	
	/** 
    * Donne le plus court chemin
	* @return ArrayList&lt;Case&gt;
    */
	public ArrayList<Case> getPath() {
		return this.chemin;
	}
	
	/** 
    * Definit le temps de déplacement sur le chemin
    */
	private void setTime(double time) {
		this.time_to_travel = time;
	}
	
	/** 
    * Définit le plus court chemin
    */
	private void setPath(ArrayList<Case> chemin) {
		this.chemin = chemin;
	}

	/** 
    * Donne le simulateur utilisé
	* @return Simulateur
    */
	public Simulateur getSim() {
		return this.simulateur;
	}
	
}
	
class TupleOfHashMap {
	HashMap<Case, Case> predecesseur;
	HashMap<Case, Double> tableDesCouts;
	public TupleOfHashMap(HashMap<Case, Case> predecesseur, HashMap<Case, Double> tableDesCouts) {
		this.predecesseur = predecesseur;
		this.tableDesCouts = tableDesCouts;
	}
}
