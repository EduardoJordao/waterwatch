import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

public class WaterwatchLocais
{
   private int idMunicipio, qHabitantes, idReservatorio, qResidencias;
   private String nome;
   private double areaTotal, areaAbastecida;
   
   public WaterwatchLocais(int idReservatorio)
   {
      this.idReservatorio = idReservatorio;
   }
   
   public WaterwatchLocais(int idMunicipio, int qHabitantes, int idReservatorio,
                           int qResidencias, String nome, double areaTotal,
                           double areaAbastecida)
   {
      this.idMunicipio = idMunicipio;
      this.qHabitantes = qHabitantes;
      this.idReservatorio = idReservatorio;
      this.qResidencias = qResidencias;
      this.nome = nome;
      this.areaTotal = areaTotal;
      this.areaAbastecida = areaAbastecida;  
   }
   
   public String getNome() { return nome; }
   public int getQResidencias() { return qResidencias; }
   public int getQHabitantes() { return qHabitantes; }
   public double getAreaAbastecida() { return areaAbastecida; }
   public double getAreaTotal() { return areaTotal; }
   
   public static ArrayList<WaterwatchLocais> getEntradas(Connection conn, int idReservatorio) throws ClassNotFoundException, SQLException 
   {
      String sqlSelect = "SELECT Municipio.idMunicipio,Municipio.QuantidadeHabitantes,Abastecimento.ResidenciasAbastecidas,"
                        +"Municipio.Nome,Municipio.AreaKmQuad,Abastecimento.AreaAbastecidaKmQuad "
                        +"FROM Reservatorio INNER JOIN Abastecimento ON Reservatorio.idReservatorio = Abastecimento.idReservatorio "
                        +"INNER JOIN Municipio ON Municipio.idMunicipio = Abastecimento.idMunicipio " 
                        +"WHERE Reservatorio.idReservatorio = ? "
                        +"ORDER BY Municipio.Nome ASC";
      PreparedStatement stm = conn.prepareStatement(sqlSelect);
      stm.setInt(1, idReservatorio);
      ResultSet rs = stm.executeQuery();
      ArrayList<WaterwatchLocais> listaEntradas = new ArrayList<>();
      while (rs.next()) 
      {
         WaterwatchLocais entrada = new WaterwatchLocais(rs.getInt(1), rs.getInt(2), 
                                    idReservatorio, rs.getInt(3), rs.getString(4),
                                    rs.getDouble(5), rs.getDouble(6));
         listaEntradas.add(entrada);
      }
      return listaEntradas;
   }
}