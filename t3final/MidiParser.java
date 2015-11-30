/*
 Sim eu inclui tudo isso direto do tocador Xis!! c:
*/
import java.text.DecimalFormat;
import java.io.File;
import java.io.IOException;
import javax.sound.midi.*;

public class MidiParser
{
    //Main
    public static void main(String[] args)
    {
        MidiParser m = new MidiParser();
        m.OpenFile(args[0]);
	}
        private File MidiFile                   = null;
        //Estrutura do JavaSound
        private Sequencer  SequencerVar         = null;
        private Sequence   SequencePlayer;
        private Receiver   receptor             = null;
        static final int FORMULA_DE_COMPASSO = 0x58;

        static final int MENSAGEM_TEXTO = 0x01;
        static final int MENSAGEM_TONALIDADE = 0x59;


    public MidiParser(){


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

        System.out.println("    double bpm = "+bpm+";");
        System.out.println("    long duration = "+duracao+";");
        System.out.println("    int Notes[][] = {\n");

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


                try{
                    stx =  getTexto(trilha);
                }
                catch(Exception e){}


	             //modeltrilhas.addColumn(""+i+":"+st);

	             F_Line[i+1] = stx;
	             if(fc!=null)
                    F_Line[i+1] = F_Line[i+1]+""+fc.getX() +":"+ (int)(Math.pow(2, fc.getY()))+"";

                int sizee = 0;
                long lastTick = -1;
                for(int j=0; j<trilha.size(); j++)
                {
                    MidiEvent   e          = trilha.get(j);
                    MidiMessage mensagem   = e.getMessage();
                    long        tick      = e.getTick();
                    int statusNmbr = mensagem.getStatus();
                    int actualTrack = statusNmbr & 0b00001111;
                    statusNmbr = statusNmbr & 0b11110000;
                    MidiMessage message = e.getMessage();
                    if (tick != lastTick){ //Isso é cheat.

                        if (message instanceof ShortMessage) {
                            ShortMessage sm = (ShortMessage) message;
                            //byte msgNmbr[] = mensagem.getMessage();

                            if (sm.getCommand() == 0x90){ //Note on
                                lastTick = tick;
                                int key = sm.getData1();
                                int velocity = sm.getData2();
                                int kk = 0;
                                if (sm.getCommand() == 0x90)
                                    kk = 1;
                                String nomecomando = "  {"+i+","+kk+","+tick+","+velocity+","+key+"},";
                                System.out.println(nomecomando);
                                sizee++;
                            }
                        }
                    }




                }
                if (sizee > 0){
                    System.out.println("\n};\n    int totalSize = "+sizee+";");
                }
            }


    }

	public void OpenFile(String file){
            try {
                SequencePlayer = MidiSystem.getSequence(new File(file));
                //SequencePlayer = MidiSystem.getSequence(new File("mvioloncelo.mid"));
                //SequencePlayer = MidiSystem.getSequence(new File("beethoven_ode_to_joy.mid"));
                SequencerVar = MidiSystem.getSequencer();
                SequencerVar.setSequence(SequencePlayer);
                SequencerVar.open();
                SequencerVar.getTransmitter().setReceiver(receptor);
                PrintData();
                System.exit(1);
            }
            catch(InvalidMidiDataException e2) { System.out.println(e2+" : Erro nos dados midi."); }
            catch(IOException              e3) { System.out.println(e3+" : O arquivo midi nao foi encontrado.");   }
            catch(MidiUnavailableException e1) { System.out.println(e1+" : Dispositivo midi nao disponivel.");}

	}


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
    static String getTonalidade(Track trilha)
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
                    case -7: stonalidade = "Dob Maior"; break;
                    case -6: stonalidade = "Solb Maior"; break;
                    case -5: stonalidade = "Reb Maior"; break;
                    case -4: stonalidade = "L1b Maior"; break;
                    case -3: stonalidade = "Mib Maior"; break;
                    case -2: stonalidade = "Sib Maior"; break;
                    case -1: stonalidade = "Fa Maior"; break;
                    case  0: stonalidade = "Dó Maior"; break;
                    case  1: stonalidade = "Sol Maior"; break;
                    case  2: stonalidade = "Re Maior"; break;
                    case  3: stonalidade = "La Maior"; break;
                    case  4: stonalidade = "Mi Maior"; break;
                    case  5: stonalidade = "Si Maior"; break;
                    case  6: stonalidade = "Fa# Maior"; break;
                    case  7: stonalidade = "Do# Maior"; break;
                }
            }

            else if(smaior.equalsIgnoreCase("Menor"))
            {
                switch (tonalidade)
                {
                    case -7: stonalidade = "Lab Menor"; break;
                    case -6: stonalidade = "Mib Menor"; break;
                    case -5: stonalidade = "Sib Menor"; break;
                    case -4: stonalidade = "Fa Menor"; break;
                    case -3: stonalidade = "Do Menor"; break;
                    case -2: stonalidade = "Sol Menor"; break;
                    case -1: stonalidade = "Re Menor"; break;
                    case  0: stonalidade = "La Menor"; break;
                    case  1: stonalidade = "Mi Menor"; break;
                    case  2: stonalidade = "Si Menor"; break;
                    case  3: stonalidade = "Fa# Menor"; break;
                    case  4: stonalidade = "Do# Menor"; break;
                    case  5: stonalidade = "Sol# Menor"; break;
                    case  6: stonalidade = "Re# Menor"; break;
                    case  7: stonalidade = "La# Menor"; break;
                }
            }
         }
      }
      return stonalidade;
    }



}
