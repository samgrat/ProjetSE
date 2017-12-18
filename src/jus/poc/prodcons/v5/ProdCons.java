package jus.poc.prodcons.v5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	final Lock lock = new ReentrantLock();
	final Condition CondProd = lock.newCondition();
	final Condition CondCons = lock.newCondition();

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
		Message mess;

		System.out.println("[---] CONSOMMATEUR " + cons.identification() + " en demande de lecture");

		lock.lock();

		try {
			while (lec == ecr) {
				CondCons.await();
			}

			mess = buffer[lec];
			lec++;

			if (lec == bufferSize) {
				ecr = ecr % bufferSize;
				lec = 0;
			}

			TestProdCons.TEST.getObservateur().retraitMessage(cons, mess); // retraitMessage

			System.out.print(cons);
			System.out.println(" + " + 1);
			System.out.println("\t\t       " + mess);

			CondProd.signal();
		} finally {

			lock.unlock();
		}

		return mess;
	}

	@Override
	public void put(_Producteur prod, Message mess) throws Exception, InterruptedException {

		System.out.println("[+++] PRODUCTEUR " + prod.identification() + " en demande d'écriture");

		lock.lock();

		try {

			while (ecr - lec >= bufferSize) {
				CondProd.await();
			}

			buffer[ecr % bufferSize] = mess;
			ecr++;

			TestProdCons.TEST.getObservateur().depotMessage(prod, mess); // depotMessage

			System.out.print(prod);
			System.out.println(" + " + 1);
			System.out.println("\t\t     " + mess);

			CondCons.signal();
		} finally{
			lock.unlock();
		}
	}

	@Override
	public int taille() {
		return bufferSize;
	}
}
