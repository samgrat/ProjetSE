// use of wikipedia.org and stackoverflow
package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

// A bit buggy first line "Consommateur 1] Nombre de messages lus: 0 + 1	  :  null" why the fuck is that ??

public class ProdCons implements Tampon {

	final class semaphore {
		int state;

		public semaphore(int n) {
			state = n;
		}
		
		// prend une ressource
		public void P() throws InterruptedException {
			// si il n'y a pas de ressource disponible on attend
			while (state-1 < 0){
				try{
					Thread.sleep(1);}
				catch(InterruptedException e){
					throw new IllegalStateException(e);
				} 
			}
			state --;	
		}
		
		// ajoute une ressource
		public void V() {
			state++;
		}
	};

	public static semaphore nbespacevide;
	public static semaphore nbmessinbuffer;
	public static semaphore filedattente;

	private int bufferSize;
	private int lec;
	private int ecr;
	protected Message[] buffer;

	public ProdCons(int sizeOfBuffer) {
		buffer = new Message[sizeOfBuffer];
		bufferSize = sizeOfBuffer;
		lec = 0;
		ecr = 0;

		nbespacevide = new semaphore(sizeOfBuffer);
		nbmessinbuffer = new semaphore(0);
		filedattente = new semaphore(1);
	}

	public int enAttente() {
		return ecr - lec;
	}

	@Override
	public Message get(_Consommateur cons) throws Exception, InterruptedException {

		nbespacevide.P();
		filedattente.P();
		
		Message mess = buffer[lec];
		lec++;
		
		System.out.print("\t" + cons + " + " + 1 + "\t" + "  :  " + mess + "\n");

		if (lec == bufferSize) {

			ecr = 0;
			lec = 0;
		}
		
		filedattente.V();
		nbmessinbuffer.V();

		return mess;
	}

	@Override
	public void put(_Producteur prod, Message mess) throws Exception, InterruptedException {
		
		nbmessinbuffer.P();
		filedattente.P();

		buffer[ecr] = mess;
		ecr++;

		System.out.print("\t" + prod + " + " + 1 + "\t" + "  :  " + mess + "\n");

		filedattente.V();
		nbespacevide.V();
		
	}

	@Override
	public int taille() {
		return bufferSize;
	}
}
