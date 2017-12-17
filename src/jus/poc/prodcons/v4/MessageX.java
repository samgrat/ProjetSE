package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int num;
	private int idProd;
	private int nbExemplProd;		// nombre d'exemplaires du message a etre produit
	private int nbExemplCons;		// nombre d'exemplaires du message consommes
	
	public MessageX(int id, int messNbr, int nbExemplMess) {
		idProd = id;
		num = messNbr;
		nbExemplProd = nbExemplMess;
		nbExemplCons = 0;
	}
	
	public int GetnbExemplProd() {
		return nbExemplProd;
	}
	
	public int GetnbExemplCons() {
		return nbExemplCons;
	}
	
	public void Consume() {
		nbExemplCons++;
	}
	public String toString() {
		return "message [PRODUCTEUR " + idProd + ", NUMERO " + num + ", EXEMPLAIRES " + nbExemplProd + "]";
	}

}
