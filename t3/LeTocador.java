//Esse aqui é o mosso que vai tocar coisas c:

import sintese.*;

public class LeTocador
{
/*
    Descrição da matriz:
    [Id do instrumento],[Note on\off][Tick no qual foi tocada][speed][Key]
*/
    double bpm = 62.499996;
    long duration = 12;
    int Notes[][] = {

  {1,1,0,80,58},
  {1,1,600,80,48},
  {1,1,1028,80,64},
  {1,1,1200,80,47},
  {1,1,1457,80,52},
  {2,1,1628,80,57},
  {2,1,1800,80,52},
  {2,1,2057,80,57},
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



    Dispositivo disp[] = {

        BancoDeInstrumentos.sompuro(0.5f),
        BancoDeInstrumentos.marimba_i51(1f),
        BancoDeInstrumentos.trompete01(20.5f),

        BancoDeInstrumentos.timbreortogonal3(1f),




    };

    Melodia mainlodia[] = {
        new Melodia(), new Melodia(), new Melodia(), new Melodia(),
        new Melodia(), new Melodia(), new Melodia(), new Melodia(),
        new Melodia(), new Melodia(), new Melodia(), new Melodia(),
        new Melodia(), new Melodia(), new Melodia(), new Melodia(),
    };
    boolean Used[] ={
        false,false,false,false,false,false,false,false,
        false,false,false,false,false,false,false,false,
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

	public void AddNote(int track,double duration,int freq)
    {

        double setDuration = (duration-lastNote[track])/500.0;
        lastNote[track] = duration;
        if (setDuration > 0){

            System.out.println(setDuration);
            mainlodia[track].addNota(new Nota(setDuration, (double)440.0f * Math.pow(2.0f ,(freq/12.0f)), 1.5));
        }
    }

	public LeTocador()
    {
        for (int i=0;i<16;i++){
            mainlodia[i].setBPM((int)bpm);
        }
        for (int i=0;i<totalSize-1;i++){
            if (Notes[i][1] == 1){
                Used[Notes[i][0]] = true;
                double dur =Notes[i][2];
                AddNote(Notes[i][0],dur,Notes[i][4]);
            }
        }
    }

    public void Play()
    {
        int bgn = 0;
        for (int i=0;i<16;i++){
            if (Used[i]){
                bgn = i;
                break;
            }
        }
        Som total = mainlodia[bgn].getSom(disp[bgn]);
        boolean first = true;
        for (int i=bgn+1;i<16;i++){
            if (Used[i]){

                total.emenda(mainlodia[i].getSom(disp[i]));
            }
        }
        total.visualiza();

    }


}
