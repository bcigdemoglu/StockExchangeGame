package stockExchange1;
import java.util.LinkedList;
import java.util.Random;

public class MarketEngine {
	
	private LinkedList<Double> bidVal;
	private LinkedList<Double> askVal;
	
	// Days left for the increasing or decreasing trend
	// Never exceeds 10 days
	private int remainingTrend;
	
	// Maximum days of trend, can be modified
	private int maxTrend;
	
	private int maxDays;
	
	private int DEFAULT_MAX_TREND = 5;
	
	private int day;
	private double spread;
	private Random randGen = new Random();
	private double trendVal;

	public MarketEngine(int days) {
		day = 0;
		maxDays = days;
		bidVal = new LinkedList<Double>();
		askVal = new LinkedList<Double>();
		spread = randGen.nextDouble() / maxDays;
		bidVal.add(1.0);
		askVal.add(bidVal.get(0) + spread);
		maxTrend = DEFAULT_MAX_TREND; // default
	}
	
	public void setSpread(double spread){
		this.spread = spread;
	}
	
	public double getSpread() {
		return spread;
	}

	public void setMaxTrend(int maxTrend) {
		this.maxTrend = maxTrend;
	}
	
	public boolean nextDay() {
		updateTrend();
		day++;
		if(day == maxDays) {
			return false;
		}
		bidVal.add(bidVal.get(day-1) + trendVal);
		askVal.add(askVal.get(day-1) + trendVal + (randGen.nextDouble() - 0.1) / maxDays);
		return true;
		
	}
	
	public void newTrend() {
		trendVal = (randGen.nextGaussian() - 0.5 )/ 5.0 ;
		if (bidVal.get(day) + (trendVal * (double) (remainingTrend + 1)) <= 0 ) {
			newTrend();
		}
	}
	
	private void updateTrend() {
		if(remainingTrend == 0) {
			remainingTrend = randGen.nextInt(maxTrend) + 1;
			newTrend();
		}
		else {
			remainingTrend--;
		}
	}
   
   public double getTrendVal() {
      return trendVal;
   }
   
   public int getTrendSignNum() {
   
      double trendCoeff = Math.abs(trendVal);
      int numSigns = (int) (10 * trendCoeff + 1.0);
      
      if(numSigns < 5) {
         return numSigns;
      }
      else {
         return 5;
      }
      
   }
   
   public String getTrendSign() {
   
      int numSigns = getTrendSignNum();
      
      String S = "";
      for(int i=0; i < numSigns; i++) {
    	  if(getTrendVal() < 0) {
    		  S += "-";
    	  }
    	  else {
    		  S += "+";
    	  }
      }
      
      return S;
       
   }
	
	public int getDay(){
		return day;
	}
	
	public LinkedList<Double> getBidVal() {
		return bidVal;
	}
	
	public LinkedList<Double> getAskVal() {
		return askVal;
	}
	
	public double getCurrBidVal() {
		if(day == maxDays) {
			return bidVal.get(day-1);
		}
		return bidVal.get(day);
	}
	
	public double getCurrAskVal() {
		if(day == maxDays) {
			return askVal.get(day-1);
		}
		return askVal.get(day);
	}
	
	public double getLastBidVal() {
		if(day == 0) {
			return 0;
		}
		return bidVal.get(day-1);
	}
	
	public double getLastAskVal() {
		if(day == 0) {
			return 0;
		}
		return askVal.get(day-1);
	}
}
