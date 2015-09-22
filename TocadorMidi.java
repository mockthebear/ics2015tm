import javax.sound.midi.*;
   import java.io.*;

   public class TocadorMidi
   {

	  public static void main(String args[])
	  {

		 String nomearq = "03AllegroBrandenburgoN6.mid";

		 //String nomearq = "italiano.mid";
		 //String nomearq = "paisagemintervalar.mid";
		 //String nomearq = "mclarineta1.mid";
		 //String nomearq = "mpiano2.mid";

	    if(args.length>0) nomearq = args[0];

	    File      arqmidi = new File(nomearq);
	    Sequencer sequenciador;
	    Sequence  sequencia;

	    try
	    {
	      sequencia = MidiSystem.getSequence(arqmidi);

	      exibirDados(sequencia, nomearq);

	      //---preparar o sequenciador
	      sequenciador = MidiSystem.getSequencer();
	      sequenciador.setSequence(sequencia);
	      sequenciador.open();
	      retardo(500);
	      sequenciador.start();  //--aqui come�a a tocar.

	      //-- O la�o abaixo verifica a cada segundo se a execu��o j� foi conclu�da:
	      //-- a� ent�o o sequenciador ser� 'fechado';

              int i=0;
              System.out.println("Instante em segundos: ");

	      while(true)
	      {
               if(sequenciador.isRunning())
		{
                 retardo(1000);

                  //--exibi��o do instante real em segundos;
                  long  posicao = sequenciador.getMicrosecondPosition();
                  int   seg     = Math.round(posicao*0.000001f);
                  System.out.print(seg + " ");
                  i++;
                  if(i==20) { System.out.println("");
                              i=0;
                            }
                  //----------------------------------------
			}
			else break;
	      }

	      System.out.println("");
	      System.out.println("* * * \n");

         retardo(1000);
	      sequenciador.stop();
	      sequenciador.close();
	    }

	    catch(MidiUnavailableException e1) { System.out.println("Erro1: "+"Dispositivo midi n�o dispon�vel.");}
	    catch(InvalidMidiDataException e2) { System.out.println("Erro2: "+"Erro nos dados midi."); }
	    catch(IOException              e3) { System.out.println("Erro3: "+"O arquivo midi n�o foi encontrado.");
                                            System.out.println("Sintaxe: "+"java TocadorMidi arquivo.mid");
                                          }
	  }


          static void retardo(int miliseg)
          {
             try { Thread.sleep(miliseg);
                 }
	     catch(InterruptedException e) { }
          }

	  static void exibirDados(Sequence sequencia, String nome)
	  {   //---
	      long duracao     = sequencia.getMicrosecondLength()/1000000;
	      int  resolucao   = sequencia.getResolution();
	      long totaltiques = sequencia.getTickLength();

	      float durtique       = (float)duracao/totaltiques;
	      float durseminima    = durtique*resolucao;
	      float bpm            = 60/durseminima;
	      int   totalseminimas = (int)(duracao/durseminima);

	      System.out.println("");
	      System.out.println("------------------------------------------");
	      System.out.println("--------Arquivo Midi: " + nome + " ----");
	      System.out.println("------------------------------------------");
	      System.out.println("resolu��o            = "+resolucao+" tiques   (n�mero de divis�es da sem�nima)");
	      System.out.println("dura��o              = "+duracao+" s");
	      System.out.println("n�mero de tiques     = "+totaltiques+" ");
	      System.out.println("dura��o do tique     = "+durtique+" s");
	      System.out.println("dura��o da sem�nima  = "+durseminima+" s");
	      System.out.println("total de seminimas   = "+totalseminimas);
	      System.out.println("andamento            = "+Math.round(bpm)+ " bpm");
	      System.out.println("---");

	      System.out.println("\n\n\n");
	      System.out.println("------------------------------------------");
	      System.out.println("---Conte�do da sequ�ncia:-----------------");
	      System.out.println("--");




              //---------------------------------------------
              //--
              //--
              //-- C�digo para ler e decodificar o arquivo Midi:
              //--
              //---(significa percorrer a sequ�ncia)
              //--
              //--
              //---exibi��o dos dados de trilha:
              Track[] trilhas = sequencia.getTracks();

              for(int i=0; i<trilhas.length; i++)
              {
                System.out.println("In�cio da trilha n� " + i + " **********************");
	             System.out.println("------------------------------------------");
                Track trilha =  trilhas[i];

                Par    fc  =  null;
                String st  = "--";
                String stx = "--";

                //---MetaMensagem de f�rmula de compasso
                if(i==0) fc = getFormulaDeCompasso(trilha);

                //---MetaMensagem de tonalidade
                if(i==0)
                try{ st =  getTonalidade(trilha);
                   }
                catch(Exception e){}

                //---MetaMensagem de texto
                try{ stx =  getTexto(trilha);
                   }
                catch(Exception e){}

                if(fc!=null)
	             System.out.println("F�rmula de Compasso: " + fc.getX() +":"+ (int)(Math.pow(2, fc.getY())) );

                System.out.println("Tonalidade         : " + st);
	             System.out.println("Texto              : " + stx);
	             System.out.println("------------------------------------------");

                for(int j=0; j<trilha.size(); j++)
                {
                  System.out.println("Trilha n� " + i );
                  System.out.println("Evento n� " + j);
                  MidiEvent   e          = trilha.get(j);
                  MidiMessage mensagem   = e.getMessage();
                  long        tique      = e.getTick();

                  int n = mensagem.getStatus();

                  String nomecomando = ""+n;

                  switch(n)
                  {
                      case 128: nomecomando = "noteON"; break;
                      case 144: nomecomando = "noteOFF"; break;
                      case 255: nomecomando = "MetaMensagem  (a ser decodificada)"; break;
                      //---(introduzir outros casos)
                  }

                  System.out.println("       Mensagem: " + nomecomando );
                  System.out.println("       Instante: " + tique );
	               System.out.println("------------------------------------------");
                }
              }
	  }



    static final int FORMULA_DE_COMPASSO = 0x58;

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
                    case -7: stonalidade = "D�b Maior"; break;
                    case -6: stonalidade = "Solb Maior"; break;
                    case -5: stonalidade = "R�b Maior"; break;
                    case -4: stonalidade = "L�b Maior"; break;
                    case -3: stonalidade = "Mib Maior"; break;
                    case -2: stonalidade = "Sib Maior"; break;
                    case -1: stonalidade = "F� Maior"; break;
                    case  0: stonalidade = "D� Maior"; break;
                    case  1: stonalidade = "Sol Maior"; break;
                    case  2: stonalidade = "R� Maior"; break;
                    case  3: stonalidade = "L� Maior"; break;
                    case  4: stonalidade = "Mi Maior"; break;
                    case  5: stonalidade = "Si Maior"; break;
                    case  6: stonalidade = "F�# Maior"; break;
                    case  7: stonalidade = "D�# Maior"; break;
                }
            }

            else if(smaior.equalsIgnoreCase("Menor"))
            {
                switch (tonalidade)
                {
                    case -7: stonalidade = "L�b Menor"; break;
                    case -6: stonalidade = "Mib Menor"; break;
                    case -5: stonalidade = "Sib Menor"; break;
                    case -4: stonalidade = "F� Menor"; break;
                    case -3: stonalidade = "D� Menor"; break;
                    case -2: stonalidade = "Sol Menor"; break;
                    case -1: stonalidade = "R� Menor"; break;
                    case  0: stonalidade = "L� Menor"; break;
                    case  1: stonalidade = "Mi Menor"; break;
                    case  2: stonalidade = "Si Menor"; break;
                    case  3: stonalidade = "F�# Menor"; break;
                    case  4: stonalidade = "D�# Menor"; break;
                    case  5: stonalidade = "Sol# Menor"; break;
                    case  6: stonalidade = "R�# Menor"; break;
                    case  7: stonalidade = "L�# Menor"; break;
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

}  //-----fim da classe 'TocadorMidi'
