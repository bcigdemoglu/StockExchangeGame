package stockExchange4;

public class AI2 extends Player{

	private double lastBid;
	private double lastAsk;
	private double lastTrend;
	private double lastTRY;
	private double lastUSD;
	private double currBid;
	private double currAsk;
	private double currTrend;
	private boolean updatedAI;
	
	private boolean secondEntry;
	private boolean unlockedExchange;
	
	class AIExchanger extends Thread {
		private volatile boolean running = true;
		@Override
		public void run() {
			while(running) {
				if(updatedAI) {
//					long startTime = System.nanoTime();
					//System.out.printf("%4.2f %4.2f %4.2f %4.2f " + updatedAI + "\n", lastBid, lastAsk, currBid, currAsk);

					if(exchangeTRY()) {
						saveData();
					}
					else if(exchangeUSD()) {
						saveData();
					}
					updatedAI = false;
//					long endTime = System.nanoTime();
//			        System.out.println("It took " + (endTime - startTime) + " ns");
				}
			}
		}
		public void shutdown() {
			running = false;
		}
	}
	
	public AI2() {
		lastBid = 1.0;
		lastAsk = 2.0;
		lastTRY = getTRY();
		lastUSD = getUSD();
		updatedAI = false;
		// TODO Auto-generated constructor stub
	}
	
	public boolean exchangeTRY() {
		if(hasTRY()) {
			if(calculateUSD(currAsk) > lastUSD) {
				if(secondEntry) {
					if(changedTrend()) {
						unlockedExchange = true;
					}
				}
				if(unlockedExchange) {
					lastTRY = getTRY();
					sellAllTRY(currAsk);
					secondEntry = false;
					unlockedExchange = false;
					return true;
				}
				secondEntry=true;
			}
		}
		return false;
	}
	
	public boolean exchangeUSD() {
		if(hasUSD()) {
			if(calculateTRY(currBid) > lastTRY) {
				if(secondEntry) {
					if(changedTrend()) {
						unlockedExchange = true;
					}
				}
				if(unlockedExchange) {
					lastUSD = getUSD();
					sellAllUSD(currBid);
					secondEntry = false;
					unlockedExchange = false;
					return true;
				}
				secondEntry=true;
			}
		}
		return false;
	}
	
	public void saveData() {
		lastBid = currBid;
		lastAsk = currAsk;
	}
	
	public void updateAI(double currBid, double currAsk, double currTrend) {
		lastTrend = this.currTrend;
		this.currTrend = currTrend;
		if(this.currBid == currBid) {
			updatedAI = false;
		}
		else {
			this.currBid = currBid;
			this.currAsk = currAsk;
			this.currTrend = currTrend;
			updatedAI = true;
		}
	}
	
	public boolean changedTrend() {
		return (lastTrend / currTrend) < 0;
	}
	
}
