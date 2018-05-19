package bigdata;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MainSala {
	//Contar a quantidade de palavras por Livro
	//Ordenar a quantidade de Palavras por Livro
	//Mostrar a 
	public static void main(String[] args) {
		HashMap<String, Integer> hashPalavras = new HashMap<>();
		HashMap<String, Integer> hashPalavrasLivro = new HashMap<>();

		long t1 = 0;
		long t2 = 0;
		long t3 = 0;

		long tempoInicio = 0;
		long tempoFim = 0;
		long t5 = 0;

		File file = new File("./Livro2");
		File afile[] = file.listFiles();

		int totalArq = afile.length;

		tempoInicio = System.nanoTime();

		for (int j = 0; j < afile.length; j++) {
			File arquivos = afile[j];
			// System.out.println(arquivos.getName());

			try {
				FileReader fr = new FileReader(arquivos.getAbsolutePath());
				BufferedReader bfr = new BufferedReader(fr);

				String line = "";

				t1 = System.nanoTime();

				while ((line = bfr.readLine()) != null) {
					line = Normalizer.normalize(line, Normalizer.Form.NFD);
					line = line.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
					line = line.replaceAll("[0-9.,-=?!'*#]", "");
					line = line.toUpperCase();
					String str[] = line.split(" ");
					for (int i = 0; i < str.length; i++) {

						if (str[i].length() > 0 && str[i].length() < 30) {
							if (hashPalavras.containsKey(str[i])) {
								String strTratada = str[i];

								int valor = hashPalavras.get(strTratada);
								valor++;
								hashPalavras.put(strTratada, valor);
								
								if (hashPalavrasLivro.containsKey(str[i] + "#" + arquivos.getName())) {
									String strTratadaLivro = str[i] + "#" + arquivos.getName();

									int valorLivro = hashPalavrasLivro.get(strTratadaLivro);
									valorLivro++;
									hashPalavrasLivro.put(strTratadaLivro, valorLivro);
								} else {
									hashPalavrasLivro.put(str[i] + "#" + arquivos.getName(), 1);
								}								
							} else {
								hashPalavras.put(str[i], 1);
								hashPalavrasLivro.put(str[i] + "#" + arquivos.getName(), 1);
							}							
						}
					}
				}

				t2 = System.nanoTime();

				bfr.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(j + " De " + totalArq + " - Livro: " + arquivos.getAbsolutePath());
			System.out.println("Time Processing(Book): " + ((t2 - t1) / 1000000.0));
			System.out.println();
		}

		t5 = System.nanoTime();

		// Lista Palavras todos os livros	
		LinkedList<ChaveValor> listaPalavras = new LinkedList<>();

		for (Iterator iterator = hashPalavras.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			// System.out.println(""+key+" "+hashPalavras.get(key));
			listaPalavras.add(new ChaveValor(key, hashPalavras.get(key)));
		}

		Collections.sort(listaPalavras, new Comparator<ChaveValor>() {
			@Override
			public int compare(ChaveValor o1, ChaveValor o2) {
				return o1.valor > o2.valor ? -1 : (o1.valor < o2.valor ? 1 : 0);
			}
		});

		t3 = System.nanoTime();

		// Lista de Palavras Por Livro
		LinkedList<ChaveValor> listaPalavrasLivro = new LinkedList<>();

		for (Iterator iterator = hashPalavrasLivro.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			// System.out.println(""+key+" "+hashPalavras.get(key));
			listaPalavrasLivro.add(new ChaveValor(key, hashPalavrasLivro.get(key)));
		}

		/*Collections.sort(listaPalavrasLivro, new Comparator<ChaveValorLivro>() {
			@Override
			public int compare(ChaveValorLivro o1, ChaveValorLivro o2) {
				return o1.valor > o2.valor ? -1 : (o1.valor < o2.valor ? 1 : 0);
			}
		});*/
		
		for (Iterator iterator = listaPalavrasLivro.iterator(); iterator.hasNext();) {
			ChaveValor chaveValor = (ChaveValor) iterator.next();
			System.out.println(chaveValor.key+">"+chaveValor.valor);
		}
		
		try {
			FileWriter fr = new FileWriter("OUTPUT.csv");
			for (Iterator iterator = listaPalavras.iterator(); iterator.hasNext();) {
				ChaveValor chaveValor = (ChaveValor) iterator.next();
				// System.out.println(chaveValor.key+" "+chaveValor.valor);
				fr.write(chaveValor.key + ";" + chaveValor.valor + "\n");
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tempoFim = System.nanoTime();

		System.out.println("Time Processing (Books): " + ((t5 - tempoInicio) / 1000000.0));
		System.out.println("Time Sort Full: " + ((t3 - t5) / 1000000.0));
		System.out.println();

		System.out.println("NPALAVRAS Full: " + listaPalavras.size());
		System.out.println("Time Full(mm): " + ((tempoFim - tempoInicio) / 100000.0));
		System.out.println("Time Full(seg): " + ((tempoFim - tempoInicio) / 100000.0) / 60);
	}
}

class ChaveValor {
	String key;
	int valor;

	public ChaveValor(String key, int valor) {
		super();
		this.key = key;
		this.valor = valor;
	}
}
