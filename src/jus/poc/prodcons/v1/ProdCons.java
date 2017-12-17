package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	
	private int bufferSize;													// taille du buffer
	private int lec;														// indice du message retiré (lecture)
	private int ecr;														// indice du message déposé (écriture)
	protected Message[] buffer;												// le buffer est un vecteur de messages
	
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
		Message mess;
				
		synchronized(this) {
			while (lec == ecr) {
				System.out.println("[...] CONSOMMATEUR " + cons.identification() + " en attente");
				wait();
				}
			
			mess = buffer[lec];												// le message est retiré du buffer depuis l'endroit approprié
			lec++;															// on incrémente l'indice du dernier message retiré du buffer 
		
			if (lec == bufferSize) {										// si cet indice est égal à la taille du buffer
				ecr = ecr%bufferSize;										// alors l'indice du dernier message déposé dans le buffer correspond au premier élément du buffer
				lec = 0;													// et on réinitialise l'indice du dernier message retiré du buffer
			}
			
			System.out.print(cons); 
			System.out.println(" + " + 1);
			System.out.println("\t\t       " + mess);
		
			notifyAll();
		}
		
		return mess;
	}
	

	@Override
	public void put(_Producteur prod, Message mess) throws Exception, InterruptedException {
		
		synchronized(this) {
			
			while (ecr - lec >= bufferSize) {
				System.out.println("[...] PRODUCTEUR " + prod.identification() + " est en attente");
				wait();
			}
																			
			buffer[ecr%bufferSize] = mess;									// le message est déposé dans le buffer au prochain endroit disponible
			ecr++;															// on incrémente l'indice du dernier message déposé dans le buffer
			
			System.out.print(prod); 
			System.out.println(" + " + 1);
			System.out.println("\t\t     " + mess);
			
			notifyAll();
		}
	}

	@Override
	public int taille() {
		return bufferSize;
	}
}
