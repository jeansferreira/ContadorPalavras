package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import conexao.ConexaoMySQL;
import conexao.ConexaoSqlLite;
import pojo.LivroPojo;

public class LivroDAO {

	private Connection conn = null;

	public LivroDAO() {
		//conn = new ConexaoSqlLite().getConexao();
		conn = new ConexaoMySQL().getConexaoMySQL();
		// Criar Tabela
		//criarTabela();
	}

	private void criarTabela() {
		Statement stmt = null;
		
		String SQL = "CREATE TABLE IF NOT EXISTS Livros(nome VARCHAR, posicao integer, linha VARCHAR, PRIMARY KEY (nome, posicao));";
		//String SQL = "CREATE TABLE IF NOT EXISTS Livros(nome VARCHAR, posicao integer, linha VARCHAR);";
		
		try {
			//conn = DriverManager.getConnection("jdbc:sqlite:livro.db");
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();

			System.out.println("Tabela Livro criada!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// nome arquivo,posicao,linha
	public int adicionarLinha(LivroPojo liv) {
		
		int saida = 0;
		String SQL = "INSERT INTO Livros (nome,posicao,linha) VALUES (?,?,?);";
		
		try {
			//System.out.println(SQL);
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, liv.getNome());
			stmt.setLong(2, liv.getPosicao());
			stmt.setString(3, liv.getLinha());

			saida = stmt.executeUpdate();
			stmt.close();
			
			return saida;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return saida;	
	}

	public LivroPojo selecionarUmaLinha(String nomeLivro, long posicaoLinha) {

		LivroPojo po = null;
		
		try {
			String SQL = "SELECT nome, posicao, linha FROM Livros WHERE nome = ? and posicao = ?;";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, nomeLivro);
			stmt.setLong(2, posicaoLinha);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				po = new LivroPojo();
				po.setNome(rs.getString("nome"));
				po.setPosicao(rs.getInt("posicao"));
				po.setLinha(rs.getString("linha"));
			}
			rs.close();
			stmt.close();
			return po;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return po;
	}

	public ArrayList<LivroPojo> selecionarTodos() {
		
		ArrayList<LivroPojo> lista = null;
		
		try {
			String SQL = "SELECT nome, posicao, linha FROM Livros;";
			PreparedStatement stmt = conn.prepareStatement(SQL);

			ResultSet rs = stmt.executeQuery();
			lista = new ArrayList<LivroPojo>();
			
			while (rs.next()) {
				LivroPojo po = new LivroPojo();
				po.setNome(rs.getString("nome"));
				po.setPosicao(rs.getInt("posicao"));
				po.setLinha(rs.getString("linha"));
				lista.add(po);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lista;
	}
	
	public ArrayList<LivroPojo> selecionarPorLivro(String nomeLivro) {

		LivroPojo po = null;
		ArrayList<LivroPojo> lista = null;
		
		try {
			String SQL = "SELECT nome, posicao, linha FROM Livros WHERE nome = ?;";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, nomeLivro);
			
			ResultSet rs = stmt.executeQuery();
			lista = new ArrayList<LivroPojo>();
			
			while (rs.next()) {
				po = new LivroPojo();
				po.setNome(rs.getString("nome"));
				po.setPosicao(rs.getInt("posicao"));
				po.setLinha(rs.getString("linha"));
				
				lista.add(po);
			}
			rs.close();
			stmt.close();
			return lista;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lista;
	}

	public void fecharConexao() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
