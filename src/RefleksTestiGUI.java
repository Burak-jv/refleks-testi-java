import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class RefleksTestiGUI extends JFrame {
	private JLabel bilgiLabel, skorLabel, turLabel;
	private JButton baslaButon, tepkiButon;
	private long baslamaZamani;
	private boolean beklemeBitti = false;
	private int tur = 1;
	private int toplamSkor = 0;
	private long toplamSure = 0;
	private String oyuncuAdi;
	private Timer zamanlayici;
	private final int TUR_SAYISI = 5;

	public RefleksTestiGUI() {
		setTitle("Refleks Testi");
		setSize(500, 350);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);

		JPanel ustPanel = new JPanel();
		ustPanel.setBackground(Color.DARK_GRAY);
		bilgiLabel = new JLabel("Refleks Testine Hoþ Geldin!");
		bilgiLabel.setForeground(Color.WHITE);
		bilgiLabel.setFont(new Font("Arial", Font.BOLD, 18));
		ustPanel.add(bilgiLabel);

		JPanel ortaPanel = new JPanel();
		ortaPanel.setBackground(Color.DARK_GRAY);
		tepkiButon = new JButton("Hazýr Deðil");
		tepkiButon.setFont(new Font("Arial", Font.BOLD, 20));
		tepkiButon.setBackground(Color.GRAY);
		tepkiButon.setForeground(Color.BLACK);
		tepkiButon.setEnabled(false);
		ortaPanel.add(tepkiButon);

		JPanel altPanel = new JPanel();
		altPanel.setBackground(Color.DARK_GRAY);
		skorLabel = new JLabel("Skor: 0");
		skorLabel.setForeground(Color.GREEN);
		turLabel = new JLabel("Tur: 1 / " + TUR_SAYISI);
		turLabel.setForeground(Color.CYAN);
		baslaButon = new JButton("BAÞLA");
		baslaButon.setFont(new Font("Arial", Font.BOLD, 16));
		baslaButon.setBackground(Color.ORANGE);
		altPanel.add(skorLabel);
		altPanel.add(turLabel);
		altPanel.add(baslaButon);

		add(ustPanel, BorderLayout.NORTH);
		add(ortaPanel, BorderLayout.CENTER);
		add(altPanel, BorderLayout.SOUTH);

		oyuncuAdi = JOptionPane.showInputDialog(this, "Adýnýzý girin:");

		baslaButon.addActionListener(e -> yeniTur());
		tepkiButon.addActionListener(e -> kontrolEt());

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void yeniTur() {
		if (tur > TUR_SAYISI)
			return;
		beklemeBitti = false;
		tepkiButon.setEnabled(false);
		tepkiButon.setText("Hazýr Deðil");
		tepkiButon.setBackground(Color.GRAY);
		bilgiLabel.setText("Bekle...");
		int gecikme = new Random().nextInt(3000) + 2000;
		zamanlayici = new Timer(gecikme, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zamanlayici.stop();
				beklemeBitti = true;
				baslamaZamani = System.currentTimeMillis();
				tepkiButon.setEnabled(true);
				tepkiButon.setText("ÞÝMDÝ TIKLA!");
				tepkiButon.setBackground(Color.RED);
				bilgiLabel.setText("Týkla!");
			}
		});
		zamanlayici.setRepeats(false);
		zamanlayici.start();
	}

	private void kontrolEt() {
		if (!beklemeBitti) {
			bilgiLabel.setText("Çok erken bastýn! Tekrar dene.");
			return;
		}
		long sure = System.currentTimeMillis() - baslamaZamani;
		toplamSure += sure;
		int skor = sure < 250 ? 100 : sure < 500 ? 75 : sure < 750 ? 50 : 25;
		toplamSkor += skor;
		skorLabel.setText("Skor: " + toplamSkor);
		bilgiLabel.setText("Süre: " + sure + " ms | Skor: " + skor);
		tepkiButon.setEnabled(false);
		tepkiButon.setText("Hazýr Deðil");
		tepkiButon.setBackground(Color.GRAY);
		tur++;
		turLabel.setText("Tur: " + (tur > TUR_SAYISI ? TUR_SAYISI : tur) + " / " + TUR_SAYISI);

		if (tur > TUR_SAYISI) {
			long ortalama = toplamSure / TUR_SAYISI;
			int enYuksek = okuEnYuksekSkor();
			if (toplamSkor > enYuksek)
				bilgiLabel.setText("Yeni rekor! Skor: " + toplamSkor);
			else
				bilgiLabel.setText("Oyun Bitti! Skor: " + toplamSkor);
			skorLabel.setText("Ortalama: " + ortalama + " ms");
			geriBildirim(ortalama);
			skorKaydet(ortalama);
			baslaButon.setEnabled(false);
		}
	}

	private void skorKaydet(long ortalama) {
		try {
			FileWriter fw = new FileWriter("skorlar.txt", true);
			fw.write(oyuncuAdi + " - Ortalama: " + ortalama + " ms | Skor: " + toplamSkor + "\n");
			fw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Skor kaydedilemedi.");
		}
	}

	private int okuEnYuksekSkor() {
		int enYuksek = 0;
		try {
			File f = new File("skorlar.txt");
			if (!f.exists())
				return 0;
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("Skor:")) {
					String[] parts = line.split("Skor:");
					if (parts.length > 1) {
						int s = Integer.parseInt(parts[1].trim());
						if (s > enYuksek)
							enYuksek = s;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			return 0;
		}
		return enYuksek;
	}

	private void geriBildirim(long ort) {
		String mesaj = ort < 250 ? "Refleks canavarýsýn!"
				: ort < 500 ? "Gayet iyi!" : ort < 750 ? "Ýyi ama geliþtirilebilir." : "Daha hýzlý olabilirsin.";
		JOptionPane.showMessageDialog(this, mesaj, "Geri Bildirim", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new RefleksTestiGUI());
	}
}
