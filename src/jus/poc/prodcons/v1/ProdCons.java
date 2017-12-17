package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	
	private int bufferSize;
	private int lec;
	private int ecr;
	protected Message[] buffer;
	
	public ProdCons(int sizeOfBuffer) {
		buffer = new Message[sizeOfBuffer];
		bufferSize = sizeOfBuffer;
		lec = 0;
		ecr = 0;
	}

	public int enAttente() {
		return ecr - lec;
	}

	@Override
	public Message get(_Consommateur cons) throws Exception, InterruptedException {
		
		while (lec == ecr) {
			System.out.println("Le consommateur " + cons.identification() + " est en attente");
			wait();
		}
			
		Message mess = buffer[lec];
		lec++;
		
		if (lec == bufferSize) {
			ecr = ecr%bufferSize;
			lec = 0;
		}
			
		System.out.print("\t" + cons + " + " + 1 + "\t" + "  :  " + mess); 
		
		notifyAll();
		
		return mess;
	}
	

	@Override
	public void put(_Producteur prod, Message mess) throws Exception, InterruptedException {
		while (ecr - lec >= bufferSize) {
			System.out.println("Le producteur " + prod.identification() + " est en attente");
			wait();
		}

		buffer[ecr%bufferSize] = mess;
		ecr++;
			
		System.out.print("\t" + prod + " + " + 1 + "\t" + "  :  " + mess);
			
		notifyAll();
	}

	@Override
	public int taille() {
		return bufferSize;
	}
}
