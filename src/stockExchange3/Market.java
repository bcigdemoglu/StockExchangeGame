package stockExchange3;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Market extends JPanel{
	
   private static int totalMonths = 1000;
   private static volatile int waitTimeMillis = 5000;
   
	private static MarketEngine marketEngine;
	private static Player player1;
	private static AI1 ai1;
	private static AI1.AIExchanger aiExchanger1;
	private static AI2 ai2;
	private static AI2.AIExchanger aiExchanger2;
	private static Market market;
	private static JFrame frame;
	private static Scanner scanner;
	private static Market.MarketRunner marketRunner;
   
   static StringBuilder consoleViewer;
   static int consoleViewerMenuLength;
   
	public static void main(String[] args) throws InterruptedException {
		assignVariables();
		System.out.print("Enter the number of months: ");
		totalMonths = scanner.nextInt();
		viewMenu();
		createFrame();
		runMarket();
		runAI();
		closeFrame();
		marketRunner.join();
		System.out.println("Game Over\n");
		System.out.println("GameStats:");
		System.out.println("Player 1: " + player1.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("     AI1: " + ai1.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
		System.out.println("     AI2: " + ai2.endGame(marketEngine.getCurrAskVal(),marketEngine.getCurrBidVal()));
	}
	
	public Market() {
		KeyListener listener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent key) {
			    if (key.getKeyCode() == KeyEvent.VK_LEFT){
			    	if(player1.sellAllUSD(marketEngine.getCurrBidVal())){
			    		waitTimeMillis += 250;
			    	}
			    }
			    if (key.getKeyCode() == KeyEvent.VK_RIGHT){
			    	if(player1.sellAllTRY(marketEngine.getCurrAskVal())) {
			    		waitTimeMillis += 250;
			    	}
			    }
			}

			@Override
			public void keyReleased(KeyEvent e) { }

			@Override
			public void keyTyped(KeyEvent e) { }
		};
		setFocusable(true);
		addKeyListener(listener);
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
					Thread.sleep(waitTimeMillis);
					waitTimeMillis -= marketEngine.getTrendSignNum() * 10;
               
					//Never slower than a second
					if(waitTimeMillis > 1500) {
						waitTimeMillis = 1500;
					} 
					//Never faster than 0.5 of a second
					else if (waitTimeMillis < 300) {
						waitTimeMillis = 300;
					}
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
		player1 = new Player();
		ai1 = new AI1();
		ai2 = new AI2();
		scanner = new Scanner(System.in);
		frame = new JFrame("Left-Right Stock Market");
	}
	
	public static void runMarket() {
		marketEngine = new MarketEngine(totalMonths);
		marketRunner = market.new MarketRunner();
		marketRunner.start();
	}
	
	public static void runAI() {
		aiExchanger1 = ai1.new AIExchanger();
		aiExchanger1.start();
		aiExchanger2 = ai2.new AIExchanger();
		aiExchanger2.start();
	}
	
	public static void createFrame() {
		frame.add(market);
		frame.setSize(100, 100);
		frame.setVisible(true);
	}
	
	public static void closeFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void updateAI() {
		ai1.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
		ai2.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
	}
	
	public static void viewData() {
		consoleViewer.append(String.format("%5d %7.4f %7.4f %8s   %8.0f %8.0f   %8.0f %8.0f %8.0f %8.0f\n\n", 
											marketEngine.getDay(), marketEngine.getCurrAskVal(), marketEngine.getCurrBidVal(), 
											marketEngine.getTrendSign(), player1.getTRY(), player1.getUSD(),
											ai1.getTRY(), ai1.getUSD(), ai2.getTRY(), ai2.getUSD()));
		System.out.print(consoleViewer.toString());
		consoleViewer.setLength(consoleViewerMenuLength);
	}

   public static void viewMenu() {
	    consoleViewer.setLength(0);
		consoleViewer.append("   Ay   Satis    Alis  Degisim        TRY      USD    AI1 TRY  AI1 USD  AI2 TRY  AI2 USD\n");
		consoleViewer.append("----- ------- ------- --------   -------- --------   -------- -------- -------- --------\n");
		consoleViewerMenuLength = consoleViewer.length();
   }
	
}