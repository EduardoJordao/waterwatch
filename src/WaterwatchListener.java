import java.sql.SQLException;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.lang.String;
import javax.swing.RowFilter;

public class WaterwatchListener implements ActionListener
{
   private final WaterwatchGUI w;
   SimpleDateFormat parseData = new SimpleDateFormat("dd/MM/yyyy");
   
   public WaterwatchListener(WaterwatchGUI w)
   {
      this.w = w;
   }
   
   @Override 
   public void actionPerformed(ActionEvent e) 
   {
      int pos;
      if (e.getSource() == w.cb)
      {  
         pos = w.cb.getSelectedIndex();
         WaterwatchReservatorio r = new WaterwatchReservatorio(pos);
         r.carregaAtributos(w.conn);         
         w.tfVol.setText(String.format("%.000f", r.getVolumeMax())+" m³");
         w.tfVaz.setText(r.getVazao()+" m³/s");
         w.tfDrn.setText(r.getAreaDrenagem()+" km²");
         w.tfEsp.setText(r.getEspelhoAgua()+" km²");
         
         ArrayList<WaterwatchVolume> entradas = null; 
         try 
         {
            entradas = WaterwatchVolume.getEntradas(w.conn, pos);
         } 
         catch (Exception e2) 
         {   
            e2.printStackTrace();
         }
         w.pesquisa1.setRowFilter(null);
         w.tmVol.setRowCount(0);
         for (WaterwatchVolume entrada : entradas) 
         {
            String volmc = String.format("%.000f", r.getVolumeMax() * (entrada.getVolumeDiario() / 100));
            Object rowData[] = {parseData.format(entrada.getData()), volmc +" m³",
                                entrada.getVolumeDiario() +" %", entrada.getQuantidadeChuva() +" mm"};
            w.tmVol.addRow(rowData);
         }
         w.tmVol.fireTableDataChanged();
         w.tbVol.repaint();
      }
      
      if (e.getSource() == w.cb2)
      {
         pos = w.cb2.getSelectedIndex();
         WaterwatchReservatorio r2 = new WaterwatchReservatorio(pos);
         r2.carregaAtributos(w.conn);         
         w.tfVol2.setText(String.format("%.000f", r2.getVolumeMax())+" m³");
         w.tfLoc.setText(r2.getLocalizacao()+"");
         
         ArrayList<WaterwatchLocais> entradas2 = null; 
         try 
         {
            entradas2 = WaterwatchLocais.getEntradas(w.conn, pos);
         } 
         catch (Exception e2) 
         {   
            e2.printStackTrace();
         }
         w.pesquisa2.setRowFilter(null);
         w.tmLoc.setRowCount(0);
         for (WaterwatchLocais entrada : entradas2) 
         {
            Object rowData[] = {entrada.getNome(), entrada.getQResidencias(), entrada.getQHabitantes(),
                                entrada.getAreaAbastecida() +" m²", entrada.getAreaTotal() +" m²"};
            w.tmLoc.addRow(rowData);
         }
         w.tmLoc.fireTableDataChanged();
         w.tbLoc.repaint();
      }
       
      if (e.getSource() == w.bNova) 
      {
         if(w.cb.getSelectedIndex() == -1)
         {
            w.erroNull(w);
            return;
         }
         w.dlNovaEntrada(w);
      }
      
      if (e.getSource() == w.bConfirma)
      {
         pos = w.cb.getSelectedIndex();
         int idEntrada = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(w.spData.getValue()) +"0" +pos);
         Date data = (Date)w.spData.getValue();
         double Valor, Chuva;
         String tipo;
         boolean isPercentage;
         if (w.rbPct.isSelected()) { tipo = "%"; isPercentage = true;  }
         else { tipo = "m³"; isPercentage = false; }
         
         try
         {
            
            if(w.tfVal.getText().trim().isEmpty()) 
            {
               throw new NumberFormatException("Campo esta vazio.");
            }
            Valor = Double.parseDouble(w.tfVal.getText());
            
            if (isPercentage == false)
            {
               WaterwatchReservatorio r3 = new WaterwatchReservatorio(pos);
               r3.carregaAtributos(w.conn);
               double max = r3.getVolumeMax();
               Valor = (Valor*100)/max;
            }
            
         }
         catch(NumberFormatException ex)
         {
            String campo = "Volume";
            w.erroCampo(w.dlNova, campo);
            return;
         }
         
         try
         {
            if(w.tfChv.getText().trim().isEmpty()) 
            {
               throw new NumberFormatException("Campo esta vazio.");
            }
            Chuva = Double.parseDouble(w.tfChv.getText());
         }
         catch(NumberFormatException ex)
         {
            String campo = "Pluviometria";
            w.erroCampo(w.dlNova, campo);
            return;
         }
         String campos = "Data: " +parseData.format(w.spData.getValue())
                         +"\nVolume: " +Valor +" " +tipo +"\nPluviometria: " +Chuva;
         boolean s = w.dlConfirma(w.dlNova, campos);
         
         if (s == true)
         {
            WaterwatchVolume v = new WaterwatchVolume(idEntrada, pos, data, Valor, Chuva);
            boolean b = v.checarId(w.conn);
            if (b == true)
            {
               w.erroDuplicate(w);
               return;
            }
            v.incluirVolume(w.conn);
         }
         else 
         { 
            return; 
         }
      }
      if (e.getSource() == w.bFiltrar)
      {
         Date dataDe = (Date)w.spDe.getValue();
         Date dataAte = (Date)w.spAte.getValue();
         pos = w.cb.getSelectedIndex();
         WaterwatchReservatorio r = new WaterwatchReservatorio(pos);
         r.carregaAtributos(w.conn);
         ArrayList<WaterwatchVolume> entradas = null; 
         try 
         {
            entradas = WaterwatchVolume.getEntradasFiltroData(w.conn, pos, dataDe, dataAte);
         } 
         catch (Exception e2) 
         {   
            e2.printStackTrace();
         }
         w.pesquisa1.setRowFilter(null);
         w.tmVol.setRowCount(0);
         for (WaterwatchVolume entrada : entradas) 
         {
            String volmc = String.format("%.000f", r.getVolumeMax() * (entrada.getVolumeDiario() / 100));
            Object rowData[] = {parseData.format(entrada.getData()), volmc +" m³",
                                entrada.getVolumeDiario() +" %", entrada.getQuantidadeChuva() +" mm"};
            w.tmVol.addRow(rowData);
         }
         w.tmVol.fireTableDataChanged();
         w.tbVol.repaint();
      }
      if (e.getSource() == w.bPsq1)
      {
         String texto = w.tfPsq1.getText();
         if (texto.trim().length() == 0) {
            w.pesquisa1.setRowFilter(null);
         } 
         else {
            w.pesquisa1.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
         }
      }
      if (e.getSource() == w.bPsq2)
      {
         String texto2 = w.tfPsq2.getText();
         if (texto2.trim().length() == 0) {
            w.pesquisa2.setRowFilter(null);
         } 
         else {
            w.pesquisa2.setRowFilter(RowFilter.regexFilter("(?i)" + texto2));
         }
      }     
      if (e.getSource() == w.bExcluir)
      {
         if(w.cb.getSelectedIndex() == -1)
         {
            w.erroNull(w);
            return;
         }
         w.dlExcluirEntrada(w);
      }
      
      if(e.getSource() == w.bExclui)
      {
         pos = w.cb.getSelectedIndex();
         Date dataExclui = (Date)w.spExclui.getValue();
         WaterwatchVolume v = new WaterwatchVolume(pos, dataExclui);
         boolean b = v.checarId(w.conn);
         if (b != true)
         {
            w.erroExclui(w);
            return;
         }
         v.excluir(w.conn);
      }   
   }   
}