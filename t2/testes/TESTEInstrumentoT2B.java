package main;
import sintese.Curva;
import sintese.Envoltoria;
import sintese.Melodia;
import sintese.Som;
import sintese.Tema;
import sintese.UnidadeH;
import sintese.*;

public class TESTEInstrumentoT2B
{  
   InstrumentoT2B instrumentoB;
   Envoltoria   env;   
   Envoltoria   envFREQUENCIACORTE;   
   Curva        curva;  
   UnidadeH		UH1, UH2;
   
   
   public TESTEInstrumentoT2B()
   { 
     //instrumentoA  = new InstrumentoT2A(1);        //---instrumento atonal
     instrumentoB  = new InstrumentoT2B(0.2f);     //---quase tonal
     //instrumentoA  = new InstrumentoT2A(0.065f);   //---atonal
     //instrumentoA  = new InstrumentoT2A(0.001f);   //---atonal ruidoso
     //instrumentoA  = new InstrumentoT2A(0.0001f);  //---tonal: formante estreita
     //instrumentoB  = new InstrumentoT2B(0.00001f); //---tonal: som puro
     UH1 = new UnidadeH();
     UH2 = new UnidadeH();
     env   = new Envoltoria(); 
     curva = new Curva(720);     
     
     curva.addPonto(  0f,   0f);
     curva.addPonto( 30f, 400f);
     curva.addPonto(240f, 300f);
     curva.addPonto(720f,   0f);

     env.setCURVA(curva);
           
     instrumentoB.setEnvoltoria(env);
     instrumentoB.setLambda(0.5f);
     instrumentoB.setFase(0f);
     instrumentoB.setGanho(103);     
     
        
     Melodia m2 = Tema.tema_aa_fuga1();
     //Melodia m2 = Tema.tema_aa_drawing_quintet_flauta();
     //Melodia m2 = Tema.tema_duda_no_frevo_eq();
     
     m2.getAutor();     
     
     //m2.setAndamento(0.45f);
     m2.setAndamento(0.95f);

     Som som = m2.getSom(instrumentoB);     
     som.setNome("teste2B");   
     som.visualiza();

     try{ System.in.read();
          System.exit(0);
        }
     catch(Exception e){};          
     
   }      
      
   public static void main(String args[])
   { new TESTEInstrumentoT2B();
   }   

}
