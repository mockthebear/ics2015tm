
import sintese.*;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class T2 extends JFrame implements Runnable
{
    final JButton Button0                     = new JButton("Batata");
    final JButton Button1                     = new JButton("instrumento A");
    final JButton Button2                     = new JButton("instrumento B");
    final JButton Button3                     = new JButton("instrumento C");

    final JButton bm1                     = new JButton("Fuga 1");
    final JButton bm2                     = new JButton("tema_aa_drawing_quintet_flauta");
    final JButton bm3                     = new JButton("tema_bwv775_invencao4_direita");
    final JButton bm4                     = new JButton("melodiasexta1");
    private Container painel                = getContentPane();
    JLabel frequenciaLab        = new JLabel("Frequencia:");
    JTextField frequenciaGet    = new JTextField("0.2");
    JLabel lambdaLab            = new JLabel("Lambda:");
    JTextField lambdaGet        = new JTextField("0.5");
    JLabel faseLab              = new JLabel("Fase:");
    JTextField faseGet          = new JTextField("0.0");
    JLabel ganhoLab             = new JLabel("Ganho:");
    JTextField ganhoGet         = new JTextField("103.0f");
    JLabel andamentoLab         = new JLabel("Andamento:");
    JTextField andamentoGet     = new JTextField("0.95");

    JLabel pontoslabel             = new JLabel("Pontos:");

    JTextField curvaPontos[]    = {
        new JTextField("0"),new JTextField("0"),
        new JTextField("30"),new JTextField("400"),
        new JTextField("240"),new JTextField("300"),
        new JTextField("720"),new JTextField("0"),
        new JTextField("-1"),new JTextField("-1"),
        new JTextField("-1"),new JTextField("-1"),
        new JTextField("-1"),new JTextField("-1"),
    };



    Melodia m2                  = Tema.tema_aa_fuga1();
    InstrumentoT2A instrumentoA                     = new InstrumentoT2A(0.2f);
    InstrumentoT2B instrumentoB                     = new InstrumentoT2B(0.2f);
    InstrumentoT2C instrumentoC                     = new InstrumentoT2C(0.2f);

    float frequencia    =   0.2f;
    float lambda        =   0.5f;
    float fase          =   0.0f;
    float ganho         =   103.0f;
    float andamento     =   0.95f;

    public T2()
    {
        super("T2");
        JPanel Colunes[] = {new JPanel(),new JPanel(),new JPanel()};
        Colunes[0].add(Button1);
        Colunes[0].setBorder(BorderFactory.createEtchedBorder());


        Colunes[1].add(Button2);
        Colunes[1].setBorder(BorderFactory.createEtchedBorder());


        Colunes[2].add(Button3);
        Colunes[2].setBorder(BorderFactory.createEtchedBorder());


        JPanel ExtraPanel = new JPanel();
        JPanel SettingsPanel = new JPanel();
        SettingsPanel.setBorder(BorderFactory.createEtchedBorder());
        SettingsPanel.setLayout(new GridLayout(0,4));


        JPanel CurvaPanel = new JPanel();
        JPanel TemaPanel = new JPanel();
        JPanel CurvaPanel_big = new JPanel();
        CurvaPanel.setLayout(new GridLayout(8,2));
        CurvaPanel_big.setLayout(new GridLayout(0,1));
        TemaPanel.setLayout(new GridLayout(2,2));

        SettingsPanel.add(frequenciaLab);
        SettingsPanel.add(frequenciaGet);
        SettingsPanel.add(lambdaLab);
        SettingsPanel.add(lambdaGet);
        SettingsPanel.add(faseLab);
        SettingsPanel.add(faseGet);
        SettingsPanel.add(ganhoLab);
        SettingsPanel.add(ganhoGet);
        SettingsPanel.add(andamentoLab);
        SettingsPanel.add(andamentoGet);


        //ExtraPanel.setLayout(new BoxLayout(ExtraPanel, BoxLayout.X_AXIS));


        ExtraPanel.add(Colunes[0]);
        ExtraPanel.add(Colunes[1]);
        ExtraPanel.add(Colunes[2]);


        CurvaPanel_big.add(pontoslabel);
        CurvaPanel_big.add(CurvaPanel);

        CurvaPanel.add(curvaPontos[0]);
        CurvaPanel.add(curvaPontos[1]);
        CurvaPanel.add(curvaPontos[2]);
        CurvaPanel.add(curvaPontos[3]);
        CurvaPanel.add(curvaPontos[4]);
        CurvaPanel.add(curvaPontos[5]);
        CurvaPanel.add(curvaPontos[6]);
        CurvaPanel.add(curvaPontos[7]);
        CurvaPanel.add(curvaPontos[8]);
        CurvaPanel.add(curvaPontos[9]);
        CurvaPanel.add(curvaPontos[10]);
        CurvaPanel.add(curvaPontos[11]);
        CurvaPanel.add(curvaPontos[12]);
        CurvaPanel.add(curvaPontos[13]);

        TemaPanel.add(bm1);
        TemaPanel.add(bm2);
        TemaPanel.add(bm3);
        TemaPanel.add(bm4);

        painel.setLayout(new GridLayout(2,2));
        painel.add(ExtraPanel);
        painel.add(SettingsPanel);
        painel.add(CurvaPanel_big);
        painel.add(TemaPanel);

        bm1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                m2 = Tema.tema_aa_fuga1();
            }
        });
        bm2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                m2 = Tema.tema_aa_drawing_quintet_flauta();
            }
        });
        bm3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                m2 = Tema.tema_bwv775_invencao4_direita();
            }
        });
        bm4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                m2 = Tema. 	melodiasexta1();
            }
        });

        Button1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateData();
                Envoltoria   env    =   new Envoltoria(); ;
                Envoltoria   envFREQUENCIACORTE;
                Curva        curva  = new Curva(720);
                UnidadeH		UH1 =   new UnidadeH();
                UnidadeH		UH2 =   new UnidadeH();

                for (int i=0;i<14;i += 2){

                    String str1 = curvaPontos[i].getText();
                    String str2 = curvaPontos[i+1].getText();
                    if (!str1.equals("") && !str2.equals("")) {
                        double f1 = Double.parseDouble(str1);
                        double f2 = Double.parseDouble(str2);
                        if (f1 >= 0 && f2 >= 0){
                                System.out.print("Ponto: ");
                            System.out.print(f1);
                            System.out.print(" - ");
                            System.out.println(f2);
                            curva.addPonto(  (float)f1,  (float)f2);
                        }
                    }
                }

                /*curva.addPonto( 30f, 400f);
                curva.addPonto(240f, 300f);
                curva.addPonto(720f,   0f);*/
                env.setCURVA(curva);

                instrumentoA.setEnvoltoria(env);
                instrumentoA.setFrequencia(frequencia);
                instrumentoA.setLambda(lambda);
                instrumentoA.setFase(fase);
                instrumentoA.setGanho(ganho);

                instrumentoA.reset();
                m2.setAndamento(andamento);

                Som som = m2.getSom(instrumentoA);
                som.setNome("teste2A");
                som.visualiza();

            }
        });
        Button2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateData();
                Envoltoria   env    =   new Envoltoria(); ;
                Envoltoria   envFREQUENCIACORTE;
                Curva        curva  = new Curva(720);
                UnidadeH		UH1 =   new UnidadeH();
                UnidadeH		UH2 =   new UnidadeH();

                for (int i=0;i<14;i += 2){

                    String str1 = curvaPontos[i].getText();
                    String str2 = curvaPontos[i+1].getText();
                    if (!str1.equals("") && !str2.equals("")) {
                        double f1 = Double.parseDouble(str1);
                        double f2 = Double.parseDouble(str2);
                        if (f1 > 0 && f2 > 0){
                                System.out.print("Ponto: ");
                            System.out.print(f1);
                            System.out.print(" - ");
                            System.out.println(f2);
                            curva.addPonto(  (float)f1,  (float)f2);
                        }
                    }
                }
                env.setCURVA(curva);
                instrumentoB.setEnvoltoria(env);
                instrumentoB.setFrequencia(frequencia);
                instrumentoB.setLambda(lambda);
                instrumentoB.setFase(fase);
                instrumentoB.setGanho(ganho);

                instrumentoB.reset();

                m2.setAndamento(andamento);
                Som som = m2.getSom(instrumentoB);
                som.setNome("teste2B");
                som.visualiza();

            }
        });
        Button3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateData();
                Envoltoria   env    =   new Envoltoria(); ;
                Envoltoria   envFREQUENCIACORTE;
                Curva        curva  = new Curva(720);
                UnidadeH		UH1 =   new UnidadeH();
                UnidadeH		UH2 =   new UnidadeH();

                for (int i=0;i<14;i += 2){

                    String str1 = curvaPontos[i].getText();
                    String str2 = curvaPontos[i+1].getText();
                    if (!str1.equals("") && !str2.equals("")) {
                        double f1 = Double.parseDouble(str1);
                        double f2 = Double.parseDouble(str2);
                        if (f1 > 0 && f2 > 0){
                            System.out.print("Ponto: ");
                            System.out.print(f1);
                            System.out.print(" - ");
                            System.out.println(f2);
                            curva.addPonto(  (float)f1,  (float)f2);
                        }
                    }
                }

                env.setCURVA(curva);
                instrumentoC.setEnvoltoria(env);
                instrumentoC.setFrequencia(frequencia);
                instrumentoC.setLambda(lambda);
                instrumentoC.setFase(fase);
                instrumentoC.setGanho(ganho);

                instrumentoC.reset();

                m2.setAndamento(andamento);
                Som som = m2.getSom(instrumentoC);
                som.setNome("teste2C");
                som.visualiza();

            }
        });

        setSize(640,480);
        setLocation(640,480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void run(){
        while(true){
        }
    }
    public void updateData(){
        String strTextValue = frequenciaGet.getText();
        if (!strTextValue.equals("")) {
            double dValue = Double.parseDouble(strTextValue);
            frequencia = (float)dValue;
            System.out.print("Frequencia = ");
            System.out.println(frequencia);
        }
        strTextValue = lambdaGet.getText();
        if (!strTextValue.equals("")) {
            double dValue = Double.parseDouble(strTextValue);
            lambda = (float)dValue;
            System.out.print("Lambda = ");
            System.out.println(lambda);
        }
        strTextValue = faseGet.getText();
        if (!strTextValue.equals("")) {
            double dValue = Double.parseDouble(strTextValue);
            fase = (float)dValue;
            System.out.print("Fase = ");
            System.out.println(fase);
        }
        strTextValue = ganhoGet.getText();
        if (!strTextValue.equals("")) {
            double dValue = Double.parseDouble(strTextValue);
            ganho = (float)dValue;
            System.out.print("ganho = ");
            System.out.println(ganho);
        }
        strTextValue = andamentoGet.getText();
        if (!strTextValue.equals("")) {
            double dValue = Double.parseDouble(strTextValue);
            andamento = (float)dValue;
            System.out.print("andamento = ");
            System.out.println(andamento);
        }

    }

    public static void main(String[] args)
    {
        Thread thread  = new Thread(new T2());
        thread.start();

	}
}

