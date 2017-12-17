package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {
	
	protected int id;														// identificateur du producteur de message
	protected int nbMessMax;												// 
	protected int nbMessProd;												// nombre de messages produits par le producteur
	
	private static int idStatic = 1;
	private int cpt, cptMax;												// compteur et compteur max
	private Aleatoire random;												

	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement,	int deviationTempsDeTraitement) throws ControlException { 
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		random = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		cptMax = random.next();
		cpt = 0;
		
		id = idStatic; 
		idStatic ++;
		nbMessMax = Aleatoire.valeur(5, 10) + 1; 							// un entier entre 1 et 10 
		nbMessProd = 0;
	}

	@Override
	public int deviationTempsDeTraitement() {
		return deviationTempsDeTraitement;
		}

	@Override
	public int identification() {
		return id;
		}

	@Override
	public int moyenneTempsDeTraitement() {
		return moyenneTempsDeTraitement;
		}

	@Override
	public int nombreDeMessages() {
		return nbMessMax;
		}

	@Override
	public void run() {
		while (nbMessMax != nbMessProd) {									// tant que le nombre de messages max n'est pas atteint 
			if (cpt < cptMax)                       						// si le compteur est inférieur au compteur max                                     
				cpt++;														// alors on incrémente le compteur de 1 
			else {															// sinon
				try {														
					MessageX messX = new MessageX(id, nbMessProd);			// on produit un nouveau message
					TestProdCons.TEST.getBuffer().put(this, messX);			// on dépose le message dans le buffer
					nbMessProd++;											// et on incrémente le nombre de messages produits de 1 
				} catch (InterruptedException e) {
					e.printStackTrace();
					} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				cptMax = random.next();										// le compteur max prend une nouvelle valeur 
				cpt = 0;													// on réinitialise notre compteur
			}
		}
		
		TestProdCons.TEST.remove(this);
	}
	
	public String toString() {
		return "[-->] PRODUCTEUR " + id + " : nombre de messages écrits  = " + nbMessProd + "\n\t\t     nombre de messages maximal = " + nbMessMax;
	}

}
