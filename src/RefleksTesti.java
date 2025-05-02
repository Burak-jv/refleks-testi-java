import java.util.Scanner;
import java.util.Random;
import java.io.*;

public class RefleksTesti {
	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();

		System.out.println("Refleks Testi Oyununa Hoş Geldin!");
		System.out.print("Adını gir: ");
		String oyuncuAdi = scanner.nextLine();

		int secim = 0;
		while (secim != 1 && secim != 2) {
			try {
				System.out.println("Zorluk Seviyesi Seç:\n1 - Kolay (3-5 saniye)\n2 - Zor (1-3 saniye)");
				System.out.print("Seçimin: ");
				secim = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Geçersiz giriş! Lütfen 1 veya 2 gir.");
			}
		}

		int minBekleme = (secim == 1) ? 3000 : 1000;
		int maxBekleme = (secim == 1) ? 5000 : 3000;

		long toplamSure = 0;
		int toplamSkor = 0;

		for (int tur = 1; tur <= 5; tur++) {
			System.out.println("\nTur " + tur + " başlıyor. Hazır olduğunda Enter'a bas...");
			scanner.nextLine();

			System.out.println("Bekle...");
			int beklemeSuresi = minBekleme + random.nextInt(maxBekleme - minBekleme);
			long baslangic = System.currentTimeMillis();
			long gecen = 0;

			while (gecen < beklemeSuresi) {
				if (System.in.available() > 0) {
					scanner.nextLine();
					System.out.println("Çok erken bastın! Bu tur sayılmadı.");
					tur--;
					Thread.sleep(1000);
					continue;
				}
				gecen = System.currentTimeMillis() - baslangic;
			}

			System.out.println("ŞİMDİ! Enter'a hemen bas!");
			long start = System.currentTimeMillis();
			scanner.nextLine();
			long end = System.currentTimeMillis();

			long reaksiyonSuresi = end - start;
			toplamSure += reaksiyonSuresi;

			int skor;
			if (reaksiyonSuresi < 250)
				skor = 100;
			else if (reaksiyonSuresi < 500)
				skor = 75;
			else if (reaksiyonSuresi < 750)
				skor = 50;
			else
				skor = 25;

			toplamSkor += skor;

			System.out.println("Reaksiyon Süresi: " + reaksiyonSuresi + " ms");
			System.out.println("Skor: " + skor);
		}

		long ortalama = toplamSure / 5;

		System.out.println("\nOyun Bitti!");
		System.out.println("Oyuncu: " + oyuncuAdi);
		System.out.println("Ortalama Süre: " + ortalama + " ms");
		System.out.println("Toplam Skor: " + toplamSkor);

		int enYuksekSkor = okuEnYuksekSkoru();
		if (toplamSkor > enYuksekSkor) {
			System.out.println("TEBRİKLER! Yeni rekor senin!");
		} else {
			System.out.println("En yüksek skor: " + enYuksekSkor);
		}

		System.out.print("Geri Bildirim: ");
		if (ortalama < 250)
			System.out.println("İnsan değilsin, refleks canavarı!");
		else if (ortalama < 500)
			System.out.println("Gayet iyi, hızlı tepki verdin!");
		else if (ortalama < 750)
			System.out.println("Fena değil ama geliştirilebilir.");
		else
			System.out.println("Refleksin tembel, daha çok çalış!");

		FileWriter writer = new FileWriter("skorlar.txt", true);
		writer.write(oyuncuAdi + " - Ortalama: " + ortalama + " ms | Skor: " + toplamSkor + "\n");
		writer.close();

		System.out.println("Skorun skorlar.txt dosyasına kaydedildi.");
		scanner.close();
	}

	public static int okuEnYuksekSkoru() {
		int enYuksek = 0;
		try {
			File file = new File("skorlar.txt");
			if (!file.exists())
				return 0;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String satir;
			while ((satir = reader.readLine()) != null) {
				if (satir.contains("Skor:")) {
					String[] parcalar = satir.split("Skor: ");
					if (parcalar.length > 1) {
						int skor = Integer.parseInt(parcalar[1].trim());
						if (skor > enYuksek) {
							enYuksek = skor;
						}
					}
				}
			}
			reader.close();
		} catch (IOException | NumberFormatException e) {
			System.out.println("En yüksek skor okunamadı.");
		}
		return enYuksek;
	}
}
