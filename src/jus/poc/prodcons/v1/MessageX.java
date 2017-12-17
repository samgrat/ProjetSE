package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int num;
	private int idProd;
	
	public MessageX(int id, int messNbr) {
		idProd = id;
		num = messNbr;
	}
	
	public String toString() {
		return "Message : producteur " + idProd + "; numéro " + num;
	}

}
