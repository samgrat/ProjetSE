package jus.poc.prodcons.v5;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {
	
	protected int id;														
	protected int nbMessMax;												
	protected int nbMessProd;												
	
	private static int idStatic = 1;
	private int cpt, cptMax;												
	private Aleatoire random;												

	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement,	int deviationTempsDeTraitement) throws ControlException { 
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		random = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		cptMax = random.next();
		cpt = 0;
		
		id = idStatic; 
		idStatic ++;
		nbMessMax = Aleatoire.valeur(5, 10) + 1; 							
		nbMessProd = 0;
		
		observateur.newProducteur(this);	// newProducteur 
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
		while (nbMessMax != nbMessProd) {									
			if (cpt < cptMax)                       						                                    
				cpt++;														
			else {															
				try {														
					MessageX mess = new MessageX(id, nbMessProd);			
					TestProdCons.TEST.getBuffer().put(this, mess);			
					nbMessProd++;											
					TestProdCons.TEST.getObservateur().productionMessage(this, mess, cptMax);	// productionMessage
				} catch (InterruptedException e) {
					e.printStackTrace();
					} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				cptMax = random.next();										
				cpt = 0;													
			}
		}
		
		TestProdCons.TEST.remove(this);
	}
	
	public String toString() {
		return "[-->] PRODUCTEUR " + id + " : nombre de messages écrits  = " + nbMessProd + "\n\t\t     nombre de messages maximal = " + nbMessMax;
	}

}
