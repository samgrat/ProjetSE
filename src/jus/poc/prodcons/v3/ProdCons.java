package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	
	final class Semaphore {
		private int k = 1;
		
		public void suspend() throws InterruptedException {					
			synchronized(this) {
				k--;
				if (k < 0 )
					wait();													  
			}				
		}
		
		public void wakeup() {												
			synchronized(this) {
				k++;
				notify();													
			}
		}
		
		
	}
	
	private Semaphore semaProd;
	private Semaphore semaCons;	
	
	private int bufferSize;													
	private int lec;														
	private int ecr;														
	protected Message[] buffer;												
	
	public ProdCons(int sizeOfBuffer) {
		semaProd = new Semaphore();
		semaCons = new Semaphore();
		
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
		
		System.out.println("[---] CONSOMMATEUR " + cons.identification() + " en demande de lecture");
		
		semaCons.suspend();
				
		synchronized(this) {
			while (lec == ecr) {
				wait();
			}
			
			mess = buffer[lec];												
			lec++;														 
		
			if (lec == bufferSize) {										
				ecr = ecr%bufferSize;										
				lec = 0;													
			}
			
			TestProdCons.TEST.getObservateur().retraitMessage(cons, mess);	// retraitMessage
			
			System.out.print(cons); 
			System.out.println(" + " + 1);
			System.out.println("\t\t       " + mess);
		
			notifyAll();
		}
		
		semaCons.wakeup();
		
		return mess;
	}
	

	@Override
	public void put(_Producteur prod, Message mess) throws Exception, InterruptedException {
		
		System.out.println("[+++] PRODUCTEUR " + prod.identification() + " en demande d'écriture");
		
		semaProd.suspend();
		
		synchronized(this) {
			
			while (ecr - lec >= bufferSize) {
				wait();
			}
																			
			buffer[ecr%bufferSize] = mess;									
			ecr++;															
			
			TestProdCons.TEST.getObservateur().depotMessage(prod, mess);	// depotMessage
			
			System.out.print(prod); 
			System.out.println(" + " + 1);
			System.out.println("\t\t     " + mess);
			
			notifyAll();
		}
		
		semaProd.wakeup();
	}

	@Override
	public int taille() {
		return bufferSize;
	}
}
