import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class RefleksTesti {
	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();

		System.out.println("🧠 Refleks Testi Başlıyor!");
		System.out.println("Hazır olduğunda Enter'a bas...");
		scanner.nextLine();

		System.out.println("Bekle... Ama sakın basma ha!");
		int beklemeSuresi = 2000 + random.nextInt(3000); // 2-5 saniye

		long startBekleme = System.currentTimeMillis();
		long gecenSure = 0;

		while (gecenSure < beklemeSuresi) {
			if (System.in.available() > 0) {
				scanner.nextLine(); // erken gelen Enter'ı yut
				System.out.println("🚫 Çok erken bastın enayi! Daha sinyal gelmemişti 😄");
				scanner.close();
				return; // oyunu bitir
			}
			gecenSure = System.currentTimeMillis() - startBekleme;
		}

		System.out.println("💥 ŞİMDİ! Enter'a hemen bas!");
		long startTime = System.currentTimeMillis();

		scanner.nextLine(); // burada gerçek tepkini ölçeriz
		long endTime = System.currentTimeMillis();

		long reaksiyonSuresi = endTime - startTime;
		System.out.println("⏱ Reaksiyon süren: " + reaksiyonSuresi + " milisaniye!");

		if (reaksiyonSuresi < 250) {
			System.out.println("🚀 İnanılmaz hızlısın!");
		} else if (reaksiyonSuresi < 500) {
			System.out.println("⚡ Güzel refleks!");
		} else {
			System.out.println("🐢 Biraz geç kaldın ama sorun yok!");
		}

		scanner.close();
	}
	
}
