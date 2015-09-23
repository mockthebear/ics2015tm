/*
 Sim eu inclui tudo isso direto do tocador Xis!! c:
*/
import java.text.DecimalFormat;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
public class SuperPlayer8000 extends JFrame implements Runnable
{
    //Main
    public static void main(String[] args)
    {
          //Isso é para a função run. ela vai rodar paralelamente a função main
          Thread thread  = new Thread(new SuperPlayer8000());
          thread.start();
	}

    //Estrutura e informações basicas do programa
        private  int w = 690;
        private  int h  = 480;

        ImageIcon icon      = null;
        //Isso veio do tocadorXis
        private String diretorio                = System.getProperty("user.dir");



        //Botoes base
        final JButton PlayB                     = new JButton("Play");
        final JButton PauseB                    = new JButton("Pause");
        final JButton StopB                     = new JButton("Stop");
        final JButton OpenB                     = new JButton("Open a file!!!!");
        //Label que vai conter o arquivo e algumas variaveis relacionadas a arquivo
        JLabel Cfile                            = new JLabel("Current file: ");
        private String FileName                 = "";
        private File MidiFile                   = null;
        //Estrutura do JavaSound
        private Sequencer  SequencerVar         = null;
        private Sequence   SequencePlayer;
        private Receiver   receptor             = null;
        //Relacionado a duração play e pause
        private JProgressBar Progress           = new JProgressBar();
        private double Duration                 = 0;
        private long pausedDuration             = 0;
        private long inicio                     = 0;
        private boolean Paused                  = false;
        private JProgressBar Progresso          = new JProgressBar();
        JLabel DurationLabel                    = new JLabel("Duracao: 00:00:00");
        JLabel TDLabel                          = new JLabel("Total: 00:00:00");
        //Relacionado a volume
        //Inicia-se com metade do volume
        /*
            Sabe-se que a escala de volume perceptivel é logaritimica
            Dado que o computador armazena a escala de 0 a 1, sendo 0% e 100%
            0-127 corresponde respectivamente a escala de volume.
            A metade não é 75 nem 63, e sim 89 pois:
            sqrt((127*127)/2.0) = 89

            edit: vi que no java eles ja consideram isso.
            NEVERMIND!!!!

        */
        private int          volume             = 50;
        private JSlider      VolumeUi           = new JSlider(JSlider.VERTICAL,0, 127, volume);
        JLabel VolumeLabel                      = new JLabel("Volume: " + 50 + "%");

        //Painel principal
        private Container painel                = getContentPane();


        private DefaultTableModel model;
        private DefaultTableModel modeltrilhas;
        private JTable table;
        private JTable tabletrilhas;

        static final int FORMULA_DE_COMPASSO = 0x58;




