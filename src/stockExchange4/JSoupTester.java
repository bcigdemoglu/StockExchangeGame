package stockExchange4;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupTester {

	private static Document web;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		String html = "http://example.com/";
		Document doc = Jsoup.parse(html); 
		String text = doc.body().text(); // "An example link"
		System.out.println(text);
		
		Document doc2;
		Element textContents3;
		double bid;
		double ask;
		
		while(true) {
			while(true)
				try {
					web = Jsoup.connect("http://www.marketwatch.com/investing/currency/eurusd").post();
					break;
				} catch (IOException e) {
				}
			textContents3 = web.getElementById("rates").select("tr").first().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();
			bid = Double.parseDouble(textContents3.select("td.bgBid").text());
			System.out.println(bid);
			ask = Double.parseDouble(textContents3.select("td.bgAsk").text());
			System.out.println(ask);
			
			System.out.println("Bitti");
		}
		
//		int i = 0;
//		while(i < 100) {
//
//			try {
//				i++;
//				doc2 = Jsoup.connect("http://www.marketwatch.com/investing/currency/eurusd").post();
//				textContents3 = doc2.getElementById("rates").select("tr").first().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();
//				bid = Double.parseDouble(textContents3.select("td.bgBid").text());
//				System.out.println(bid);
//				ask = Double.parseDouble(textContents3.select("td.bgAsk").text());
//				System.out.println(ask);
//			} catch (IOException e) {
//				System.out.println("error");
//			} 
//		}
	}

}
