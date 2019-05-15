import java.util.Comparator;


public class Animation {

   private String n_Animation;
   private int f_Width;
   private int f_Height;
   private int numberOfFrames;
   private int delay;
  Animation(String s, int width, int height, int frames, int delay){
       n_Animation = s;
       f_Height = height;
       f_Width = width;
       numberOfFrames = frames;
       this.delay = delay;
   }
   public String getN_Animation(){
        return n_Animation;
    }

    public int getF_Width(){
        return f_Width;
    }

    public int getF_Height(){
        return f_Height;
    }

    public int getNumberOfFrames(){
        return numberOfFrames;
    }

    public int getDelay(){
        return delay;
    }
    public  String toString(){
       return n_Animation;
    }
}
class AnimationComparator implements Comparator<Animation>{
    public int compare(Animation a, Animation b){
        return a.getN_Animation().compareTo(b.getN_Animation());
    }
}