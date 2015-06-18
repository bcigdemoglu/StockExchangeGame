package stockExchange2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Market extends JPanel{
	
   private static int totalMonths = 1000;
   private static volatile int waitTimeMillis = 5000;
   
	private static MarketEngine marketEngine;
	private static Player player1;
	private static AI ai;
	private static AI.AIExchanger aiExchanger;
	private static Market market;
	private static JFrame frame;
	private static Market.MarketRunner marketRunner;
   
   static StringBuilder consoleViewer;
   static int consoleViewerMenuLength;
   
	public static void main(String[] args) throws InterruptedException {
		assignVariables();
		viewMenu();
		createFrame();
		runMarket();
		runAI();
		closeFrame();
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
					//Never faster than 0.3 of a second
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
		marketEngine = new MarketEngine(totalMonths);
		player1 = new Player();
		ai = new AI();
		frame = new JFrame("Left-Right Stock Market");
	}
	
	public static void runMarket() {
		marketRunner = market.new MarketRunner();
		marketRunner.start();
	}
	
	public static void runAI() {
		aiExchanger = ai.new AIExchanger();
		aiExchanger.start();
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
		ai.updateAI(marketEngine.getCurrBidVal(), marketEngine.getCurrAskVal(), marketEngine.getTrendVal());
	}
	
	public static void viewData() {
		consoleViewer.append(String.format("%5d %9.6f %9.6f %8s %10.2f %10.2f %10.2f %10.2f\n\n", 
											marketEngine.getDay(), marketEngine.getCurrAskVal(), marketEngine.getCurrBidVal(), 
											marketEngine.getTrendSign(), player1.getTRY(), player1.getUSD(),
											ai.getTRY(), ai.getUSD()));
		System.out.print(consoleViewer.toString());
		consoleViewer.setLength(consoleViewerMenuLength);
	}

   public static void viewMenu() {
	    consoleViewer.setLength(0);
		consoleViewer.append("   Ay     Satis      Alis  Degisim        TRY        USD      AI TRY    AI USD\n");
		consoleViewer.append("----- --------- --------- -------- ---------- ---------- ---------- ----------\n");
		consoleViewerMenuLength = consoleViewer.length();
   }
	
}