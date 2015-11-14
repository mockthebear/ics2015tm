package main;
import sintese.Dispositivo;
import sintese.Envoltoria;
import sintese.Oscilador;
import sintese.Ruido;
import sintese.Somador;

/**
 * Constroi um instrumento (formante espectral básico) para o Trabalho T2, item 'a'.
 *
 * @since 02/06/2015
*/

public class InstrumentoT2C extends Dispositivo
{
    private boolean    canal;
    private float      lambda;
    private float      ummenoslambda;
    private float      fase;
    private float      ganho = 1;
    
    private Envoltoria envAMPLITUDE;
    private Ruido      geradorRUIDO;   //--unidade de Ruído
    private Oscilador  oscSENOIDAL, oscSENOIDAL2;    //--unidade Senoidal
    private Somador    somador;
    private Dispositivo P8, VI;
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
        /*dispositivos que correspondem às entradas do oscilador na figura do instrumento c */
        this.P8 = new Dispositivo();
        P8.setFrequencia(40);
        this.VI = new Dispositivo();
        VI.setFrequencia(80);
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
      P8.setRelogio(n);
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
        /*amplitude setada na raca, sem dó nem piedade. Precisa confirmar se pode fazer assim */
        oscSENOIDAL2.setDispositivoAmplitude(this.P8,this.VI);
        

        geradorRUIDO.setDispositivoAmplitude(envAMPLITUDE);
        geradorRUIDO.setFrequencia((float)frequencia*fatorCorte);
        geradorRUIDO.setFase(fase);
        geradorRUIDO.setDuracao(duracao);
        geradorRUIDO.reset();
        somador = new Somador(this.T8, geradorRUIDO);
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