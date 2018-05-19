package bigdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class ScannerFile {

	public static void main(String[] args) throws Exception {
		ScannerFile();
	}

	private static void ScannerFile() {
		File source = new File("C:\\Users\\JeanCarlos\\workspace\\Palavra\\Livro2\\2city10.txt");
		File destination = new File("C:\\Users\\JeanCarlos\\workspace\\Palavra\\Livro2\\arq2.txt");
		if (!destination.exists()) {
			try {
				destination.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Writer w = null;
		try {
			w = new FileWriter(destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter writer = new BufferedWriter(w);
		Scanner sc = null;
		try {
			sc = new Scanner(source);
			while (sc.hasNext()) {
				writer.write(sc.nextLine().replaceAll("\\s+", ";"));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