class InstrumentoT2A extends Dispositivo
{
    private boolean    canal;
    private float      lambda;
    private float      ummenoslambda;
    private float      fase;
    private float      ganho = 1;

    private Envoltoria envAMPLITUDE;
    private Ruido      geradorRUIDO;   //--unidade de Ruído
    private Oscilador  oscSENOIDAL;    //--unidade Senoidal

    private float fatorCorte;



    public InstrumentoT2A(float fc)
    { this(new Ruido(), new Envoltoria(), fc);
    }


    public InstrumentoT2A(Ruido ruido_, Envoltoria env_, float fc_)
    {
        super();
        fatorCorte = fc_;

        this.geradorRUIDO    = ruido_;
        this.envAMPLITUDE    = env_;

        this.oscSENOIDAL = new Oscilador();
        this.setRelogio(0);
    }


    public void relogio()
    { oscSENOIDAL.relogio();
    }


    public void setRelogio(long n)
    {
      envAMPLITUDE.setRelogio(n);
      oscSENOIDAL.setRelogio(n);
      geradorRUIDO.setRelogio(n);
      this.saida = oscSENOIDAL.getSaida()*((canal) ? ummenoslambda : lambda);
      canal = !canal;
      reset();
    }


    public float getSaida()
    {
      this.saida = oscSENOIDAL.getSaida()*((canal) ? ummenoslambda : lambda);
      canal      = !canal;
      return ganho*this.saida;
    }


