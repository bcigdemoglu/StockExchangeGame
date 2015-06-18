package stockExchange4;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MarketEngine {
	
	private LinkedList<Double> bidVal;
	private LinkedList<Double> askVal;
	
	
	private int day;
	
	private Document web;
	private Element tableBidAsk;
	private PrintWriter writer;
	private Object timeStamp;
	private Object timeInfo;
	private double fetchedBidVal;
	private double fetchedAskVal;
	private Double storedAskVal;
	private Double storedBidVal;
	
	

	public MarketEngine() throws IOException {
		timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		writer = new PrintWriter("EurUsd_" + timeStamp + ".txt", "UTF-8");
		writer.println("Time           Ask    Bid");
		bidVal = new LinkedList<Double>();
		askVal = new LinkedList<Double>();
		storedBidVal = 0.0;
		storedAskVal = 0.0;
		fetchWebData();
		day = 0;
	}
	
	public void closeFile() {
		writer.close();
	}
	
	public void fetchWebData() {
		while(true)
			try {
				web = Jsoup.connect("http://www.marketwatch.com/investing/currency/eurusd").post();
				break;
			} catch (IOException e) { }
		
		tableBidAsk = web.getElementById("rates").select("tr").first().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();
		
		fetchedBidVal = Double.parseDouble(tableBidAsk.select("td.bgBid").text());
		fetchedAskVal = Double.parseDouble(tableBidAsk.select("td.bgAsk").text());
		if (storedBidVal != fetchedBidVal || storedAskVal != fetchedAskVal) {
			bidVal.add(fetchedBidVal);
			askVal.add(fetchedAskVal);
		} else {
			fetchWebData();
		}
		
	}
	
	
	public void writeToFile() {
		timeInfo = new SimpleDateFormat("MM dd HH mm ss").format(new Date());
		writer.printf(timeInfo + " %5.4f %5.4f\n", askVal.get(day), bidVal.get(day));
	}
	
	public boolean nextDay() {
		day++;
		fetchWebData();
		writeToFile();
		
		storedBidVal = bidVal.get(day);
		storedAskVal = askVal.get(day);
		return true;
		
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
		if(bidVal.size() == day) {
			return bidVal.get(day-1);
		}
		return bidVal.get(day);
	}
	
	public double getCurrAskVal() {
		if(askVal.size() == day) {
			return askVal.get(day - 1);
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
