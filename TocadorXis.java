/*
 *** O programa 'TocadorXis' é parte da disciplina
 *   Introdução à Computação Sônica (CIC 116491)
 *------------------------------------------------
 *
 * (c) 2013 ICS, CIC, UnB
 *-------------------------------------------------------------------------
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
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;


public class TocadorXis extends JFrame implements Runnable
{
        private  int largura = 390;
        private  int altura  = 280;

        private  int posx   = 400;
        private  int posy   = 140;

        ImageIcon logo      = null;

        private String diretorio = System.getProperty("user.dir");

        final JButton botaoABRIR             = constroiBotao("Abrir", 9);
        final JButton botaoTOCAR             = constroiBotao("\u25b6", 9);
        final JButton botaoFAZERPAUSA        = constroiBotao("\u25ae\u25ae", 9);
        final JButton botaoPARAR             = constroiBotao("\u25fc", 9);

        final JButton botaoMOSTRADORcaminho  = constroiBotao(" DIR: "+ diretorio, 9);
        final JButton botaoMOSTRADORarquivo  = constroiBotao(" Arquivo: ", 9);
        final JButton botaoMOSTRADORduracao  = constroiBotao(" Dura\u00e7\u00e3o: ", 9);
        final JButton botaoMOSTRADORinstante = constroiBotao(" ", 9);
        final JButton botaoMOSTRADORvalorvolume = constroiBotao(" ", 9);



	private Sequencer  sequenciador = null;
	private Sequence   sequencia;
	private Receiver   receptor                = null;
	private long       inicio = 0;


	private int          volumeATUAL             = 75;
	private JSlider      sliderVolume            = new JSlider(JSlider.HORIZONTAL,0, 127, volumeATUAL);
        private JProgressBar sliderPROGRESSOinstante = new JProgressBar();


	private Container painel = getContentPane();
        private boolean   soando = false;


	public static void main(String[] args)
        {
          TocadorXis tocador = new TocadorXis();
          Thread     thread  = new Thread(tocador);
          thread.start();
	}


	public TocadorXis()
        {
            super("TocadorXis");
            personalizarInterfaceUsuario();

            ImageIcon logo   = new javax.swing.ImageIcon(getClass().getResource("icon.png"));
            setIconImage(logo.getImage());

            Color corOPR = new Color(180, 220, 220);
            Color corARQ = new Color(230, 230, 228);

            botaoABRIR.setBackground(corOPR);
            botaoTOCAR.setBackground(corOPR);
            botaoFAZERPAUSA.setBackground(corOPR);
            botaoPARAR.setBackground(corOPR);


            botaoABRIR.setEnabled(true);
            botaoTOCAR.setEnabled(false);
            botaoFAZERPAUSA.setEnabled(false);
            botaoPARAR.setEnabled(false);

            try{
                    JPanel p1 = new JPanel();
                    JPanel p2 = new JPanel();
                    JPanel p3 = new JPanel();
                    JPanel p4 = new JPanel();
                    JPanel p5 = new JPanel();
                    JPanel p6 = new JPanel();

                    JPanel painelOPERACOES = new JPanel();

                    painelOPERACOES.setLayout(new GridLayout(3,0));
                    painel.setLayout(new GridLayout(7,0));


                    botaoABRIR.addActionListener(new ActionListener()
                    { public void actionPerformed(ActionEvent e)
                      { abrir();
                      }
                    });

                    botaoTOCAR.addActionListener(new ActionListener()
                    { public void actionPerformed(ActionEvent e)
                      { tocar(botaoMOSTRADORcaminho.getText(),inicio);
                      }
                    });

                    botaoFAZERPAUSA.addActionListener(new ActionListener()
                    { public void actionPerformed(ActionEvent e)
                      { inicio = sequenciador.getMicrosecondPosition();
                        fazerpausa();
                      }
                    });

                    botaoPARAR.addActionListener(new ActionListener()
                    { public void actionPerformed(ActionEvent e)
                      { parar();
                      }
                    });


                    //-----
                    p1.add(botaoMOSTRADORcaminho);


                    //-----
                    p2.add(botaoABRIR);
                    p2.add(botaoTOCAR);
                    p2.add(botaoFAZERPAUSA);
                    p2.add(botaoPARAR);
                    //painelOPERACOES.add(p2);


                    //------
                    sliderPROGRESSOinstante.setPreferredSize(new Dimension(200,20));
                    sliderPROGRESSOinstante.setFocusable(false);
                    p5.add(sliderPROGRESSOinstante);
                    p5.add(botaoMOSTRADORinstante);

                    //-----
                    botaoMOSTRADORcaminho.setBackground(corARQ);
                    botaoMOSTRADORarquivo.setBackground(corARQ);
                    botaoMOSTRADORduracao.setBackground(corARQ);
                    botaoMOSTRADORinstante.setBackground(corARQ);
                    botaoMOSTRADORvalorvolume.setBackground(corARQ);

                    p3.add(botaoMOSTRADORarquivo);
                    p4.add(botaoMOSTRADORduracao);
                    //painelOPERACOES.add(p3);
                    //painelOPERACOES.add(p4);


                    JLabel vol = new JLabel("Volume: ");
                    sliderVolume.setPreferredSize(new Dimension(150,20));
                    sliderVolume.setFocusable(false);
                    p6.add(vol);
                    p6.add(sliderVolume);
                    p6.add(botaoMOSTRADORvalorvolume);

                    painel.add(p1);
                    //painel.add(painelOPERACOES);
                    painel.add(p2);
                    painel.add(p3);
                    painel.add(p4);
                    painel.add(p5);
                    painel.add(p6);

                    botaoMOSTRADORvalorvolume.setText("" + (volumeATUAL*100)/127 + "%");

                    sliderVolume.addChangeListener(new ChangeListener()
                    {
                        public void stateChanged(ChangeEvent e)
                        {
                            JSlider source = (JSlider)e.getSource();
                            if(!source.getValueIsAdjusting())
                            {
                                int valor = (int)source.getValue();

                                ShortMessage mensagemDeVolume = new ShortMessage();
                                for(int i=0; i<16; i++)
                                {
                                    try { mensagemDeVolume.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, valor);
                                          receptor.send(mensagemDeVolume, -1);
                                        }
                                    catch (InvalidMidiDataException e1) {}
                                }
                                volumeATUAL = valor;
                                botaoMOSTRADORvalorvolume.setText("" + (volumeATUAL*100)/127 + "%");
                            }
                        }
                    });

                    setSize(largura, altura);
                    setLocation(posx,posy);
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                    setVisible(true);
                    //this.setResizable(false);
            }
            catch(Exception e){
                    System.out.println(e.getMessage());
            }

	}



	public void tocar(String caminho, long inicio)
        {
            try
            {
                File arqmidi = new File(caminho);
                sequencia    = MidiSystem.getSequence(arqmidi);
                sequenciador = MidiSystem.getSequencer();

                sequenciador.setSequence(sequencia);
                sequenciador.open();
                retardo(500);
                sequenciador.start();

                receptor = sequenciador.getTransmitters().iterator().next().getReceiver();
                sequenciador.getTransmitter().setReceiver(receptor);

                botaoMOSTRADORarquivo.setText("Arquivo: \"" + arqmidi.getName() + "\"");

                    long duracao  = sequencia.getMicrosecondLength()/1000000;
                botaoMOSTRADORduracao.setText("\nDura\u00e7\u00e3o:"+ formataInstante(duracao));
                botaoMOSTRADORinstante.setText(formataInstante(0));

                sequenciador.setMicrosecondPosition(inicio);

                if (sequenciador.isRunning())
                { duracao = sequenciador.getMicrosecondLength();
                  soando = true;
                }
                else { soando = false;
                       sequenciador.stop();
                       sequenciador.close();
                       inicio = 0L;
                       duracao = 0;
                     }

                 botaoABRIR.setEnabled(false);
                 botaoTOCAR.setEnabled(false);
                 botaoFAZERPAUSA.setEnabled(true);
                 botaoPARAR.setEnabled(true);

            }
            catch(MidiUnavailableException e1) { System.out.println(e1+" : Dispositivo midi nao disponivel.");}
            catch(InvalidMidiDataException e2) { System.out.println(e2+" : Erro nos dados midi."); }
            catch(IOException              e3) { System.out.println(e3+" : O arquivo midi nao foi encontrado.");   }
            catch(Exception e){  System.out.println(e.toString());  }
	}

	void retardo(int miliseg)
	{
            try { Thread.sleep(miliseg);
                }
            catch(InterruptedException e) { }
	}


	public void fazerpausa()
        {
            soando = false;
            sequenciador.stop();

            botaoABRIR.setEnabled(false);
            botaoTOCAR.setEnabled(true);
            botaoFAZERPAUSA.setEnabled(false);
            botaoPARAR.setEnabled(false);
	}

	public void parar()
        {
            soando = false;
            sequenciador.stop();
            sequenciador.close();
            sequenciador = null;
            inicio = 0L;

             botaoABRIR.setEnabled(true);
             botaoTOCAR.setEnabled(true);
             botaoFAZERPAUSA.setEnabled(false);
             botaoPARAR.setEnabled(false);

             sliderPROGRESSOinstante.setValue(0);
             botaoMOSTRADORinstante.setText(formataInstante(0));
	}

	public void abrir()
        {
            JFileChooser selecao = new JFileChooser(".");
            selecao.setFileSelectionMode(JFileChooser.FILES_ONLY);
            selecao.setFileFilter(new FileFilter()
            {
                public boolean accept(File f)
                {
                    if (!f.isFile()) return true;
                    String name = f.getName().toLowerCase();
                    if (name.endsWith(".mid"))  return true;
                    if (name.endsWith(".midi")) return true;
                    return false;
                }

                public String getDescription()
                { return "Arquivo Midi (*.mid,*.midi)";
                }
            });
            selecao.showOpenDialog(this);


            botaoMOSTRADORcaminho.setText(selecao.getSelectedFile().toString());
            File arqseqnovo = selecao.getSelectedFile();
            try {
                  if(sequenciador!=null && sequenciador.isRunning()) { sequenciador.stop();
                                                                       sequenciador.close();
                                                                       sequenciador = null;
                                                                     }
                  Sequence sequencianova = MidiSystem.getSequence(arqseqnovo);
                  double duracao = sequencianova.getMicrosecondLength()/1000000.0d;

                  botaoMOSTRADORarquivo.setText("Arquivo: \"" + arqseqnovo.getName() + "\"");
                  botaoMOSTRADORduracao.setText("\nDura\u00e7\u00e3o:"+ formataInstante(duracao));

                  botaoTOCAR.setEnabled(true);
                  botaoFAZERPAUSA.setEnabled(false);
                  botaoPARAR.setEnabled(false);
                }
            catch (Throwable e1) { System.out.println("Erro em carregaArquivoMidi: "+ e1.toString());
                                 }
	}


	public void run()
        {
            double dur;
            double t;
            int    pos =0;

            while(true)
            {
                if (soando)
                { dur   = sequenciador.getMicrosecondLength()/1000000;
                  t     = sequenciador.getMicrosecondPosition()/1000000;
                  pos   = (int) ((t*100)/dur);
                  try {
                        sliderPROGRESSOinstante.setValue(pos);
                        botaoMOSTRADORinstante.setText(formataInstante(t));
                        retardo(1000);
                        if(t>=dur) {  sliderPROGRESSOinstante.setValue(0);
                                      botaoMOSTRADORinstante.setText(formataInstante(0));

                                      botaoABRIR.setEnabled(true);
                                      botaoTOCAR.setEnabled(true);
                                      botaoFAZERPAUSA.setEnabled(false);
                                      botaoPARAR.setEnabled(false);
                                   }
                      }
                  catch(Exception e) { System.out.println(e.getMessage());
                                     }
                }

                else{ try{ retardo(1000);
                         }
                      catch(Exception e) { System.out.println(e.getMessage());
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

        return inicio = "\n" + "   "+sh1+"h "+       sm1+"m "+    ss1+"s";
      }


      public String reformata(double x, int casas)
      { DecimalFormat df = new DecimalFormat() ;
        df.setGroupingUsed(false);
        df.setMaximumFractionDigits(casas);
        return df.format(x);
      }

	public JButton constroiBotao(String legenda)
        {
            JButton botao = new JButton(legenda);
            botao.setMargin(new Insets(2, 2, 2, 2));
            botao.setFocusable(false);
            botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
            return botao;
	}

	public JButton constroiBotao(String legenda, float tamanhoFonte)
        {
            JButton botao = new JButton(legenda);
            botao.setMargin(new Insets(2, 2, 2, 2));
            botao.setFocusable(false);
            botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
            botao.setFont(botao.getFont().deriveFont(tamanhoFonte));
            return botao;
	}



    //---procedimento para customizar a interface GUI
    private void personalizarInterfaceUsuario()
    {
            UIManager.put("FileChooser.openDialogTitleText", "Abrir arquivo midi");
            UIManager.put("FileChooser.lookInLabelText", "Buscar em");
            UIManager.put("FileChooser.openButtonText", "Abrir");
            UIManager.put("FileChooser.cancelButtonText", "Cancelar");
            UIManager.put("FileChooser.fileNameLabelText", "Nome do arquivo");
            UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo");
            UIManager.put("FileChooser.openButtonToolTipText", "Abrir o arquivo selecionado");
            UIManager.put("FileChooser.cancelButtonToolTipText","Cancelar");
            UIManager.put("FileChooser.fileNameHeaderText","Nome");
            UIManager.put("FileChooser.upFolderToolTipText", "Subir um nível");
            UIManager.put("FileChooser.homeFolderToolTipText","Nível home");
            UIManager.put("FileChooser.newFolderToolTipText","Criar pasta");
            UIManager.put("FileChooser.listViewButtonToolTipText","Em lista");
            UIManager.put("FileChooser.newFolderButtonText","Criar pasta");
            UIManager.put("FileChooser.renameFileButtonText", "Mudar o nome");
            UIManager.put("FileChooser.deleteFileButtonText", "Deletar");
            UIManager.put("FileChooser.filterLabelText", "Extensão de arquivo");
            UIManager.put("FileChooser.detailsViewButtonToolTipText", "Com detalhes");
            UIManager.put("FileChooser.fileSizeHeaderText","Tamanho");
            UIManager.put("FileChooser.fileDateHeaderText", "Data de modificação");
            UIManager.put("FileChooser.acceptAllFileFilterText", "Binário");

            UIManager.put("FileChooser.saveButtonText", "Salvar");
            UIManager.put("FileChooser.saveDialogTitleText", "Salvar em");
            UIManager.put("FileChooser.saveInLabelText", "Salvar em");
            UIManager.put("FileChooser.saveButtonToolTipText", "Salvar arquivo selecionado");

            UIManager.put("OptionPane.yesButtonText",    "Sim");
            UIManager.put("OptionPane.noButtonText",     "Não");
            UIManager.put("OptionPane.cancelButtonText", "Cancelar");

            UIManager.put("FileChooser.listFont", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("FileChooser.fileNameLabelText", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("FileChooser.filesOfTypeLabelText", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));

            UIManager.put("JSlider.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JSlider.listFont", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JSlider", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JFileChooser.listFont", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JFileChooser.fileNameLabelText", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JFileChooser.filesOfTypeLabelText", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JButton", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));

            UIManager.put("OptionPane.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("Button.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("RadioButton.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));

            UIManager.put("Label.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JLabel.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("JLabel", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));


            UIManager.put("ComboBox.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("ToolTip.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("EditorPane.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("List.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("Panel.listFont", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("Panel.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("Table.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("TextArea.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("TextField.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("TextPane.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("JTextArea.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("JTextField.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("JTextPane.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
            UIManager.put("InternalFrame.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("InternalFrame.titleFont",new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("Frame.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("Frame.titleFont",new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));
            UIManager.put("ScrollPane.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 11)));

            UIManager.put("ProgressBar.font", new javax.swing.plaf.FontUIResource(new Font("Arial", java.awt.Font.PLAIN, 10)));
        }




}

//-----18/05/2013 (ICS 2013/1)
