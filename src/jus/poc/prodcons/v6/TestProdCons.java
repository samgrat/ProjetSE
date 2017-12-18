package jus.poc.prodcons.v6;

import java.util.Vector;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.ControlException;

public class TestProdCons extends Simulateur {
	
	public final static TestProdCons TEST = new TestProdCons(new Observateur());
	
	protected static Vector<Consommateur> consumerVector;
	protected static Vector<Producteur> producerVector;
	protected static ProdCons buffer;
	
	protected java.util.Properties option;
	
	protected void run() throws Exception {
		init();
		
		for (int i = 0; i < producerVector.size(); i++)
			producerVector.get(i).start();
		for (int i = 0; i < consumerVector.size(); i++)
			consumerVector.get(i).start();
		}										
		
	void remove(Consommateur p) {
		consumerVector.remove(p);
		}
	
	void remove(Producteur p) {
		producerVector.remove(p);
		}
	
	public ProdCons getBuffer() {
		return buffer;
		}
	
	public Observateur getObservateur() {
		return observateur;
	}
	
	public boolean finished() {
		return (consumerVector.size() == 0 && buffer.enAttente() == 0);
	}
		
	public TestProdCons(Observateur observateur) {
		super(observateur);
		}
	
	/**
	* Retreave the parameters of the application.
	* @param file the final name of the file containing the options.
	*/
	protected void init() {
		MessageX.reinitID();
		
		System.out.println("---------- NOUVEAU TEST ----------");
		System.out.println("\n");
		
		// retreave the parameters of the application
		final class Properties extends java.util.Properties {
			private static final long serialVersionUID = 1L;
			
//			public int get(String key) {
//				return Integer.parseInt(getProperty(key));
//				}
			
			public Properties(String file) {
				try {
					loadFromXML(ClassLoader.getSystemResourceAsStream(file));
					}
				catch(Exception e) {
					e.printStackTrace();
					}
				}
			}
		
		// les options sont toujours obtenues via le fichier xml
				option = new Properties("jus/poc/prodcons/options/" + "options.xml");

				int nbProd = Integer.parseInt((String) option.get("nbProd"));
				int nbCons = Integer.parseInt((String) option.get("nbCons"));
				int nbBuffer = Integer.parseInt((String) option.get("nbBuffer"));

				try { // initialisation de l'observateur
					observateur.init(nbProd, nbCons, nbBuffer);
				} catch (ControlException e) {
					e.printStackTrace();
				}

				producerVector = new Vector<Producteur>();
				consumerVector = new Vector<Consommateur>();

				buffer = new ProdCons(nbBuffer);

				try {
					for (int i = 0; i < nbProd; i++){
						Producteur producteur = new Producteur(observateur, Integer.parseInt((String) option.get("tempsMoyenProduction")),
								Integer.parseInt((String) option.get("deviationTempsMoyenProduction")));
						producerVector.add(producteur); // on garde une copie de notre producteur dans le producerVector
					}
					for (int i = 0; i < nbCons; i++){
						Consommateur consommateur = new Consommateur(observateur, Integer.parseInt((String) option.get("tempsMoyenConsommation")),
								Integer.parseInt((String) option.get("deviationTempsMoyenConsommation")));
						consumerVector.add(consommateur); // on garde une copie de notre consommateur dans le consumerVector
					}
				} catch (ControlException e) {
					e.printStackTrace();
				}
		}
	
	public static void main(String[] args) {
		TEST.start();
		}
	
	}
