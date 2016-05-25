import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WaterwatchVolume 
{
   private int idPluviometriaDiaria;
   private int idReservatorio;
   private Date data;
   private double volumeDiario;
   private double quantidadeChuva;

   public WaterwatchVolume(int idPluviometriaDiaria, int idReservatorio, Date data, double volumeDiario, double quantidadeChuva)
   {
      setIdPluviometriaDiaria(idPluviometriaDiaria);
      setIdReservatorio(idReservatorio);
      setData(data);
      setVolumeDiario(volumeDiario);
      setQuantidadeChuva(quantidadeChuva);
   }
   public WaterwatchVolume(int idReservatorio) 
   { 
      setIdReservatorio(idReservatorio); 
   }
   public WaterwatchVolume(int idReservatorio, Date data)
   {
      setIdReservatorio(idReservatorio);
      setData(data);
   }
   
   public void setIdPluviometriaDiaria(int idPluviometriaDiaria) { this.idPluviometriaDiaria = idPluviometriaDiaria; }
   public void setIdReservatorio(int idReservatorio) { this.idReservatorio = idReservatorio; }
   public void setData(Date data) { this.data = data; }
   public void setVolumeDiario(double volumeDiario) { this.volumeDiario = volumeDiario; }
   public void setQuantidadeChuva(double quantidadeChuva) { this.quantidadeChuva = quantidadeChuva; }
    
   public int getIdPluviometriaDiaria() { return idPluviometriaDiaria; }
   public int getIdReservatorio() { return idReservatorio; }   
   public Date getData() { return data; }
   public double getVolumeDiario() { return volumeDiario; }
   public double getQuantidadeChuva() { return quantidadeChuva; }
   
   public static ArrayList<WaterwatchVolume> getEntradas(Connection conn, int idReservatorio) throws ClassNotFoundException, SQLException 
   {
      String sqlSelect = "SELECT idPluviometriaDiaria, Data, VolumeDiario, QuantidadeChuva FROM PluviometriaDiaria WHERE idReservatorio = ?";
      PreparedStatement stm = conn.prepareStatement(sqlSelect);
      stm.setInt(1, idReservatorio);
      ResultSet rs = stm.executeQuery();
      ArrayList<WaterwatchVolume> listaEntradas = new ArrayList<>();
      while (rs.next()) 
      {
         WaterwatchVolume entrada = new WaterwatchVolume(rs.getInt(1), idReservatorio, 
                                    rs.getDate(2), rs.getDouble(3), rs.getDouble(4));
         listaEntradas.add(entrada);
      }
      return listaEntradas;
   }
   
   public static ArrayList<WaterwatchVolume> getEntradasFiltroData(Connection conn, int idReservatorio, Date de, Date ate) throws ClassNotFoundException, SQLException 
   {
      String sqlSelect = "SELECT idPluviometriaDiaria, Data, VolumeDiario, QuantidadeChuva FROM PluviometriaDiaria WHERE idReservatorio = ? AND Data BETWEEN ? AND ?";
      java.sql.Date dataDe = new java.sql.Date(de.getTime());
      java.sql.Date dataAte = new java.sql.Date(ate.getTime());
      PreparedStatement stm = conn.prepareStatement(sqlSelect);
      stm.setInt(1, idReservatorio);
      stm.setDate(2, dataDe);
      stm.setDate(3, dataAte);
      ResultSet rs = stm.executeQuery();
      ArrayList<WaterwatchVolume> listaEntradas = new ArrayList<>();
      while (rs.next()) 
      {
         WaterwatchVolume entrada = new WaterwatchVolume(rs.getInt(1), idReservatorio, 
                                    rs.getDate(2), rs.getDouble(3), rs.getDouble(4));
         listaEntradas.add(entrada);
      }
      return listaEntradas;
   }
   
   public void incluirVolume(Connection conn)
   {
      String sqlInsert = 
         "INSERT INTO PluviometriaDiaria(idPluviometriaDiaria, idReservatorio, Data, VolumeDiario, QuantidadeChuva) VALUES (?, ?, ?, ?, ?)";
      try(PreparedStatement stm = conn.prepareStatement(sqlInsert);)
      {
         stm.setInt(1, getIdPluviometriaDiaria());
         stm.setInt(2, getIdReservatorio());
         stm.setDate(3, new java.sql.Date(getData().getTime()));
         stm.setDouble(4, getVolumeDiario());
         stm.setDouble(5, getQuantidadeChuva());
         stm.execute();
      }
      catch(Exception e)
      {  
         e.printStackTrace();
         try
         {
            conn.rollback();
         }
         catch (SQLException el)
         {
            System.out.println(el.getStackTrace());
         }
      
      }
   } 
   
   public boolean checarId(Connection conn)
   {
      String sqlSelect = "SELECT * FROM PluviometriaDiaria WHERE Data = ?";
      boolean b = false;     
      try (PreparedStatement stm = conn.prepareStatement(sqlSelect);)
      {
         stm.setDate(1, new java.sql.Date(getData().getTime()));
         try (ResultSet rs = stm.executeQuery();)
         {
                  
            if(rs.next())
            {
               b = true;
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
      return b; 
   }  

   public void excluir(Connection conn)
   {
      String sqlDelete = "DELETE FROM PluviometriaDiaria WHERE Data = ?";
      try (PreparedStatement stm = conn.prepareStatement (sqlDelete);)
      {
         stm.setDate(1, new java.sql.Date(getData().getTime()));
         stm.execute();
      }
      catch (Exception e ){
         e.printStackTrace();
         try 
         {
            conn.rollback();
         }
         catch (SQLException e1){
            System.out.println(e1.getStackTrace());
         }
      }
   }
}