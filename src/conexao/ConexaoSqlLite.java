package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoSqlLite {
	Connection conn = null;

	public Connection getConexao() {
		try {
			//conn = DriverManager.getConnection("jdbc:sqlite:data.sqlite");
			conn = DriverManager.getConnection("jdbc:sqlite:bigdata.db");
			if (conn != null) {
				System.out.println("Conexão com o Banco de Dados realizada!");
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return conn;
	}
}
