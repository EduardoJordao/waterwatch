import java.sql.*;
import java.util.ArrayList;

public class WaterwatchReservatorio
{
   private int idReservatorio;
   private String nome;
   private String localizacao;
   private double volumeMax;
   private double vazao;
   private double areaDrenagem;
   private double espelhoAgua;
  
   public WaterwatchReservatorio(int idReservatorio)
   {
      setIdReservatorio(idReservatorio);
   }

   public void setIdReservatorio(int idReservatorio) { this.idReservatorio = idReservatorio; }
   public void setNome(String nome) { this.nome = nome; }
   public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
   public void setVolumeMax(double volumeMax) { this.volumeMax = volumeMax; }
   public void setVazao(double vazao) { this.vazao = vazao; }
   public void setAreaDrenagem(double areaDrenagem) { this.areaDrenagem = areaDrenagem; }
   public void setEspelhoAgua(double espelhoAgua) { this.espelhoAgua = espelhoAgua; }
 
   public int getIdReservatorio() { return idReservatorio; }
   public String getNome() { return nome; }
   public String getLocalizacao() { return localizacao; }
   public double getVolumeMax() { return volumeMax; }
   public double getVazao() { return vazao; }
   public double getAreaDrenagem() { return areaDrenagem; }
   public double getEspelhoAgua() { return espelhoAgua; }
   
   public static ArrayList<String> getNomes(Connection conn) throws ClassNotFoundException, SQLException
   {
      String sqlSelect = "SELECT Nome FROM Reservatorio ORDER BY idReservatorio ASC";
      ArrayList<String> listaReservatorios = new ArrayList<>();     
      try (PreparedStatement stm = conn.prepareStatement(sqlSelect);)
      {
         try (ResultSet rs = stm.executeQuery();)
         {      
            while (rs.next())
            {
               listaReservatorios.add(rs.getString(1));
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      catch (SQLException e1)
      {
         System.out.print(e1.getStackTrace());
      }
      return listaReservatorios;
   }   
   
   public void carregaAtributos(Connection conn)
   {
      String sqlSelect = "SELECT Nome, VolumeMax, Vazao, AreaDrenagem, EspelhoAgua, Localizacao FROM Reservatorio WHERE idReservatorio = ?";
           
      try (PreparedStatement stm = conn.prepareStatement(sqlSelect);)
      {
         stm.setInt(1, getIdReservatorio());
         try (ResultSet rs = stm.executeQuery();)
         {
                  
            if(rs.next())
            {
               this.setNome(rs.getString(1));
               this.setVolumeMax(rs.getDouble(2));
               this.setVazao(rs.getDouble(3));
               this.setAreaDrenagem(rs.getDouble(4));
               this.setEspelhoAgua(rs.getDouble(5));
               this.setLocalizacao(rs.getString(6));
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      catch (SQLException e1)
      {
         System.out.print(e1.getStackTrace());
      }
   }   
}