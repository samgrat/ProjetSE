package jus.poc.prodcons.v3;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	protected int id;
	protected int nbMessCons;
	
	private static int idStatic = 1;
	private int cpt, cptMax;
	private Aleatoire random;
	
	protected Consommateur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		random = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);

		cptMax = random.next();											
		cpt = 0;
		
		id = idStatic; 
		idStatic ++;
		nbMessCons = 0;
		
		observateur.newConsommateur(this);	// newConsommateur
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
		return nbMessCons;
		}

	@Override
	public void run() {
		while (!TestProdCons.TEST.finished()) {							
			if (cpt < cptMax)											
				cpt++;													
			else {														
				try {
					Message mess = TestProdCons.TEST.getBuffer().get(this);
					TestProdCons.TEST.getBuffer().get(this);			
					nbMessCons++;										
					TestProdCons.TEST.getObservateur().consommationMessage(this, mess, cptMax);	// consommationMessage
				} catch (InterruptedException e) {
					e.printStackTrace();
					} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				cpt = random.next();										
				cpt = 0;													
			}
		}
		
		TestProdCons.TEST.remove(this);
	}
	
	public String toString() {
		return "[<--] CONSOMMATEUR " + id + " : nombre de messages lus = " + nbMessCons;
	}

}
