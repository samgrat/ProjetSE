package jus.poc.prodcons.v3;

import java.util.Vector;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.ControlException;

public class TestProdCons extends Simulateur {
	
	public final static TestProdCons TEST = new TestProdCons(new Observateur());
	
	protected static Vector<Consommateur> consumerVector;
	protected static Vector<Producteur> producerVector;
	protected static ProdCons buffer;
	
	protected void run() throws Exception {
		
		int nbProd = (int) option.get("nbProd");
		int nbCons = (int) option.get("nbCons");
		int nbBuffer = (int) option.get("nbBuffer");
		
		init(nbProd, nbCons, nbBuffer);
		
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
	
	protected java.util.Properties option;
	
	public TestProdCons(Observateur observateur) {
		super(observateur);
		}
	
	/**
	* Retreave the parameters of the application.
	* @param file the final name of the file containing the options.
	*/
	protected void init(int nbConsommateurs, int nbProducteurs, int nbBuffers) {
		
		System.out.println("---------- NOUVEAU TEST ----------");
		System.out.println("\n");
		
		// retreave the parameters of the application
		final class Properties extends java.util.Properties {
			private static final long serialVersionUID = 1L;
			
			public int get(String key) {
				return Integer.parseInt(getProperty(key));
				}
			
			public Properties(String file) {
				try {
					loadFromXML(ClassLoader.getSystemResourceAsStream(file));
					}
				catch(Exception e) {
					e.printStackTrace();
					}
				}
			}
		
		Properties option = new Properties("jus/poc/prodcons/options/"+"options.xml");
		
		producerVector = new Vector<Producteur>();
		consumerVector = new Vector<Consommateur>();
		buffer = new ProdCons(nbBuffers);
		
		try {
			for (int i = 0; i < nbProducteurs; i++)
				producerVector.add(new Producteur(observateur, option.get("tempsMoyenProduction"), option.get("deviationTempsMoyenProduction")));
			for (int i = 0; i < nbConsommateurs; i++)
				consumerVector.add(new Consommateur(observateur, option.get("tempsMoyenConsommation"), option.get("deviationTempsMoyenConsommation")));
		} 
		catch (ControlException e) {
			e.printStackTrace();
			}
		}
	
	public static void main(String[] args) {
		TEST.start();
		}
	
	}
