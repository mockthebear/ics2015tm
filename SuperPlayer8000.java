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

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;

import javax.swing.filechooser.FileNameExtensionFilter;


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
        private  int w = 590;
        private  int h  = 280;

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



    public SuperPlayer8000()
        {
            super("SuperPlayer8000");


            //Carrega o logo
            ImageIcon logo   = new javax.swing.ImageIcon(getClass().getResource("icon.png"));
            setIconImage(logo.getImage());

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
            JPanel Colunes[] = {new JPanel(),new JPanel()};
            JPanel SecondRightPanel = new JPanel();
            JPanel Lines[] = {new JPanel(),new JPanel(),new JPanel(),new JPanel(),new JPanel(),new JPanel()};



            //painel.setLayout(new GridLayout(1,2)); //2 colunas
            //A primeira coluna vai conter tudo
            Colunes[0].setLayout(new GridLayout(6,0));
            //Coluna do volume
            Colunes[1].setLayout(new GridLayout(1,0));


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

            painel.setLayout(new BoxLayout(painel, BoxLayout.X_AXIS));

            painel.add(Colunes[0]);
            painel.add(Colunes[1]);
            //Faz a janela aparecer
            setSize(w, h);
            setResizable(false);
            setLocation(640,480);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);



	}


    //Play
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
        atch(InterruptedException e) { }
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


}

