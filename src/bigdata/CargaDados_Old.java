package bigdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;

import dao.ArquivoDAO;
import dao.LivroDAO;
import pojo.ArquivoPojo;
import pojo.LivroPojo;

public class CargaDados_Old {

	public static void main(String[] args) {

		executarCarga();
	}

	public static void executarCarga() {
		LivroDAO daoLivro = new LivroDAO();
		ArquivoDAO daoArquivo = new ArquivoDAO();
		long tempoInicio = 0;
		long tempoFim = 0;

		tempoInicio = System.nanoTime();
		
		// "2city10.txt","1215","FROM EVERY WINDOW FLUTTERED IN EVERY VESTIGE OF
		// A GARMENT THAT THE"
/*		ArrayList<LivroPojo> lista1 = daoLivro.selecionarPorLivro("2city10.txt");

		for (Iterator iterator = lista1.iterator(); iterator.hasNext();) {
			LivroPojo livroPojo = (LivroPojo) iterator.next();
			System.out.println(livroPojo.getPosicao() + "/" + lista1.size() + " - " + livroPojo.getLinha());
		}*/
		
		File file = new File("./Livro2");
		File afile[] = file.listFiles();

		System.out.println("--------- Carga dos Arquivo ----------------");
		for (int i = 0; i < afile.length; i++) {
			File fl = afile[i];

			ArquivoPojo pojo = new ArquivoPojo();
			pojo.setNome(fl.getName());
			pojo.setCaminho(fl.getAbsolutePath());
			daoArquivo.adicionarLinha(pojo);
			System.out.println("Arquivo: "+pojo.getCaminho());
		}

		System.out.println("--------- Carga dos Livros -----------------");

		ArrayList<ArquivoPojo> lista = daoArquivo.selecionarTodos();

		// Loop dos Arquivos e Linhas
		for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			ArquivoPojo arquivoPojo = (ArquivoPojo) iterator.next();
			System.out.println(arquivoPojo.getCaminho());

			FileReader fr;
			BufferedReader fbread;

			try {
				fr = new FileReader(arquivoPojo.getCaminho());
				fbread = new BufferedReader(fr);

				String line = "";
				int posicao = 1;

				while ((line = fbread.readLine()) != null) {

					line = Normalizer.normalize(line, Normalizer.Form.NFD);
					line = line.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
					line = line.replaceAll("[0-9.,-=?!'*#]", "");
					line = line.toUpperCase();

					LivroPojo linhaLivro = new LivroPojo();
					linhaLivro.setNome(arquivoPojo.getNome());
					linhaLivro.setPosicao(posicao);
					linhaLivro.setLinha(line);

					daoLivro.adicionarLinha(linhaLivro);
					posicao++;

					//System.out.println(arquivoPojo.getNome() + " > Total Linhas: " + posicao);

					if (posicao%100 == 0){
						System.out.println( arquivoPojo.getNome()+ " > Total Linhas: " + posicao);
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		tempoFim = System.nanoTime();
		int seconds = (int) ((tempoFim / tempoInicio) / 1000) % 60 ;
		int minutes = (int) ((seconds / (1000*60)) % 60);
		
		System.out.println("Tempo Total: "+ minutes+":"+seconds);
		
		daoArquivo.fecharConexao();
	}

}
