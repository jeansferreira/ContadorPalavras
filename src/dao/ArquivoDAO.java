package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import conexao.ConexaoMySQL;
import conexao.ConexaoSqlLite;
import pojo.ArquivoPojo;

public class ArquivoDAO {

	private Connection conn = null;

	public ArquivoDAO() {
		//conn = new ConexaoSqlLite().getConexao();
		conn = new ConexaoMySQL().getConexaoMySQL();
		// Criar Tabela
		//criarTabela();
	}

	private void criarTabela() {
		Statement stmt = null;

		String SQL = "CREATE TABLE IF NOT EXISTS Arquivos(id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL , nome VARCHAR, caminho VARCHAR);";

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();
			System.out.println("Tabela Arquivo criada!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// nome arquivo,posicao,linha
	public int adicionarLinha(ArquivoPojo arq) {
		
		int saida = 0;
		String SQL = "INSERT INTO Arquivos (nome,caminho) VALUES (?,?);";
		try {
			//System.out.println(SQL);
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, arq.getNome());
			stmt.setString(2, arq.getCaminho());
			
			saida = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saida;
	}

	public ArquivoPojo selecionarUmArquivo(int id) {

		ArquivoPojo po = null;
		
		try {
			String SQL = "SELECT id, nome, caminho FROM Arquivos WHERE id = ?;";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				po = new ArquivoPojo();
				po.setId(rs.getInt("id"));
				po.setNome(rs.getString("nome"));
				po.setCaminho(rs.getString("caminho"));
			}
			rs.close();
			stmt.close();
			return po;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return po;
	}

	public ArrayList<ArquivoPojo> selecionarTodos() {
		ArrayList<ArquivoPojo> lista = null;
		
		try {
			String SQL = "SELECT id, nome, caminho FROM Arquivos;";
			PreparedStatement stmt = conn.prepareStatement(SQL);

			ResultSet rs = stmt.executeQuery();
			lista = new ArrayList<ArquivoPojo>();
			
			while (rs.next()) {
				ArquivoPojo po = new ArquivoPojo();
				po.setId(rs.getInt("id"));
				po.setNome(rs.getString("nome"));
				po.setCaminho(rs.getString("caminho"));
				lista.add(po);
			}
			rs.close();
			stmt.close();

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
