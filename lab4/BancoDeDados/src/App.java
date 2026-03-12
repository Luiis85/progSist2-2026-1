import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("--- INICIANDO O SISTEMA ---");

        System.out.println("\n[+] Inserindo dados...");
        createAluno("Luis", "Sistemas de Informação");
        createMatricula(1, "2026.1"); // Assumindo que o Luis será o ID 1
        createMatricula(1, "2026.2");

        System.out.println("\n[+] Lendo o banco de dados...");
        read();

        System.out.println("\n[+] Atualizando curso do aluno ID 1...");
        updateCurso(1, "Engenharia de Software");
        read(); 

    }

    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:6543/postgres?prepareThreshold=0";
        String username = "postgres.myiewonecgdjqlqnqxfh";
        String password = "lo05.patrocinio";
        
        return DriverManager.getConnection(url, username, password);
    }


    public static void createAluno(String nome, String curso) throws Exception {
        String sql = "INSERT INTO Aluno (nome, curso) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, nome);
            pstmt.setString(2, curso);
            pstmt.executeUpdate();
            System.out.println("Aluno " + nome + " inserido com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void createMatricula(int alunoId, String semestre) throws Exception {
        String sql = "INSERT INTO Matricula (aluno_id, semestre) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, alunoId);
            pstmt.setString(2, semestre);
            pstmt.executeUpdate();
            System.out.println("Matrícula " + semestre + " vinculada ao aluno ID " + alunoId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void read() throws Exception {
        // Fazendo um JOIN para trazer o aluno e a matrícula na mesma consulta
        String sql = "SELECT a.id, a.nome, a.curso, m.semestre " +
                     "FROM Aluno a LEFT JOIN Matricula m ON a.id = m.aluno_id " +
                     "ORDER BY a.id";
                     
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String curso = rs.getString("curso");
                String semestre = rs.getString("semestre");
                
                String sem = (semestre != null) ? semestre : "Sem matrícula";
                System.out.println("ID: " + id + " | Aluno: " + nome + " | Curso: " + curso + " | Semestre: " + sem);
            }
            System.out.println("--------------------------------------------------");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateCurso(int alunoId, String novoCurso) throws Exception {
        String sql = "UPDATE Aluno SET curso = ? WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, novoCurso);
            pstmt.setInt(2, alunoId);
            pstmt.executeUpdate();
            System.out.println("Curso atualizado com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteAluno(int alunoId) throws Exception {
        String sql = "DELETE FROM Aluno WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, alunoId);
            pstmt.executeUpdate();
            System.out.println("Aluno deletado com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}