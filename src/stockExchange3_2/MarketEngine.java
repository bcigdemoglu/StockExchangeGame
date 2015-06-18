package stockExchange3_2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class MarketEngine {
	
	private LinkedList<Double> bidVal;
	private LinkedList<Double> askVal;
	
	private int maxDays;
	
	private int day;
	private Random randGen = new Random();
	private double trendVal;
	
	

	public MarketEngine() throws IOException {
		bidVal = new LinkedList<Double>();
		askVal = new LinkedList<Double>();
		BufferedReader br = new BufferedReader(new FileReader("sekerbankUsdSatis2.txt"));
		String sCurrentLine;
		sCurrentLine = br.readLine();
		for (String usdBuy: sCurrentLine.split(",")) {
			askVal.add(Double.parseDouble(usdBuy));
		}
		ListIterator<Double> listIterator = askVal.listIterator();
		while(listIterator.hasNext()) {
			bidVal.add(listIterator.next() - 0.010);
		}
		
//		BufferedReader br = new BufferedReader(new FileReader("sekerbank2009.txt"));
//		String sCurrentLine;
//		while((sCurrentLine = br.readLine()) != null) {
//			askVal.addFirst(Double.parseDouble(sCurrentLine));
//		}
//		for (String usdBuy: sCurrentLine.split(",")) {
//			askVal.add(Double.parseDouble(usdBuy));
//		}
//		ListIterator<Double> listIterator = askVal.listIterator();
//		while(listIterator.hasNext()) {
//			bidVal.add(listIterator.next() - 0.010);
//		}
		
		day = 0;
		maxDays = askVal.size() - 1;
		br.close();
	}
	
	
	public boolean nextDay() {
		day++;
		if(day == maxDays) {
			return false;
		}
		bidVal.get(day);
		askVal.get(day);
		return true;
		
	}
	
	public void newTrend() {
		trendVal = (randGen.nextGaussian() + 0.0001) / 100 ;
		if (bidVal.get(day) + (trendVal) <= 0 ) {
			newTrend();
		}
	}
   
   public double getTrendVal() {
      return bidVal.get(day) - bidVal.get(day-1);
   }
   
   public int getTrendSignNum() {
   
      double trendCoeff = Math.abs(getTrendVal());
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
