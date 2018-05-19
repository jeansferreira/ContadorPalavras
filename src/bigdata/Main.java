package bigdata;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import conexao.ConexaoMySQL;
import dao.ArquivoDAO;
import dao.LivroDAO;
import pojo.ArquivoPojo;

public class Main {

	static LivroDAO daoLivro;
	static ArquivoDAO daoArquivo;

	public static void main(String[] args) {
		
		//Connection conn = null;
		//conn = new ConexaoMySQL().getConexaoMySQL();
			
		long tempoInicio = 0;
		long tempoFim = 0;

		tempoInicio = System.nanoTime();

		daoLivro = new LivroDAO();
		daoArquivo = new ArquivoDAO();

		/**
		 * Informar quantas threads deseja simultaneamente.
		 */
		int threads = 16; // 4 CPU's
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(threads);

		File file = new File("./Livros");
		File afile[] = file.listFiles();

		System.out.println("--------- Carga dos Livros ----------------");
		for (int i = 0; i < afile.length; i++) {
			File fl = afile[i];

			ArquivoPojo pojo = new ArquivoPojo();
			pojo.setNome(fl.getName());
			pojo.setCaminho(fl.getAbsolutePath());
			daoArquivo.adicionarLinha(pojo);
			System.out.println("Carregando inf. do Livro: " + pojo.getCaminho());
		}
		System.out.println();
		System.out.println("--------- Carga das Linhas -----------------");

		ArrayList<ArquivoPojo> lista = daoArquivo.selecionarTodos();

		for (Iterator<ArquivoPojo> iterator = lista.iterator(); iterator.hasNext();) {
			ArquivoPojo arquivoPojo = (ArquivoPojo) iterator.next();
			executor.execute(new CargaLivros(daoLivro, arquivoPojo.getCaminho(), arquivoPojo.getNome()));
		}

		tempoFim = System.nanoTime();
		int seconds = (int) ((tempoFim / tempoInicio) / 1000) % 60;
		int minutes = (int) ((seconds / (1000 * 60)) % 60);

		// System.out.println("Tempo Total: "+ minutes+":"+seconds);

	}
}
