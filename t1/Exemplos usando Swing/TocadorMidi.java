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
	      sequenciador.start();  //--aqui começa a tocar.

	      //-- O laço abaixo verifica a cada segundo se a execução já foi concluída:
	      //-- aí então o sequenciador será 'fechado';

              int i=0;
              System.out.println("Instante em segundos: ");

	      while(true)
	      {
               if(sequenciador.isRunning())
		{
                 retardo(1000);

                  //--exibição do instante real em segundos;
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

	    catch(MidiUnavailableException e1) { System.out.println("Erro1: "+"Dispositivo midi não disponível.");}
	    catch(InvalidMidiDataException e2) { System.out.println("Erro2: "+"Erro nos dados midi."); }
	    catch(IOException              e3) { System.out.println("Erro3: "+"O arquivo midi não foi encontrado.");
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
	      System.out.println("resolução            = "+resolucao+" tiques   (número de divisões da semínima)");
	      System.out.println("duração              = "+duracao+" s");
	      System.out.println("número de tiques     = "+totaltiques+" ");
	      System.out.println("duração do tique     = "+durtique+" s");
	      System.out.println("duração da semínima  = "+durseminima+" s");
	      System.out.println("total de seminimas   = "+totalseminimas);
	      System.out.println("andamento            = "+Math.round(bpm)+ " bpm");
	      System.out.println("---");

	      System.out.println("\n\n\n");
	      System.out.println("------------------------------------------");
	      System.out.println("---Conteúdo da sequência:-----------------");
	      System.out.println("--");




              //---------------------------------------------
              //--
              //--
              //-- Código para ler e decodificar o arquivo Midi:
              //--
              //---(significa percorrer a sequência)
              //--
              //--
              //---exibição dos dados de trilha:
              Track[] trilhas = sequencia.getTracks();

              for(int i=0; i<trilhas.length; i++)
              {
                System.out.println("Início da trilha nº " + i + " **********************");
	             System.out.println("------------------------------------------");
                Track trilha =  trilhas[i];

                Par    fc  =  null;
                String st  = "--";
                String stx = "--";

                //---MetaMensagem de fórmula de compasso
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
	             System.out.println("Fórmula de Compasso: " + fc.getX() +":"+ (int)(Math.pow(2, fc.getY())) );

                System.out.println("Tonalidade         : " + st);
	             System.out.println("Texto              : " + stx);
	             System.out.println("------------------------------------------");

                for(int j=0; j<trilha.size(); j++)
                {
                  System.out.println("Trilha nº " + i );
                  System.out.println("Evento nº " + j);
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

}  //-----fim da classe 'TocadorMidi'