    public void reset()
    {
        envAMPLITUDE.setDuracao(duracao);
        envAMPLITUDE.reset();

        geradorRUIDO.setDispositivoAmplitude(envAMPLITUDE);
        geradorRUIDO.setFrequencia((float)frequencia*fatorCorte);
        geradorRUIDO.setFase(fase);
        geradorRUIDO.setDuracao(duracao);
        geradorRUIDO.reset();

        oscSENOIDAL.setDispositivoAmplitude(geradorRUIDO);
        oscSENOIDAL.setFrequencia((float)frequencia);
        oscSENOIDAL.setFase(fase);
        oscSENOIDAL.setDuracao(duracao);
        oscSENOIDAL.reset();
    }



    public void setGanho(float g)
    {this.ganho = g;

    }

    public void setDuracao(float d)
    {   this.duracao = d;
        envAMPLITUDE.setDuracao(d);
        geradorRUIDO.setDuracao(d);
        oscSENOIDAL.setDuracao(d);
        reset();
    }


    public void setFrequencia(float freq)
    { this.frequencia = freq;
      reset();
    }

    public void setLambda(double lambda_)
    { this.lambda = (float)lambda_;
      this.ummenoslambda = 1 - this.lambda;
      reset();
    }

    public void setEnvoltoria(Envoltoria envAMPLITUDE)
    { this.envAMPLITUDE = envAMPLITUDE;
      reset();
    }

