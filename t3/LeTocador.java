//Esse aqui � o mosso que vai tocar coisas c:

import sintese.*;
import java.util.ArrayList;
import java.util.Iterator;

public class LeTocador
{
/*
    Descri��o da matriz:
    [Id do instrumento],[Note on\off][Tick no qual foi tocada][speed][Key]
*/
      double bpm = 123.84073;
    long duration = 31;
    int Notes[][] = {

  {1,1,254,0,60},
  {1,1,256,76,64},
  {1,1,510,0,64},
  {1,1,512,79,65},
  {1,1,766,0,65},
  {2,1,766,0,65},
  {1,1,768,82,67},
  {1,1,974,0,48},
  {1,1,1022,0,67},
  {1,1,1024,79,67},
  {1,1,1278,0,67},
  {1,1,1280,70,65},
  {1,1,1486,0,55},
  {1,1,1534,0,65},
  {1,1,1536,69,64},
  {1,1,1790,0,64},
  {2,1,1790,0,64},
  {1,1,1792,69,62},
  {1,1,1998,0,55},
  {1,1,2046,0,62},
  {1,1,2048,73,60},
  {1,1,2302,0,60},
  {2,1,2302,0,60},
  {1,1,2304,74,60},
  {1,1,2558,0,60},
  {1,1,2560,78,62},
  {1,1,2814,0,62},
  {1,1,2816,83,64},
  {1,1,3022,0,48},
  {1,1,3070,0,64},
  {1,1,3072,78,64},
  {1,1,3453,0,64},
  {1,1,3456,69,62},
  {1,1,3534,0,55},
  {1,1,3583,0,62},
  {1,1,3584,74,59},
  {1,1,4046,0,55},
  {1,1,4091,0,59},
  {1,1,4096,85,60},
  {1,1,4350,0,60},
  {2,1,4352,76,64},
  {1,1,4352,76,64},
  {1,1,4606,0,64},
  {1,1,4608,80,65},
  {1,1,4862,0,65},
  {1,1,4864,79,67},
  {1,1,5070,0,48},
  {1,1,5118,0,67},
  {1,1,5120,78,67},
  {1,1,5374,0,67},
  {1,1,5376,70,65},
  {1,1,5582,0,55},
  {1,1,5630,0,65},
  {1,1,5632,72,64},
  {1,1,5886,0,64},
  {1,1,5888,70,62},
  {1,1,6094,0,55},
  {1,1,6142,0,62},
  {1,1,6144,72,60},
  {1,1,6398,0,60},
  {1,1,6400,73,60},
  {1,1,6654,0,60},
  {1,1,6656,78,62},
  {1,1,6910,0,62},
  {1,1,6912,83,64},
  {1,1,7118,0,48},
  {1,1,7166,0,64},
  {1,1,7168,74,62},
  {1,1,7549,0,62},
  {1,1,7552,71,60},
  {1,1,7630,0,55},
  {1,1,7679,0,60},
  {1,1,7680,77,60},
  {1,1,8142,0,48},
  {1,1,8187,0,60},
  {1,1,8192,85,62},
  {1,1,8446,0,62},
  {1,1,8448,75,62},
  {1,1,8702,0,62},
  {1,1,8704,80,64},
  {1,1,8958,0,64},
  {1,1,8960,67,60},
  {1,1,9166,0,55},
  {1,1,9214,0,60},
  {1,1,9216,81,62},
  {1,1,9470,0,62},
  {1,1,9472,81,64},
  {1,1,9599,0,64},
  {1,1,9600,81,65},
  {1,1,9678,0,55},
  {1,1,9727,0,65},
  {1,1,9728,75,64},
  {1,1,9982,0,64},
  {1,1,9984,67,60},
  {1,1,10190,0,55},
  {1,1,10238,0,60},
  {1,1,10240,81,62},
  {1,1,10494,0,62},
  {1,1,10496,79,64},
  {1,1,10623,0,64},
  {1,1,10624,79,65},
  {1,1,10702,0,55},
  {1,1,10751,0,65},
  {1,1,10752,75,64},
  {1,1,11006,0,64},
  {1,1,11008,70,62},
  {1,1,11214,0,56},
  {1,1,11262,0,62},
  {1,1,11264,71,60},
  {1,1,11495,0,57},
  {1,1,11518,0,60},
  {1,1,11520,79,62},
  {1,1,11751,0,54},
  {1,1,11774,0,62},
  {1,1,11776,51,55},
  {1,1,12032,84,64},
  {1,1,12238,0,55},
  {1,1,12288,55,60},
  {1,1,12542,0,64},
  {1,1,12544,97,64},
  {1,1,12750,0,60},
  {1,1,12798,0,64},
  {1,1,12800,100,65},
  {1,1,13054,0,65},
  {1,1,13056,103,67},
  {1,1,13262,0,58},
  {1,1,13310,0,67},
  {1,1,13312,101,67},
  {1,1,13566,0,67},
  {1,1,13568,92,65},
  {1,1,13774,0,57},
  {1,1,13822,0,65},
  {1,1,13824,89,64},
  {1,1,14078,0,64},
  {1,1,14080,88,62},
  {1,1,14286,0,53},
  {1,1,14334,0,62},
  {1,1,14336,93,60},
  {1,1,14567,0,52},
  {1,1,14590,0,60},
  {1,1,14592,97,60},
  {1,1,14823,0,52},
  {1,1,14846,0,60},
  {1,1,14848,100,62},
  {1,1,15079,0,55},
  {1,1,15102,0,62},
  {1,1,15104,103,64},
  {1,1,15335,0,60},
  {1,1,15358,0,64},
  {1,1,15360,94,62},
  {1,1,15741,0,62},
  {1,1,15744,90,60},
  {1,1,15822,0,55},
  {1,1,15871,0,60},
  {1,1,15872,93,60},
  {1,1,16334,0,48},
  {1,1,16379,0,60},

};
    int totalSize = 157;

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
