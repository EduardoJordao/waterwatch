import java.sql.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WaterwatchConexao
{
   static
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver");
         
      }
      catch(ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public static Connection conectar() throws SQLException, IOException, FileNotFoundException
   {
      String pFile = "config.properties";
      InputStream is = WaterwatchConexao.class.getClassLoader().getResourceAsStream(pFile);
      Properties p = new Properties();
      
      if (is != null) 
      {
         p.load(is);
      } 
      else 
      {
         throw new FileNotFoundException();
      }
      String servidor = p.getProperty("servidor");
      String porta = p.getProperty("porta");
      String database = p.getProperty("database");
      String usuario = p.getProperty("usuario");
      String senha = p.getProperty("senha");
      is.close();
   
      return DriverManager.getConnection("jdbc:mysql://"+servidor+":"+porta+"/"+database+"?user="+usuario+"&password="+senha);    
   }
}