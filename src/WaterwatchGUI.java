import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class WaterwatchGUI extends JFrame
{
   JTabbedPane abas;
   JComponent aba1, aba2;
   JPanel pnVol, pnLoc;
   JDialog dlNova, dlExclui;
   JLabel l;
   JComboBox cb, cb2;
   JTextField tfVol, tfVol2, tfVaz, tfDrn, tfEsp, tfPsq1, tfPsq2, tfLoc, tfVal, tfChv;
   JSpinner spDe, spAte, spData, spExclui;
   SpinnerModel smDe, smAte, smData, smExclui; 
   DefaultTableModel tmVol, tmLoc;
   JTable tbVol, tbLoc;
   JScrollPane rlVol, rlLoc;
   JButton bFiltrar, bAtualizar, bPsq1, bPsq2, bNova, bExcluir, bConfirma, bExclui;
   JRadioButton rbPct, rbMc;
   ButtonGroup bgPctMc;
   GridBagConstraints c = new GridBagConstraints();  
   TableRowSorter<TableModel> pesquisa1, pesquisa2;
   ArrayList<String> listaReservatorios;
   WaterwatchListener al = new WaterwatchListener(this);
   Color pCor = new Color(130, 225,255);
   Connection conn;
   
   public WaterwatchGUI()
   {
      super("WaterWatch - Mananciais de Sao Paulo");
      try 
      {
         WaterwatchConexao bd = new WaterwatchConexao();
         conn = bd.conectar();
         listaReservatorios = WaterwatchReservatorio.getNomes(conn);
      
      } 
      catch (Exception e) 
      { 
         e.printStackTrace();
         erroTenso(this);
      }
      abas = new JTabbedPane();
      aba1 = abaVolume();
      abas.addTab("Volume e Pluviometria", null, aba1, "Veja o volume e nivel pluviometrico em diferentes datas!");
      aba2 = abaLocais();
      abas.addTab("Abastecimento e Locais", null, aba2, "Veja os locais que cada reservatorio abastece!");
      add(abas);
      getContentPane().setBackground( pCor );
      setSize(800, 600);
      setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
   }
   
   private JComponent abaVolume()
   {
      pnVol = new JPanel();                    
      pnVol.setLayout(new GridBagLayout());
      pnVol.setBackground(pCor);            
                   
      l = new JLabel("Selecione um manancial:");
      c.gridwidth = 2;                                       
      c.insets = new Insets(10, 10, 0, 20);                
      c.anchor = GridBagConstraints.FIRST_LINE_START;       
      c.gridy = 0;                                          
      c.gridx = 0;                                      
      pnVol.add(l, c);                            
       
      cb = new JComboBox (listaReservatorios.toArray());
      cb.setMaximumRowCount(6);
      cb.setSelectedItem(null);
      c.insets = new Insets(10, 10, 50, 20);                 
      c.gridy = 1;
      cb.addActionListener(al);
      pnVol.add(cb, c);
      
      
      l = new JLabel("Volume maximo:");
      c.insets = new Insets(10, 10, 0, 20);
      c.gridy = 2;
      pnVol.add(l, c);
      
      tfVol = new JTextField(15);
      tfVol.setEditable(false);
      c.gridy = 3;
      pnVol.add(tfVol, c);
      
      l = new JLabel("Vazão:");
      c.gridy = 4;
      pnVol.add(l, c);
      
      tfVaz = new JTextField(15);
      tfVaz.setEditable(false);
      c.gridy = 5;
      pnVol.add(tfVaz, c);
      
      l = new JLabel("Area de drenagem:");
      c.gridy = 6;
      pnVol.add(l, c);
      
      tfDrn = new JTextField(15);
      tfDrn.setEditable(false);
      c.gridy = 7;
      pnVol.add(tfDrn, c);
      
      l = new JLabel("Espelho d agua:");
      c.gridy = 8;
      pnVol.add(l, c);
      
      tfEsp = new JTextField(15);
      tfEsp.setEditable(false);
      c.gridy = 9;
      pnVol.add(tfEsp, c);
      
      l = new JLabel("Filtrar por data:");
      c.gridy = 10;
      pnVol.add(l, c);
      
      l = new JLabel("De:");
      c.gridwidth = 1;
      c.insets = new Insets(10, 10, 0, 0);
      c.gridy = 11;
      pnVol.add(l, c);
      
      l = new JLabel("Ate:");
      c.gridy = 12;
      pnVol.add(l, c);
      
      smDe = new SpinnerDateModel();
      spDe = new JSpinner(smDe);
      spDe.setEditor(new JSpinner.DateEditor(spDe, "dd/MM/yyyy"));
      c.insets = new Insets(10, 5, 0, 0);
      c.gridx = 1;
      c.gridy = 11;
      pnVol.add(spDe, c);
      
      smAte = new SpinnerDateModel();
      spAte = new JSpinner(smAte);
      spAte.setEditor(new JSpinner.DateEditor(spAte, "dd/MM/yyyy"));
      c.gridx = 1;
      c.gridy = 12;
      pnVol.add(spAte, c);
      
      bFiltrar = new JButton("Filtrar");
      c.gridy = 13;
      bFiltrar.addActionListener(al);
      pnVol.add(bFiltrar, c);
      
      l = new JLabel("Volume e Pluviometria:");
      c.insets = new Insets(10, 0, 10, 0);
      c.gridx = 2;
      c.gridy = 0;
      pnVol.add(l, c);
      
      final String[] colunas = {"Data","Volume no dia","Volume em porcentagem","Quantidade de chuvas"};
      tmVol = new DefaultTableModel(colunas, 0);
      tbVol = new JTable(tmVol);
      pesquisa1 = new TableRowSorter<>(tbVol.getModel());
      tbVol.setRowSorter(pesquisa1);
      c.insets = new Insets(0, 0, 0, 0);
      c.gridx = 2;
      c.gridy = 1;
      c.gridwidth = 10;
      c.gridheight = 13;
      c.weightx = 1;
      c.weighty = 1;
      c.fill = GridBagConstraints.BOTH;
      pnVol.add(tbVol, c);
      
      rlVol = new JScrollPane(tbVol);
      rlVol.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      c.insets = new Insets(0,0,0,10);
      pnVol.add(rlVol, c);
      
      tfPsq1 = new JTextField(15);
      c.insets = new Insets(10, 0, 10, 0);
      c.fill = GridBagConstraints.NONE;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.weightx = 0;
      c.weighty = 0;
      c.gridy = 14;
      pnVol.add(tfPsq1, c);
      
      bPsq1 = new JButton("Pesquisar");
      bPsq1.setPreferredSize(new Dimension(100, 19));
      c.gridx = 3;
      bPsq1.addActionListener(al);
      pnVol.add(bPsq1, c);
      
      bNova = new JButton("Nova Entrada");
      bNova.setPreferredSize(new Dimension(120, 19));
      c.insets = new Insets(10, 10, 10, 0);
      c.gridx = 10;
      bNova.addActionListener(al);
      pnVol.add(bNova, c);      
      
      bExcluir = new JButton("Excluir entrada");
      bExcluir.setPreferredSize(new Dimension(120, 19));
      c.gridx = 11;
      bExcluir.addActionListener(al);
      pnVol.add(bExcluir, c);               
      return pnVol;
   }
   
   private JComponent abaLocais()
   {
      pnLoc = new JPanel();
      pnLoc.setLayout(new GridBagLayout());
      pnLoc.setBackground(pCor); 
      
      l = new JLabel("Selecione um manancial:");
      c.insets = new Insets(10, 10, 0, 20);
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.gridy = 0;
      c.gridx = 0;
      pnLoc.add(l, c);
       
      cb2 = new JComboBox (listaReservatorios.toArray());
      cb2.setMaximumRowCount(6);
      cb2.setSelectedItem(null);
      c.insets = new Insets(10, 10, 100, 20);
      c.gridy = 1;
      cb2.addActionListener(al);
      pnLoc.add(cb2, c);
      
      l = new JLabel("Volume maximo:");
      c.insets = new Insets(10, 10, 0, 20);
      c.gridy = 2;
      pnLoc.add(l, c);
      
      tfVol2 = new JTextField(15);
      tfVol2.setEditable(false);
      c.gridy = 3;
      pnLoc.add(tfVol2,c);
      
      l = new JLabel("Local do(s) reservatorio(s):");
      c.gridy = 4;
      pnLoc.add(l, c);
      
      tfLoc = new JTextField(15);
      tfLoc.setEditable(false);
      c.gridy = 5;
      pnLoc.add(tfLoc, c);
      
      l = new JLabel("Pluviometria diaria:");
      c.insets = new Insets(10, 0, 10, 0);
      c.gridx = 1;
      c.gridy = 0;
      pnLoc.add(l, c);
      
      final String[] colunas = {"Municipio","Residencias abastecidas","Habitantes total","Area abastecida","Area total"};
      tmLoc = new DefaultTableModel(colunas, 0);
      tbLoc = new JTable(tmLoc);
      pesquisa2 = new TableRowSorter<>(tbLoc.getModel());
      tbLoc.setRowSorter(pesquisa2);
      c.insets = new Insets(0, 0, 0, 0);
      c.gridx = 1;
      c.gridy = 1;
      c.gridwidth = 10;
      c.gridheight = 10;
      c.weightx = 1;
      c.weighty = 1;
      c.fill = GridBagConstraints.BOTH;
      pnLoc.add(tbLoc, c);
      
      rlLoc = new JScrollPane(tbLoc);
      rlLoc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      c.insets = new Insets(0,0,0,10);
      pnLoc.add(rlLoc, c);
      
      tfPsq2 = new JTextField(15);
      c.insets = new Insets(10, 0, 10, 0);
      c.fill = GridBagConstraints.NONE;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.gridy = 12;
      c.weightx = 0;
      c.weighty = 0;
      pnLoc.add(tfPsq2, c);
      
      bPsq2 = new JButton("Pesquisar");
      bPsq2.setPreferredSize(new Dimension(100, 19));
      c.gridx = 2;
      bPsq2.addActionListener(al);
      pnLoc.add(bPsq2, c);
      return pnLoc;
   }
   
   public JDialog dlNovaEntrada(JFrame frame)
   {
      dlNova = new JDialog(frame, "Nova entrada", true);
      dlNova.setLayout(new GridBagLayout());
      dlNova.setBackground(pCor);
      
      l = new JLabel("Escreva os dados da nova entrada diária:");
      c.gridx = 0;
      c.gridy = 0;
      c.insets = new Insets(10, 10, 20, 10);
      c.anchor = GridBagConstraints.PAGE_START;
      c.gridwidth = 2;
      dlNova.add(l, c);
      
      l = new JLabel("Data:");
      c.insets = new Insets(10, 0, 0, 0);
      c.anchor = GridBagConstraints.FIRST_LINE_END;
      c.gridwidth = 1;
      c.gridy = 1;
      dlNova.add(l, c);
      
      l = new JLabel("Volume:");
      c.gridy = 2;
      dlNova.add(l, c);
      
      l = new JLabel("Pluviometria:");
      c.gridy = 5;
      dlNova.add(l, c);
      
      smData = new SpinnerDateModel();
      spData = new JSpinner(smData);
      spData.setEditor(new JSpinner.DateEditor(spData, "dd/MM/yyyy"));
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.insets = new Insets(10, 10, 0, 0);
      c.gridx = 1;
      c.gridy = 1;
      dlNova.add(spData, c);
      
      tfVal = new JTextField(9);
      c.gridy = 2;
      dlNova.add(tfVal, c);
      
      rbPct = new JRadioButton("Porcentagem");
      rbPct.setSelected(true);
      c.gridx = 1;
      c.insets = new Insets(0, 8, 0, 0);
      c.gridy = 3;
      dlNova.add(rbPct, c);
      rbMc = new JRadioButton("Metros cubicos");
      c.gridy = 4;
      dlNova.add(rbMc, c);
      bgPctMc = new ButtonGroup();
      bgPctMc.add(rbPct);
      bgPctMc.add(rbMc);
      
      tfChv = new JTextField(9);
      c.insets = new Insets(10, 10, 0, 0);
      c.gridy = 5;
      dlNova.add(tfChv, c);
      
      bConfirma = new JButton("Confirmar");
      c.anchor = GridBagConstraints.CENTER;
      c.insets = new Insets(30, 10, 10, 10);
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 6;
      bConfirma.addActionListener(al);
      dlNova.add(bConfirma, c);
      
      dlNova.setSize(275, 300);
      dlNova.setResizable(false);
      dlNova.setLocationRelativeTo(null);
      dlNova.setVisible(true);
      return dlNova;
   }
   
   public JDialog dlExcluirEntrada(JFrame frame)
   {
      dlExclui = new JDialog(frame, "Excluir entrada", true);
      dlExclui.setLayout(new FlowLayout());
      dlExclui.setBackground(pCor);
      
      l = new JLabel("Selecione a data para excluir:");
      dlExclui.add(l);
      
      smExclui = new SpinnerDateModel();
      spExclui = new JSpinner(smExclui);
      spExclui.setEditor(new JSpinner.DateEditor(spExclui, "dd/MM/yyyy"));
      dlExclui.add(spExclui);
      
      bExclui = new JButton("Excluir");
      bExclui.addActionListener(al);
      dlExclui.add(bExclui);
      
      dlExclui.setSize(200, 100);
      dlExclui.setResizable(false);
      dlExclui.setLocationRelativeTo(null);
      dlExclui.setVisible(true);
      return dlExclui;   
   }
   
   public boolean dlConfirma(JDialog dialog, String valores)
   {
      boolean confirma;
      if (JOptionPane.showConfirmDialog(dialog, "Por favor, verificar se os dados estão corretos:\n\n" +valores
         + "\n\nDeseja inserir essa entrada?", "Confirmacao de dados", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
      { confirma = true; } 
      else 
      { confirma = false; }
      return confirma;
   }
   
   public void erroCampo(JDialog dialog, String campo)
   {
      JOptionPane.showMessageDialog(dialog, "O dado inserido no campo " +campo +" não é valido!",
                                    "Informacao invalida", JOptionPane.WARNING_MESSAGE);
   }
   
   public void erroNull(JFrame frame)
   {
      JOptionPane.showMessageDialog(frame, "Você não selecionou um reservatório!",
                                    "Erro", JOptionPane.WARNING_MESSAGE);
   }
   
   public void erroExclui(JFrame frame)
   {
      JOptionPane.showMessageDialog(frame, "Essa data não possui entrada!!!",
                                    "Erro", JOptionPane.WARNING_MESSAGE);
   }
   
   public void erroDuplicate(JFrame frame)
   {
      JOptionPane.showMessageDialog(frame, "Esta data já possui uma entrada! Exclua essa entrada ou escolha outro dia.",
                                    "Dia duplicado", JOptionPane.WARNING_MESSAGE);
   }
   
   public void erroTenso(JFrame frame)
   {
      JOptionPane.showMessageDialog(frame, "Ocorreu um erro inesperado. Tente as seguintes soluções:"
                                    +"\n\n- Verifique se o arquivo config.properties se encontra no diretório do aplicativo."
                                    +"\n- Verifique se as opções em config.properties estão corretas."
                                    +"\n- Verifique se o servidor e o banco de dados estão funcionando corretamente.",
                                    "Erro!!", JOptionPane.ERROR_MESSAGE);
   }
   
   public static void main (String[] args)
   {
      WaterwatchGUI w = new WaterwatchGUI();
   }
}