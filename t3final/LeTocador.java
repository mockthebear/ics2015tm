//Esse aqui é o mosso que vai tocar coisas c:

import sintese.*;
import java.util.ArrayList;
import java.util.Iterator;

public class LeTocador
{
	double bpm = 62.499996;
    long duration = 12;
    int Notes[][] = {

  {1,1,0,80,58},
  {1,1,600,80,48},
  {1,1,1028,80,64},
  {1,1,1200,80,47},
  {1,1,1457,80,52},
  {1,1,1628,80,57},
  {1,1,1800,80,52},
  {1,1,2057,80,57},
  {1,1,2228,80,60},
  {1,1,2400,80,58},
  {1,1,2485,80,58},
  {1,1,2657,80,56},
  {1,1,2828,80,50},
  {1,1,3000,80,49},
  {1,1,3085,80,54},
  {1,1,3257,80,54},
  {1,1,3428,80,62},
  {1,1,3514,80,63},
  {1,1,3600,80,48},
  {1,1,3685,80,55},
  {1,1,3857,80,48},
  {1,1,4028,80,51},
  {1,1,4114,80,74},
  {1,1,4200,80,54},
  {1,1,4285,80,47},
  {1,1,4457,80,53},
  {1,1,4542,80,51},
  {1,1,4628,80,51},
  {1,1,4714,80,64},
  {1,1,4800,80,49},
  {1,1,4885,80,51},
  {1,1,4971,80,58},
  {1,1,5057,80,55},
  {1,1,5142,80,47},
  {1,1,5228,80,52},
  {1,1,5314,80,56},
  {1,1,5400,80,50},
  {1,1,5485,80,50},
  {1,1,5571,80,55},
  {1,1,5657,80,58},
  {1,1,5742,80,52},
  {1,1,5914,80,51},

};
    int totalSize = 42;

    private class Data_Som
    {
        Data_Som(){};
        Som audio;
        int delay;
        double dur;
    };
    int notaCounter[] = {
        0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,
    };
    Data_Som notas[][] = new Data_Som[16][totalSize];


    Dispositivo disp[] = {

        BancoDeInstrumentos.marimba_i51(1f),
        BancoDeInstrumentos.flauta_nao_harmonica_tonal(0.9f),
        BancoDeInstrumentos.marimba_i51(0.9f),






    };

    int Used[] ={
        0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,
    };
    double lastNote[] = {
        0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
        0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
    };

    public static void main(String[] args)
    {
        LeTocador L = new LeTocador();
        L.Play();
	}

	public void AddNote(int track,double duration,int freq,int interval)
    {

        double setDuration =duration/500.0;
        lastNote[track] = duration;
        System.out.println(setDuration);
        Nota n = new Nota(setDuration, (double)440.0f * Math.pow(2.0f ,(freq/12.0f)), 1.5);
        notas[track][notaCounter[track]] = new Data_Som();
        notas[track][notaCounter[track]].audio = n.getSom(disp[track]);
        notas[track][notaCounter[track]].delay = interval;
        notas[track][notaCounter[track]].dur = setDuration;
        notaCounter[track]++;

    }

	public LeTocador()
    {
        for (int i=0;i<totalSize-1;i++){
            if (Notes[i][1] == 1){
                Used[Notes[i][0]] = 8;
                int nextDur = Notes[i+1][2];
                int increment = 1;
                while (nextDur == Notes[i][2]){
                    increment++;
                    nextDur = Notes[i+increment][2];
                }
                double dur =nextDur-Notes[i][2];
                AddNote(Notes[i][0],dur,Notes[i][4],Notes[i][2]);
            }
        }
    }

    public void Play()
    {
        Som total = notas[1][0].audio; //Sempre existe
        for (int i=0;i<16;i++){
            System.out.println(i);
            int D = Used[i];
            if (8 == D){
                Som local = notas[i][0].audio;
                for (int e=1;e<notaCounter[i];e++){
                    if (notas[i][e].dur <= 0.01){
                        local = local.mistura(notas[i][e].audio);
                        System.out.println(i+" :[M] "+ e);
                    }else{
                        local = local.emenda(notas[i][e].audio);
                        System.out.println(i+" :[E] "+ e);
                    }


                }
                total = total.mistura(local);
            }

        }
        total.visualiza();


    }


}

