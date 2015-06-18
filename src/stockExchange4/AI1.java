package stockExchange4;

public class AI1 extends Player{

	private double lastTRY;
	private double lastUSD;
	private double currBid;
	private double currAsk;
	private boolean updatedAI;
	
	class AIExchanger extends Thread {
		private volatile boolean running = true;
		@Override
		public void run() {
			while(running) {
				if(updatedAI) {
					if(exchangeTRY()) {
						saveData();
					}
					else if(exchangeUSD()) {
						saveData();
					}
					updatedAI = false;
				}
			}
		}
		public void shutdown() {
			running = false;
		}
	}
	
	public AI1() {
		lastTRY = getTRY();
		lastUSD = getUSD();
		updatedAI = false;
		// TODO Auto-generated constructor stub
	}
	
	public boolean exchangeTRY() {
		if(hasTRY()) {
			if(calculateUSD(currAsk) > lastUSD) {
				lastTRY = getTRY();
				sellAllTRY(currAsk);
				return true;
			}
		}
		return false;
	}
	
	public boolean exchangeUSD() {
		if(hasUSD()) {
			if(calculateTRY(currBid) > lastTRY) {
				lastUSD = getUSD();
				sellAllUSD(currBid);
				return true;
			}
		}
		return false;
	}
	
	public void saveData() {
	}
	
	public void updateAI(double currBid, double currAsk, double currTrend) {
		if(this.currBid == currBid) {
			updatedAI = false;
		}
		else {
			this.currBid = currBid;
			this.currAsk = currAsk;
			updatedAI = true;
		}
	}
	
}
