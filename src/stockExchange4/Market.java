package stockExchange4;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Market{
   private static volatile int waitTimeMillis = 2;
   
	private static MarketEngine marketEngine;
	private static AI1 ai1;
	private static AI1.AIExchanger aiExchanger1;
	private static AI2 ai2;
	private static AI2.AIExchanger aiExchanger2;
	private static AI3 ai3;
	private static AI3.AIExchanger aiExchanger3;
	private static AI1 ai4;
	private static AI1.AIExchanger aiExchanger4;
	private static AI2 ai5;
	private static AI2.AIExchanger aiExchanger5;
	private static AI3 ai6;
	private static AI3.AIExchanger aiExchanger6;
	private static Market market;
	private static Scanner scanner;
	private static Market.MarketRunner marketRunner;
	
	private static double startTRY;
	private static double startUSD;
   
   static StringBuilder consoleViewer;
   static int consoleViewerMenuLength;

	private static String timeStamp;
	private static PrintWriter writer;

	private static String timeInfo;
   
	public static void main(String[] args) throws InterruptedException, IOException {
		timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		writer = new PrintWriter("Game_EurUsd_" + timeStamp + ".txt", "UTF-8");
		
		assignVariables();
		getStartBalance();
		viewMenu();
		runMarket();
		runAI();
		scanner.nextLine();
		marketRunner.shutdown();
		marketRunner.join();
		marketEngine.closeFile();
		shutdownAI();
//		System.out.println("Game Over\n");
//		System.out.println("GameStats:");
//		System.out.println(" AI Start: " + ai1.startGame());
//		Thread.sleep(1000);
		System.out.println("      AI1: " + ai1.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("      AI2: " + ai2.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("      AI3: " + ai3.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("      AI4: " + ai4.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("      AI5: " + ai5.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("      AI6: " + ai6.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println();
		
		writer.println("      AI1: " + ai1.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		writer.println("      AI2: " + ai2.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		writer.println("      AI3: " + ai3.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		writer.println("      AI4: " + ai4.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		writer.println("      AI5: " + ai5.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		writer.println("      AI6: " + ai6.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		writer.println();
		writer.close();
		//System.exit(0);
	}
	
	public Market() {
		super();
	}
	
	class MarketRunner extends Thread {
		private volatile boolean running = true;
		@Override
		public void run() {
			while(running) {
				if(!marketEngine.nextDay()) {
					break;
				}
				viewData();
				updateAI();
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public void shutdown() {
			running = false;
		}
	}
	
	public static void assignVariables() {
		consoleViewer = new StringBuilder(512);
		market = new Market();
		scanner = new Scanner(System.in);
		
		ai1 = new AI1();
		aiExchanger1 = ai1.new AIExchanger();
		
		ai2 = new AI2();
		aiExchanger2 = ai2.new AIExchanger();
		
		ai3 = new AI3();
		aiExchanger3 = ai3.new AIExchanger();
		
		ai4 = new AI1();
		aiExchanger4 = ai4.new AIExchanger();
		
		ai5 = new AI2();
		aiExchanger5 = ai5.new AIExchanger();
		
		ai6 = new AI3();
		aiExchanger6 = ai6.new AIExchanger();
		
		
	}
	
	public static void getStartBalance() {
		startUSD = 0;
		startTRY = 10000;
		aiSetBalance();
	}
	
	public static void runMarket() throws IOException {
		marketEngine = new MarketEngine();
		marketRunner = market.new MarketRunner();
		marketRunner.start();
	}
	
	public static void aiSetBalance() {
		ai1.setStartBalance(10000.0, 0.0);
		ai2.setStartBalance(10000.0, 0.0);
		ai3.setStartBalance(10000.0, 0.0);
		ai4.setStartBalance(0.0, 10000.0);
		ai5.setStartBalance(0.0, 10000.0);
		ai6.setStartBalance(0.0, 10000.0);
	}
	
	public static void runAI() {
		aiExchanger1.start();
		aiExchanger2.start();
		aiExchanger3.start();
		
		aiExchanger4.start();
		aiExchanger5.start();
		aiExchanger6.start();
	}
	
	public static void updateAI() {
		ai1.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai2.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai3.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		
		ai4.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai5.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai6.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
	}
	
	public static void shutdownAI() throws InterruptedException {
		aiExchanger1.shutdown();
		aiExchanger2.shutdown();
		aiExchanger3.shutdown();
		
		aiExchanger4.shutdown();
		aiExchanger5.shutdown();
		aiExchanger6.shutdown();
		
		aiExchanger1.join();
		aiExchanger2.join();
		aiExchanger3.join();
		
		aiExchanger4.join();
		aiExchanger5.join();
		aiExchanger6.join();
	}
	
	public static void viewData() {
		timeInfo = new SimpleDateFormat("MM dd HH mm ss").format(new Date());
		consoleViewer.append(String.format(timeInfo + " %8.4f %8.4f %6s %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f\n\n", 
											marketEngine.getCurrAskVal(), marketEngine.getCurrBidVal(), marketEngine.getTrendSign(), 
											ai1.getTRY(), ai1.getUSD(), ai2.getTRY(), ai2.getUSD(), ai3.getTRY(), ai3.getUSD(),
											ai4.getTRY(), ai4.getUSD(), ai5.getTRY(), ai5.getUSD(), ai6.getTRY(), ai6.getUSD()));
		System.out.print(consoleViewer.toString());
		writer.printf(timeInfo + " %8.4f %8.4f %6s %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f\n\n", 
											marketEngine.getCurrAskVal(), marketEngine.getCurrBidVal(), marketEngine.getTrendSign(), 
											ai1.getTRY(), ai1.getUSD(), ai2.getTRY(), ai2.getUSD(), ai3.getTRY(), ai3.getUSD(),
											ai4.getTRY(), ai4.getUSD(), ai5.getTRY(), ai5.getUSD(), ai6.getTRY(), ai6.getUSD());
		consoleViewer.setLength(0);
	}

   public static void viewMenu() {
	    consoleViewer.setLength(0);
			    writer.print("MM dd HH mm ss      Ask      Bid   Sign  AI1 USD  AI1 EUR  AI2 USD  AI2 EUR  AI3 USD  AI3 EUR  AI4 USD  AI4 EUR  AI5 USD  AI5 EUR  AI6 USD  AI6 EUR\n");
			    writer.print("-- -- -- -- -- -------- -------- ------ -------- -------- -------- -------- -------- -------- -------- -------- -------- -------- -------- --------\n");
		consoleViewer.append("MM dd HH mm ss      Ask      Bid   Sign  AI1 USD  AI1 EUR  AI2 USD  AI2 EUR  AI3 USD  AI3 EUR  AI4 USD  AI4 EUR  AI5 USD  AI5 EUR  AI6 USD  AI6 EUR\n");
		consoleViewer.append("-- -- -- -- -- -------- -------- ------ -------- -------- -------- -------- -------- -------- -------- -------- -------- -------- -------- --------\n");
		consoleViewerMenuLength = consoleViewer.length();
   }
	
}