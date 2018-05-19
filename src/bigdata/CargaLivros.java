package bigdata;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;

import dao.LivroDAO;
import pojo.LivroPojo;

public class CargaLivros implements Runnable {

	LivroDAO daoLivro;
	String caminhoArquivo;
	String nomeArquivo;
	String threadName;

	@Override
	public void run() {
		try {
			processar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CargaLivros(LivroDAO _daoLivro, String _caminhoArquivo, String _nomeArquivo) {
		this.daoLivro = _daoLivro;
		this.caminhoArquivo = _caminhoArquivo;
		this.nomeArquivo = _nomeArquivo;
	}

	public void processar() throws IOException {

		//FileReader fr;
		//BufferedReader fbread = null;

		String line = "";
		int posicao = 1;

		Scanner sc = null;
		try {
			
			System.out.println( "Livro: (" + nomeArquivo+ ") com (" + posicao +") linhas inseridas.");
			
			File source = new File(caminhoArquivo);
			sc = new Scanner(source);
			while (sc.hasNext()) {
				line = Normalizer.normalize(sc.nextLine(), Normalizer.Form.NFD);
				line = line.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
				line = line.replaceAll("[0-9.,-=?!'*#]", "");
				line = line.toUpperCase();

				LivroPojo linhaLivro = new LivroPojo();
				linhaLivro.setNome(nomeArquivo);
				linhaLivro.setPosicao(posicao);
				linhaLivro.setLinha(line);

				daoLivro.adicionarLinha(linhaLivro);
				posicao++;
				
				if (posicao % 100 == 0){
					System.out.println( "Livro: (" + nomeArquivo+ ") com (" + posicao +") linhas inseridas.");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//fbread.close();
	}
}
