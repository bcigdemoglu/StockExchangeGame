package stockExchange3_2;

public class Player {
	private double balanceTRY;
	private double balanceUSD;
	
	private double startTRY;
	private double startUSD;
	
	public Player(){
		balanceTRY = 0;
		balanceUSD = 10000;
		startTRY = balanceTRY;
		startUSD = balanceUSD;
	}
	
	public void setStartBalance(double TRY, double USD) {
		balanceTRY = TRY;
		balanceUSD = USD;
		startTRY = balanceTRY;
		startUSD = balanceUSD;
	}
	
	public double getUSD() {
		return balanceUSD;
	}
	
	public double getTRY() {
		return balanceTRY;
	}
	
	public boolean hasTRY() {
		return balanceTRY > 0;
	}

	public boolean hasUSD() {
		return balanceUSD > 0;
	}
	
	public boolean sellAllTRY(double askVal) {
      if(balanceTRY == 0) {
         return false;
      }
		return sellTRY(getTRY(),askVal);
	}
	
	public boolean sellAllUSD(double bidVal) {
      if(balanceUSD == 0) {
         return false;
      }
		return sellUSD(getUSD(),bidVal);
	}
	
	public boolean sellTRY(double TRY, double askVal) {
		if(balanceTRY - TRY >= 0) {
			balanceTRY -= TRY;
			balanceUSD += TRY / askVal;
			return true;
		}
		return false;
	}

	public boolean sellUSD(double USD, double bidVal) {
		if(balanceUSD - USD >= 0) {
			balanceUSD -= USD;
			balanceTRY += USD * bidVal;
			return true;
		}
		return false;
	}
	
	public double calculateUSD(double askVal) {
		return balanceUSD + balanceTRY / askVal;
	}
	
	public double calculateTRY(double bidVal) {
		return balanceTRY + balanceUSD * bidVal;
	}
	
	public boolean buyTRY(double TRY, double bidVal) {
		if(balanceUSD - TRY / bidVal >= 0) {
			balanceTRY += TRY;
			balanceUSD -= TRY / bidVal;
			return true;
		}
		return false;
	}

	public boolean buyUSD(double USD, double askVal) {
		if(balanceTRY - USD * askVal >= 0) {
			balanceUSD += USD;
			balanceTRY -= askVal * USD;
			return true;
		}
		return false;
	}
	
	public String startGame() {
		return String.format("%10.2f", startTRY) + " TRY  and" + String.format("%10.2f", startUSD) + " USD.";
	}
	
	public String endGame(double askVal, double bidVal) {
		return String.format("%20.2f", calculateTRY(bidVal)) + " TRY   or" + String.format("%20.2f", calculateUSD(askVal)) + " USD.";
	}
	
}