    public void setFase(float fase)
    { this.fase = fase;
      reset();
    }

}



class InstrumentoT2B extends Dispositivo
{
    private boolean    canal;
    private float      lambda;
    private float      ummenoslambda;
    private float      fase;
    private float      ganho = 1;

    private Envoltoria envAMPLITUDE;
    private Ruido      geradorRUIDO;   //--unidade de Ruído
    private Oscilador  oscSENOIDAL,oscSENOIDAL2;    //--unidade Senoidal
    private Somador    somador;
    private Dispositivo m_dispositivo;
    private float fatorCorte;



    public InstrumentoT2B(float fc)
    { this(new Ruido(), new Envoltoria(), fc);
    }


    public InstrumentoT2B(Ruido ruido_, Envoltoria env_, float fc_)
    {
        super();
        fatorCorte = fc_;

        this.geradorRUIDO    = ruido_;
        this.envAMPLITUDE    = env_;
        this.oscSENOIDAL = new Oscilador();
        this.oscSENOIDAL2 = new Oscilador();
        this.m_dispositivo = new Dispositivo();
        m_dispositivo.setFrequencia(40);

        this.setRelogio(0);
    }


    public void relogio()
    { oscSENOIDAL.relogio();
        oscSENOIDAL2.relogio();
    }


    public void setRelogio(long n)
    {
      envAMPLITUDE.setRelogio(n);
      oscSENOIDAL.setRelogio(n);
      oscSENOIDAL2.setRelogio(n);
      geradorRUIDO.setRelogio(n);
      m_dispositivo.setRelogio(n);
      this.saida = oscSENOIDAL.getSaida()*((canal) ? ummenoslambda : lambda);
      canal = !canal;
      reset();
    }


    public float getSaida()
    {
      this.saida = oscSENOIDAL.getSaida()*((canal) ? ummenoslambda : lambda);
      canal      = !canal;
      return ganho*this.saida;
    }


    public void reset()
    {
        envAMPLITUDE.setDuracao(duracao);
        envAMPLITUDE.reset();


        geradorRUIDO.setDispositivoAmplitude(envAMPLITUDE);
        geradorRUIDO.setFrequencia((float)frequencia*fatorCorte);
        geradorRUIDO.setFase(fase);
        geradorRUIDO.setDuracao(duracao);
        geradorRUIDO.reset();

        oscSENOIDAL2.setDispositivoAmplitude(envAMPLITUDE);
        oscSENOIDAL2.setFrequencia((float)frequencia);
        oscSENOIDAL2.setFase(fase);
        oscSENOIDAL2.setDuracao(duracao);

        somador = new Somador(oscSENOIDAL2, geradorRUIDO);
        somador.setDuracao(duracao);
        somador.setFrequencia((float)frequencia*fatorCorte);

        oscSENOIDAL.setDispositivoAmplitude(somador);
        oscSENOIDAL.setFrequencia((float)frequencia);
        oscSENOIDAL.setFase(fase);
        oscSENOIDAL.setDuracao(duracao);
        oscSENOIDAL.reset();
    }



    public void setGanho(float g)
    {this.ganho = g;

    }

    public void setDuracao(float d)
    {   this.duracao = d;
        envAMPLITUDE.setDuracao(d);
        geradorRUIDO.setDuracao(d);
        oscSENOIDAL.setDuracao(d);
        oscSENOIDAL2.setDuracao(d);
        reset();
    }


    public void setFrequencia(float freq)
    { this.frequencia = freq;
      reset();
    }

    public void setLambda(double lambda_)
    { this.lambda = (float)lambda_;
      this.ummenoslambda = 1 - this.lambda;
      reset();
    }

    public void setEnvoltoria(Envoltoria envAMPLITUDE)
    { this.envAMPLITUDE = envAMPLITUDE;
      reset();
    }

    public void setFase(float fase)
    { this.fase = fase;
      reset();
    }


	public Somador getSomador() {
		return somador;
	}


	public void setSomador(Somador somador) {
		this.somador = somador;
	}

}


