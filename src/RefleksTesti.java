import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class RefleksTesti {
	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();

		System.out.println("🎮 Refleks Testi Oyununa Hoş Geldin!");
		System.out.print("Adını gir: ");
		String oyuncuAdi = scanner.nextLine();

		System.out.println("\nZorluk Seviyesi Seç:");
		System.out.println("1 - Kolay (3-5 saniye bekleme)");
		System.out.println("2 - Zor (1-3 saniye bekleme)");
		int secim = scanner.nextInt();
		scanner.nextLine(); 
		int minBekleme = (secim == 1) ? 3000 : 1000;
		int maxBekleme = (secim == 1) ? 5000 : 3000;

		long toplamSure = 0;
		int toplamSkor = 0;

		for (int tur = 1; tur <= 5; tur++) {
			System.out.println("\nTur " + tur + " başlıyor. Hazır olduğunda Enter'a bas...");
			scanner.nextLine();

			System.out.println("Bekle... ama erken basma!");
			int beklemeSuresi = minBekleme + random.nextInt(maxBekleme - minBekleme);
			long baslangic = System.currentTimeMillis();
			long gecen = 0;

			while (gecen < beklemeSuresi) {
				if (System.in.available() > 0) {
					scanner.nextLine(); 
					System.out.println("🚫 Çok erken bastın! Bu tur sayılmadı.");
					tur--; 
					Thread.sleep(1000);
					continue;
				}
				gecen = System.currentTimeMillis() - baslangic;
			}

			System.out.println("💥 ŞİMDİ! Enter'a hemen bas!");
			long start = System.currentTimeMillis();
			scanner.nextLine();
			long end = System.currentTimeMillis();

			long sure = end - start;
			toplamSure += sure;

			System.out.println("⏱ Reaksiyon Süren: " + sure + " ms");

			int skor;
			if (sure < 250)
				skor = 100;
			else if (sure < 500)
				skor = 75;
			else if (sure < 750)
				skor = 50;
			else
				skor = 25;

			toplamSkor += skor;
			System.out.println("⭐ Bu turdan kazandığın skor: " + skor);
		}

		long ortalama = toplamSure / 5;
		System.out.println("\n🎉 Oyun Bitti!");
		System.out.println("👤 Oyuncu: " + oyuncuAdi);
		System.out.println("📊 Ortalama Refleks Süresi: " + ortalama + " ms");
		System.out.println("💯 Toplam Skor: " + toplamSkor);

		
		FileWriter writer = new FileWriter("skorlar.txt", true);
		writer.write(oyuncuAdi + " - Ortalama: " + ortalama + " ms | Skor: " + toplamSkor + "\n");
		writer.close();

		System.out.println("\n📁 Skorun skorlar.txt dosyasına kaydedildi.");
		scanner.close();
	}
}
