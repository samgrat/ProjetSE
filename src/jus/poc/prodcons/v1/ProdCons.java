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
	public synchronized Message get(_Consommateur cons) throws Exception, InterruptedException {

		while (lec == ecr) {
			System.out.println("Le consommateur " + cons.identification() + " est en attente \n");
			wait();
		}

		Message mess = buffer[lec];
		lec++;

		System.out.print("\t" + cons + " + " + 1 + "\t" + "  :  " + mess + "\n");

		if (lec == bufferSize) {
			
			ecr = 0;
			lec = 0;
		}

		notifyAll();

		return mess;
	}

	@Override
	public synchronized void put(_Producteur prod, Message mess) throws Exception, InterruptedException {
		while (ecr - lec >= bufferSize) {
			System.out.println("Le producteur " + prod.identification() + " est en attente \n");
			wait();
		}

		buffer[ecr] = mess;
		ecr++;

		System.out.print("\t" + prod + " + " + 1 + "\t" + "  :  " + mess + "\n");

		notifyAll();
	}

	@Override
	public int taille() {
		return bufferSize;
	}
}