class InstrumentoT2C extends Dispositivo
{
    private boolean    canal;
    private float      lambda;
    private float      ummenoslambda;
    private float      fase;
    private float      ganho = 1;

    private Envoltoria envAMPLITUDE;
    private Ruido      geradorRUIDO;   //--unidade de Ruído
    private Oscilador  oscSENOIDAL, oscSENOIDAL2, oscSENOIDAL3;    //--unidade Senoidal
    private Somador    somador,somador2;
    private float fatorCorte;



    public InstrumentoT2C(float fc)
    { this(new Ruido(), new Envoltoria(), fc);
    }


    public InstrumentoT2C(Ruido ruido_, Envoltoria env_, float fc_)
    {
        super();
        fatorCorte = fc_;

        this.geradorRUIDO    = ruido_;
        this.envAMPLITUDE    = env_;
        this.oscSENOIDAL = new Oscilador();
        this.oscSENOIDAL2 = new Oscilador();
        this.oscSENOIDAL3 = new Oscilador();
        this.somador= new Somador(geradorRUIDO, oscSENOIDAL3);
        this.somador2= new Somador(somador, envAMPLITUDE);
        /*dispositivos que correspondem às entradas do oscilador na figura do instrumento c */
        this.setRelogio(0);
    }


    public void relogio()
    { oscSENOIDAL.relogio();
      oscSENOIDAL2.relogio();
    }


    public void setRelogio(long n)
    {
      envAMPLITUDE.setRelogio(n);
      oscSENOIDAL.setRelogio(n);
      oscSENOIDAL2.setRelogio(n);
      geradorRUIDO.setRelogio(n);
      somador.setRelogio(n);
      somador2.setRelogio(n);

      this.saida = oscSENOIDAL.getSaida()*((canal) ? ummenoslambda : lambda);
      canal = !canal;
      reset();
    }


    public float getSaida()
    {
      this.saida = oscSENOIDAL.getSaida()*((canal) ? ummenoslambda : lambda);
      canal      = !canal;
      return ganho*this.saida;
    }


    public void reset()
    {
        //Envoltoria
        envAMPLITUDE.setDuracao(duracao);
        envAMPLITUDE.reset();

        //Gerador ruido
        geradorRUIDO.setDispositivoAmplitude(envAMPLITUDE);
        geradorRUIDO.setFrequencia((float)frequencia*fatorCorte);
        geradorRUIDO.setFase(fase);
        geradorRUIDO.setDuracao(duracao);
        geradorRUIDO.reset();

        oscSENOIDAL3.setDispositivoAmplitude(envAMPLITUDE);
        oscSENOIDAL3.setFrequencia((float)frequencia);
        oscSENOIDAL3.setFase(fase);
        oscSENOIDAL3.setDuracao(duracao);
        oscSENOIDAL3.reset();

        somador = new Somador(geradorRUIDO, oscSENOIDAL3);
        somador.setDuracao(duracao);
        somador.setFrequencia((float)frequencia);
        somador.reset();

        somador2 = new Somador(somador, envAMPLITUDE);
        somador2.setDuracao(duracao);
        somador2.setFrequencia((float)frequencia);
        somador2.reset();

        oscSENOIDAL.setDispositivoAmplitude(envAMPLITUDE);
        oscSENOIDAL.setDispositivoFrequencia(somador2);
        oscSENOIDAL.setFrequencia((float)frequencia);
        oscSENOIDAL.setFase(fase);
        oscSENOIDAL.setDuracao(duracao);
        oscSENOIDAL.reset();


        //O oscilador mais de baixo F3

        //O oscilador da direira F1 é a envoltoria.
    }



    public void setGanho(float g)
    {this.ganho = g;

    }

    public void setDuracao(float d)
    {   this.duracao = d;
        envAMPLITUDE.setDuracao(d);
        geradorRUIDO.setDuracao(d);
        oscSENOIDAL.setDuracao(d);
        reset();
    }


    public void setFrequencia(float freq)
    { this.frequencia = freq;
      reset();
    }

    public void setLambda(double lambda_)
    { this.lambda = (float)lambda_;
      this.ummenoslambda = 1 - this.lambda;
      reset();
    }

    public void setEnvoltoria(Envoltoria envAMPLITUDE)
    { this.envAMPLITUDE = envAMPLITUDE;
      reset();
    }

    public void setFase(float fase)
    { this.fase = fase;
      reset();
    }


	public Somador getSomador() {
		return somador;
	}


	public void setSomador(Somador somador) {
		this.somador = somador;
	}

}




