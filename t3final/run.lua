local str = [=[
//Esse aqui é o mosso que vai tocar coisas c:

import sintese.*;
import java.util.ArrayList;
import java.util.Iterator;

public class LeTocador
{
	%s

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

]=]


--os.execute("javac MidiParser.java")
require( "iuplua" )

filedlg = iup.filedlg{dialogtype = "LOAD", title = "Load midi",
                      filter = "*.mid", filterinfo = "Midi files",
                      directory=""}
filedlg:popup (iup.ANYWHERE, iup.ANYWHERE)


if filedlg.status == '0' then



	function os.capture(cmd, raw)
	  local f = assert(io.popen(cmd, 'r'))
	  local s = assert(f:read('*a'))
	  f:close()
	  if raw then return s end
	  s = string.gsub(s, '^%s+', '')
	  s = string.gsub(s, '%s+$', '')
	  --s = string.gsub(s, '[\n\r]+', ' ')
	  return s
	end
	local s = os.capture("java MidiParser \""..filedlg.value.."\"");
	str = str:format(s)


	local file = io.open("LeTocador.java",'w')
	file:write(str)
	file:close();

	os.execute("javac -cp sintese.jar LeTocador.java")
	os.execute("java -cp .;sintese.jar LeTocador")
	end
