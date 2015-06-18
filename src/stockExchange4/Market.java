package stockExchange4;
import java.io.IOException;
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
	private static Market market;
	private static Scanner scanner;
	private static Market.MarketRunner marketRunner;
	
	private static double startTRY;
	private static double startUSD;
   
   static StringBuilder consoleViewer;
   static int consoleViewerMenuLength;
   
	public static void main(String[] args) throws InterruptedException, IOException {
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
		System.out.println();
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
		
		
	}
	
	public static void getStartBalance() {
//		System.out.print("Starting USD: ");
//		startUSD = scanner.nextDouble();
//		System.out.print("Starting TRY: ");
//		startTRY = scanner.nextDouble();
//		System.out.println();
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
		ai1.setStartBalance(startTRY, startUSD);
		ai2.setStartBalance(startTRY, startUSD);
		ai3.setStartBalance(startTRY, startUSD);
	}
	
	public static void runAI() {
		aiExchanger1.start();
		aiExchanger2.start();
		aiExchanger3.start();
	}
	
	public static void updateAI() {
		ai1.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai2.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai3.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
	}
	
	public static void shutdownAI() throws InterruptedException {
		aiExchanger1.shutdown();
		aiExchanger2.shutdown();
		aiExchanger3.shutdown();
		
		aiExchanger1.join();
		aiExchanger2.join();
		aiExchanger3.join();
	}
	
	public static void viewData() {
		consoleViewer.append(String.format("%5d %8.4f %8.4f %8s %8.0f %8.0f %8.0f %8.0f %8.0f %8.0f\n\n", 
											marketEngine.getDay(), marketEngine.getCurrAskVal(), marketEngine.getCurrBidVal(), 
											marketEngine.getTrendSign(), 
											ai1.getTRY(), ai1.getUSD(), ai2.getTRY(), ai2.getUSD(), ai3.getTRY(), ai3.getUSD()));
		System.out.print(consoleViewer.toString());
		consoleViewer.setLength(0);
	}

   public static void viewMenu() {
	    consoleViewer.setLength(0);
		consoleViewer.append("   Ay    Satis     Alis  Degisim  AI1 USD  AI1 EUR  AI2 USD  AI2 EUR  AI3 USD  AI3 EUR\n");
		consoleViewer.append("----- -------- -------- -------- -------- -------- -------- -------- -------- --------\n");
		consoleViewerMenuLength = consoleViewer.length();
   }
	
}