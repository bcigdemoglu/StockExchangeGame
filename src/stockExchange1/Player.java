package stockExchange1;

public class Player {
	private double balanceTRY;
	private double balanceUSD;
	
	public Player(){
		balanceTRY = 10000;
		balanceUSD = 10000;
	}
	
	public double getUSD() {
		return balanceUSD;
	}
	
	public double getTRY() {
		return balanceTRY;
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
	
}
