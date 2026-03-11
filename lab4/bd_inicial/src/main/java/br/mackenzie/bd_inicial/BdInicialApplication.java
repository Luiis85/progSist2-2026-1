package br.mackenzie.bd_inicial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BdInicialApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(BdInicialApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			String createTable = "CREATE TABLE IF NOT EXISTS Pacientes(id number(2), nome VARCHAR(256) NOT NULL UNIQUE);";
			String databaseURL = "file:./data/banco_dados;";
			try {
				Connection conn = DriverManager.getConnection(
					"jdbc:h2:" + databaseURL + "INIT=" + createTable,
					"admin", "admin");
				
				System.out.println("executando read");
				read(conn);
				System.out.println("executando insert");
				inserir(conn);
				System.out.println("executando read");
				read(conn);

				
			} catch(Exception e) {
				e.printStackTrace();
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void read(Connection con)throws Exception{
		String sql = "select * from pacientes";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.out.println("nome do paciente: "+rs.getString("nome"));
		}

	}

	private void inserir(Connection con)throws Exception{
		String sql = "insert into pacientes (id, nome)values(?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1,1);
		pstmt.setString(2,"Ronaldo");
		int linhas = pstmt.executeUpdate();
		if(linhas >0)
			System.out.println("inserido com sucesso");

	}

}
