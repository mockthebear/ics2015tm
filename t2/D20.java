import sintese.*;

class D20
{
   Curva      curva1, curva2;
   Oscilador  o1;
   Envoltoria env1 = new Envoltoria();
   Envoltoria env2 = new Envoltoria();


   public D20()
   {
     curva1 = new Curva(720);
     curva1.addPonto(0f, 0f);
     curva1.addPonto(10f, 1000f);
     curva1.addPonto(600f, 300f);
     curva1.addPonto(720f, 0f);

     curva2 = new Curva(720);
     curva2.addPonto(  0f, 330f);
     curva2.addPonto(720f, 330f);

     env1.setCURVA(curva1);
     env2.setCURVA(curva2);

     env1.setGanho(1f);
     env1.setGanho(2f);
     o1 = new Oscilador(env1, env2);

     Som som = new Som(o1, 1.5f, "simples");
     som.salvawave();
     som.tocawave();
   }

   public static void main(String args[])
   { new D20();
   }
}
