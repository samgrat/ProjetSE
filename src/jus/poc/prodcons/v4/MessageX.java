package jus.poc.prodcons.v4;

import java.util.ArrayList;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	public static int ID;			// l'id du message le plus recent
	
	private int num;
	private int idProd;
	private int idMessage;
	private int nbExemplProd;		// nombre d'exemplaires du message a etre produit
	private int nbExemplCons;		// nombre d'exemplaires du message consommes
	
	public MessageX(int id, int messNbr, int nbExemplMess) {
		idProd = id;
		num = messNbr;
		nbExemplProd = nbExemplMess;
		nbExemplCons = 0;
		
		idMessage = ID;
		ID++;
	}
	
	public static void reinitID(){
		ID = 0;
	}
	
	public int GetidMessage(){
		return idProd;
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
