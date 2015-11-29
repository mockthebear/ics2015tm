//Esse aqui é o mosso que vai tocar coisas c:
public class AutoPlayer
{
    //Main

    float bpm = 62.499996;
    long duration = 12;

    int Notes[][] = {
        /*
            A matriz é apenas parâmetros
            {[instrumento],[Note on\off],[tick],[velocity],[key (note)]},
        */
        {1,0,0,16,58},
        {1,1,600,0,58},
        {1,0,600,16,48},
        {1,1,1028,0,48},
        {1,0,1028,16,64},
        {1,1,1200,0,64},
        {1,0,1200,16,47},
        {1,1,1457,0,47},
        {1,0,1457,16,52},
        {1,1,1628,0,52},
    };
    public static void main(String[] args)
    {
        Thread thread  = new Thread(new AutoPlayer());

        thread.start();
	}

	public SuperPlayer8000()
    {

    }

	public void run(){

	}


}