    public SuperPlayer8000()
        {
            super("SuperPlayer8000");


            //Carrega o logo
            ImageIcon logo   = new javax.swing.ImageIcon(getClass().getResource("icon.png"));

            setIconImage(logo.getImage());
            model = new DefaultTableModel();
            modeltrilhas = new DefaultTableModel();
            modeltrilhas.addColumn("Instante");//trilha 0... tem que ter pelo menos uma!
            model.addColumn("Reso.");
            model.addColumn("Dur (s)");
            model.addColumn("N ticks");
            model.addColumn("T ticks");
            model.addColumn("T C.min");
            model.addColumn("N C.min");
            model.addColumn("BMP");
            model.addColumn("Trilhas");

            //Estrutura da interface
            /*
                Complicado...

                Considere que um elemento Jpanel é uma lista que contem outros elementos
                Existe formas de se exibir essa lista na tela.
                Por exemplo. se eu colocar Jpanl com GridLayout(2,2) e adcionar 4 botões
                Eles vão ser exibidos:
                [b1][b2]
                [b3][b4]
                Acontece que dependendo de como vc tiver o tamanho da tela ele edita isso em tempo
                de execução.
                Outra coisa importante é que esse cunjunto de 4 botões pode ser um elemento só, pois
                eles estão dentro o Jpanel.
                Eu tentei ordenar a janela em um grande panel contendo 2 panels
                o primeiro é onde fica o player, e o segundo é onde fica o volume.
                o panel do player é apenas mais uma sequencia de paineis.
                Ficou confuso logo a baixo :c

            */
            int LINES_NUMBER = 5;
            ImagePanel panele = new ImagePanel(new ImageIcon("bher.png").getImage());

            JPanel Colunes[] = {new JPanel(),new JPanel()};
            JPanel SecondRightPanel = new ImagePanel(new ImageIcon("volume.png").getImage());
            JPanel Lines[] = {new JPanel(),new JPanel(),new JPanel(),new JPanel(),new JPanel(),new JPanel()};

            JPanel ExtraPanel = new JPanel();



            JPanel TablePanel = new JPanel();

            //Painel extra para informações midi:





            //painel.setLayout(new GridLayout(1,2)); //2 colunas
            //A primeira coluna vai conter tudo
            Colunes[0].setLayout(new GridLayout(6,0));
            //Coluna do volume
            Colunes[1].setLayout(new GridLayout(1,0));

            TablePanel.setLayout(new GridLayout(2,0));
            painel.setLayout(new GridLayout(2,0));





            //Botões de menu devem ficar em cima
            //Botão de open
            OpenB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    OpenFile();
                }
            });
            OpenB.setEnabled(true);
            //Ele vai ficar na primeira linha da primeira coluna
            Lines[0].add(OpenB);


            //File name
            UpdateFile("");
            //Ele vai ficar na primeira linha da primeira coluna
            Lines[1].add(Cfile);


            /*
                Informações:
            */
            JPanel InformationPanel = new JPanel();
            InformationPanel.setLayout(new GridLayout(2,0));
            InformationPanel.add(TDLabel);
            Lines[2].add(InformationPanel);
            Lines[3].add(DurationLabel);
            Lines[4].add(Progresso);




            //Botões devem ficar na utima linha
            PlayB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    Play(0);
                }
            });
            PlayB.setEnabled(false);
            Lines[LINES_NUMBER].add(PlayB);

            PauseB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    Pause();
                }
            });
            PauseB.setEnabled(false);
            Lines[LINES_NUMBER].add(PauseB);

            StopB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    Stop();
                }
            });
            StopB.setEnabled(false);
            Lines[LINES_NUMBER].add(StopB);

            //Seta o volume
            VolumeUi.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider)e.getSource();
                        if (receptor != null){
                            int valor = (int)source.getValue();
                            ShortMessage mensagemDeVolume = new ShortMessage();
                            for(int i=0; i<16; i++)
                            {
                                try { mensagemDeVolume.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, valor);
                                        receptor.send(mensagemDeVolume, -1);
                                    }
                                catch (InvalidMidiDataException e1) {}
                            }
                            volume = valor;
                            VolumeLabel.setText("Volume: " + reformata((volume*100)/127, 3) + "%");
                        }else{
                            VolumeUi.setValue(89);
                        }
                    }

                });


            SecondRightPanel.add(VolumeUi);
            SecondRightPanel.add(VolumeLabel);




            for (int i=0;i<=LINES_NUMBER;i++){
                Colunes[0].add(Lines[i]);
            }

            //NAO SEI PQ FUNCIONOU.
            //NAO MEXE NISSO!!
            Colunes[0].setBorder(BorderFactory.createEtchedBorder());
            Colunes[1].setBorder(BorderFactory.createEtchedBorder());

            Colunes[0].setAlignmentX( Component.LEFT_ALIGNMENT );
            Colunes[1].setAlignmentX( Component.RIGHT_ALIGNMENT );

            SecondRightPanel.setAlignmentY( Component.TOP_ALIGNMENT );
            SecondRightPanel.setLayout(new BoxLayout(SecondRightPanel, BoxLayout.Y_AXIS));
            Colunes[1].add(SecondRightPanel);

            ExtraPanel.setLayout(new BoxLayout(ExtraPanel, BoxLayout.X_AXIS));

            ExtraPanel.add(Colunes[0]);
            ExtraPanel.add(Colunes[1]);


            painel.add(ExtraPanel);


            table = new JTable(model);
            tabletrilhas = new JTable(modeltrilhas);
            JScrollPane scrollPane = new JScrollPane(table);
            JScrollPane scrollPanetrilha = new JScrollPane(tabletrilhas);



            TablePanel.add(scrollPane, BorderLayout.CENTER);
            TablePanel.add(scrollPanetrilha, BorderLayout.CENTER);
            painel.add(TablePanel);


            //painel.add(new JButton("Isso nao faz nada"));
            //Faz a janela aparecer
            setSize(w, h);
            setResizable(false);
            setLocation(640,480);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);



	}


    //Play
    public void PrintData(){


        long duracao     = SequencePlayer.getMicrosecondLength()/1000000;
        int  resolucao   = SequencePlayer.getResolution();
        long totaltiques = SequencePlayer.getTickLength();

        float durtique       = (float)duracao/totaltiques;
        float durseminima    = durtique*resolucao;
        float bpm            = 60/durseminima;
        int   totalseminimas = (int)(duracao/durseminima);
        Track[] trilhas = SequencePlayer.getTracks();

        String[] data = { "", "", "","", "", "","", "", "","", "", "","", "", "" };
        data[0] = ""+ resolucao + "";
        data[1] = ""+ duracao + "";
        data[2] = ""+ totaltiques + "";
        data[3] = ""+ durtique + "";
        data[4] = ""+ durseminima + "";
        data[5] = ""+ totalseminimas + "";
        data[6] = ""+ Math.round(bpm) + "";
        data[7] = ""+ trilhas.length+ "";

        model.addRow(data);
        //Data to load and show stuff
        int maxLoaded = 10;
        int maxLoaded_all = 0;
        String[][] Loaded   = new String[maxLoaded][trilhas.length+1];
        String[] F_Line = new String[trilhas.length+1];
        long[][] check       = new long[maxLoaded][trilhas.length+1];
        for(int i=0; i<trilhas.length; i++){
            for(int e=0; e<maxLoaded; e++){
               check[e][i+1] = -1;
            }
        }
        F_Line[0] = "-";
        //Itarator on trilhas
        for(int i=0; i<trilhas.length; i++){

                Track trilha =  trilhas[i];

                Par    fc  =  null;
                String st  = "--";
                String stx = "--";
                if(i==0){
                    fc = getFormulaDeCompasso(trilha);
                }
                if(i==0){
                    try{
                        st =  getTonalidade(trilha);
                    }
                    catch(Exception e){}
                }


                //---MetaMensagem de texto
                try{
                    stx =  getTexto(trilha);
                }
                catch(Exception e){}


	             modeltrilhas.addColumn(""+i+":"+st);

	             F_Line[i+1] = stx;
	             if(fc!=null)
                    F_Line[i+1] = F_Line[i+1]+""+fc.getX() +":"+ (int)(Math.pow(2, fc.getY()))+"";


                for(int j=0; j<trilha.size(); j++)
                {
                  MidiEvent   e          = trilha.get(j);
                  MidiMessage mensagem   = e.getMessage();
                  long        tick      = e.getTick();




                  int statusNmbr = mensagem.getStatus();
                  byte msgNmbr[] = mensagem.getMessage();
                  int NM = 3;

                  String nomecomando = "Uncoded";

                  switch(statusNmbr)
                  {
                      case 128: nomecomando = "noteON"; break;
                      case 144: nomecomando = "noteOFF"; break;
                      case 176: nomecomando = "Control Change"; break;
                      case 255: nomecomando = "System"; break;
                      //---(introduzir outros casos)
                  }
                  nomecomando = nomecomando+"["+statusNmbr+"]<"+msgNmbr[0]+">";
                    boolean found = false;
                    int position = 0;
                    for (int aux=0;aux<maxLoaded_all;aux++){
                        if (check[aux][0] == tick){
                            if (check[aux][i+1] == -1){
                                check[aux][i+1] = 1;
                                position = aux;
                                System.out.println("found at pos "+aux);
                                found = true;
                                break;
                            }else{
                                System.out.println("found at pos "+aux+" but "+(i+1)+" is "+check[aux][i+1]);
                            }

                        }
                    }
                    if (found == false){
                        Loaded[maxLoaded_all][0] = ""+tick+"";
                        check[maxLoaded_all][0] = tick;
                        position = maxLoaded_all;
                        maxLoaded_all++;
                        if (maxLoaded_all >= maxLoaded){
                            String[][] Loaded_aux = new String[maxLoaded+100][trilhas.length+1];
                            long[][] check_aux       = new long[maxLoaded+100][trilhas.length+1];
                            for(int k=0; k<trilhas.length; k++){
                                for(int m=0; m<maxLoaded+100; m++){
                                   check_aux[m][k+1] = -1;
                                }
                            }
                            for (int k=0;k<maxLoaded;k++){
                                for (int m=0;m<trilhas.length+1;m++){
                                    Loaded_aux[k][m] = Loaded[k][m];
                                    check_aux[k][m] = check[k][m];
                                }
                            }
                            check = check_aux;
                            Loaded = Loaded_aux;
                            maxLoaded += 100;

                        }






                    }

                  //Realloc



                  Loaded[position][i+1] = nomecomando;


                }
              }
              modeltrilhas.addRow(F_Line);
              for (int i=0;i<maxLoaded_all;i++){

                    modeltrilhas.addRow(Loaded[i]);
              }

    }
	public void Play(long inicio){
	    try{

                Paused = false;
                retardo(100);
                SequencerVar.start();
                receptor = SequencerVar.getTransmitters().iterator().next().getReceiver();
                SequencerVar.getTransmitter().setReceiver(receptor);
                long duracao  = SequencerVar.getMicrosecondLength()/1000000;
                TDLabel.setText("Total:" + formataInstante(duracao));
                SequencerVar.setMicrosecondPosition(inicio);
                PauseB.setEnabled(true);
                PlayB.setEnabled(true);
                StopB.setEnabled(true);
            }
            catch(Exception e){  System.out.println(e.toString());  }

	}

	void retardo(int miliseg){
	 try {
            Thread.sleep(miliseg);
        }
        catch(InterruptedException e) { }
    }


	public void Pause(){
	    retardo(100);
	    if (Paused == false){
            SequencerVar.stop();
            Paused = true;

            PauseB.setEnabled(true);
            PlayB.setEnabled(false);
            StopB.setEnabled(true);

        }else{
            SequencerVar.start();
            Paused = false;
            PlayB.setEnabled(true);
            PauseB.setEnabled(true);
            StopB.setEnabled(true);
	    }
	}

	public void Stop(){
	    retardo(100);
	    Paused = false;
	    SequencerVar.stop();
	    pausedDuration = 0;
	    PauseB.setEnabled(false);
        PlayB.setEnabled(true);
        StopB.setEnabled(false);

	}
    public void UpdateFile(String str){
        Cfile.setText("Current file: " + str);
    }
	public void OpenFile(){
        //cria a janela de selecionar
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //Define para aceitar só .midi e .mid
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Midi files", "midi","mid");
        chooser.addChoosableFileFilter(filter);
        //Se o usuario selecionar um arquivo de fato:
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            //Caso esteja tocando algo, parar :V
            if(SequencerVar!=null && SequencerVar.isRunning()) {
                SequencerVar.stop();
                SequencerVar.close();
                SequencerVar = null;
            }
            //Nome
            FileName = chooser.getSelectedFile().toString();
            UpdateFile(FileName);
            //Abre o arquivo
            MidiFile = chooser.getSelectedFile();
            //Abre um sequenciador
            try {
                SequencePlayer = MidiSystem.getSequence(MidiFile);
                Duration = SequencePlayer.getMicrosecondLength()/1000000.0d;
                SequencerVar = MidiSystem.getSequencer();
                PauseB.setEnabled(false);
                PlayB.setEnabled(true);
                StopB.setEnabled(false);
                SequencerVar.setSequence(SequencePlayer);
                SequencerVar.open();
                SequencerVar.getTransmitter().setReceiver(receptor);
                TDLabel.setText("Total:" + formataInstante(SequencerVar.getMicrosecondLength()/1000000));
                PrintData();
                retardo(100);
            }
            catch(InvalidMidiDataException e2) { System.out.println(e2+" : Erro nos dados midi."); }
            catch(IOException              e3) { System.out.println(e3+" : O arquivo midi nao foi encontrado.");   }
            catch(MidiUnavailableException e1) { System.out.println(e1+" : Dispositivo midi nao disponivel.");}
        }
	}
    public void run(){
        double dur;
        double t;
        int    pos =0;
        while(true){
            if (SequencerVar != null && SequencerVar.isRunning()){
                dur   = SequencerVar.getMicrosecondLength()/1000000;
                t     = SequencerVar.getMicrosecondPosition()/1000000;
                pos   = (int) ((t*100)/dur);
                try {

                    DurationLabel.setText(formataInstante(t));

                    if(t>=dur){
                        Progresso.setValue(0);
                        DurationLabel.setText("Duração:" + formataInstante(0));
                        PlayB.setEnabled(true);
                        PauseB.setEnabled(false);
                        StopB.setEnabled(false);
                    }else{
                        Progresso.setValue(pos);
                    }

                }
                catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                retardo(100);
            }else{
                try{
                    retardo(500);
                }
                catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }


	}
//Sim eu pegei isso do TocadorXis
    public String formataInstante(double t1)
      {
        String inicio    = "";

        //--------início
        double h1  = (int)(t1/3600.0);
        double m1  = (int)((t1 - 3600*h1)/60);
        double s1  = (t1 - (3600*h1 +60*m1));


        double h1r  = t1/3600.0;
        double m1r  = (t1 - 3600*h1)/60.0f;
        double s1r  = (t1 - (3600*h1 +60*m1));

        String sh1="";
        String sm1="";
        String ss1="";

        if     (h1 ==0) sh1 = "00";
        else if(h1 <10) sh1 = "0"+reformata(h1, 0);
        else if(h1<100) sh1 = "" +reformata(h1, 0);
        else            sh1 = "" +reformata(h1, 0);

        if     (m1 ==0) sm1 = "00";
        else if(m1 <10) sm1= "0"+reformata(m1, 0);
        else if(m1 <60) sm1 = ""+reformata(m1, 0);

        if     (s1 ==0) ss1 = "00";
        else if(s1 <10) ss1 = "0"+reformata(s1r, 2);
        else if(s1 <60) ss1 = reformata(s1r, 2);

        return inicio = "\n" + "   "+sh1+":"+       sm1+":"+    ss1+"";
      }

    public String reformata(double x, int casas)
      { DecimalFormat df = new DecimalFormat() ;
        df.setGroupingUsed(false);
        df.setMaximumFractionDigits(casas);
        return df.format(x);
      }

    static Par getFormulaDeCompasso(Track trilha)
    {   int p=1;
        int q=1;

        for(int i=0; i<trilha.size(); i++)
        {
          MidiMessage m = trilha.get(i).getMessage();
          if(m instanceof MetaMessage)
          {
            if(((MetaMessage)m).getType()==FORMULA_DE_COMPASSO)
            {
                MetaMessage mm = (MetaMessage)m;
                byte[] data = mm.getData();
                p = data[0];
                q = data[1];
                return new Par(p,q);
            }
          }
        }
        return new Par(p,q);
    }



    static private class Par
    { int x, y;

      Par (int x_, int y_)
      { this.x = x_;
        this.y = y_;
      }

      int getX()
      { return x;
      }

      int getY()
      { return y;
      }

    }
    static final int MENSAGEM_TONALIDADE = 0x59;
    static String getTonalidade(Track trilha) throws InvalidMidiDataException
    {
       String stonalidade = "";
       for(int i=0; i<trilha.size(); i++)
       { MidiMessage m = trilha.get(i).getMessage();


       if(((MetaMessage)m).getType() == MENSAGEM_TONALIDADE)
       {
            MetaMessage mm        = (MetaMessage)m;
            byte[]     data       = mm.getData();
            byte       tonalidade = data[0];
            byte       maior      = data[1];

            String       smaior = "Maior";
            if(maior==1) smaior = "Menor";

            if(smaior.equalsIgnoreCase("Maior"))
            {
                switch (tonalidade)
                {
                    case -7: stonalidade = "Dób Maior"; break;
                    case -6: stonalidade = "Solb Maior"; break;
                    case -5: stonalidade = "Réb Maior"; break;
                    case -4: stonalidade = "Láb Maior"; break;
                    case -3: stonalidade = "Mib Maior"; break;
                    case -2: stonalidade = "Sib Maior"; break;
                    case -1: stonalidade = "Fá Maior"; break;
                    case  0: stonalidade = "Dó Maior"; break;
                    case  1: stonalidade = "Sol Maior"; break;
                    case  2: stonalidade = "Ré Maior"; break;
                    case  3: stonalidade = "Lá Maior"; break;
                    case  4: stonalidade = "Mi Maior"; break;
                    case  5: stonalidade = "Si Maior"; break;
                    case  6: stonalidade = "Fá# Maior"; break;
                    case  7: stonalidade = "Dó# Maior"; break;
                }
            }

            else if(smaior.equalsIgnoreCase("Menor"))
            {
                switch (tonalidade)
                {
                    case -7: stonalidade = "Láb Menor"; break;
                    case -6: stonalidade = "Mib Menor"; break;
                    case -5: stonalidade = "Sib Menor"; break;
                    case -4: stonalidade = "Fá Menor"; break;
                    case -3: stonalidade = "Dó Menor"; break;
                    case -2: stonalidade = "Sol Menor"; break;
                    case -1: stonalidade = "Ré Menor"; break;
                    case  0: stonalidade = "Lá Menor"; break;
                    case  1: stonalidade = "Mi Menor"; break;
                    case  2: stonalidade = "Si Menor"; break;
                    case  3: stonalidade = "Fá# Menor"; break;
                    case  4: stonalidade = "Dó# Menor"; break;
                    case  5: stonalidade = "Sol# Menor"; break;
                    case  6: stonalidade = "Ré# Menor"; break;
                    case  7: stonalidade = "Lá# Menor"; break;
                }
            }
         }
      }
      return stonalidade;
    }

    static final int MENSAGEM_TEXTO = 0x01;

    static String getTexto(Track trilha) throws InvalidMidiDataException
    {
       String stexto = "";

       for(int i=0; i<trilha.size(); i++)
       { MidiMessage m = trilha.get(i).getMessage();

         if(((MetaMessage)m).getType() == MENSAGEM_TEXTO)
         {
           MetaMessage mm  = (MetaMessage)m;
           byte[]     data = mm.getData();

           for(int j=0; j<data.length; j++)
           { stexto += (char)data[j];
           }
        }
     }
     return stexto;
    }


}

class ImagePanel extends JPanel {

  private Image img;

  public ImagePanel(String img) {
    this(new ImageIcon(img).getImage());
  }

  public ImagePanel(Image img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    //setPreferredSize(size);
    //setMinimumSize(size);
    //setMaximumSize(size);
    //setSize(size);
    //setLayout(null);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }

}

