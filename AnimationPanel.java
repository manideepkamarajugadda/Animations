import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;



public class AnimationPanel extends JPanel implements Runnable  {

    private static final long serialVersionUID = 1L;
    private volatile Thread  animationThread;
    boolean threadPaused = false;
    int startX,startY;
    int frameDelay;
    int currentFrameIndex=0;
    Image [] anim;
    MediaTracker tracker;
    public void loadAnimation(Animation a) {
        anim = new Image[a.getNumberOfFrames()];
        startX = (this.getWidth() - a.getF_Width()) / 2;
        startY = (this.getHeight() - a.getF_Height()) / 2;
        frameDelay = a.getDelay();
        tracker = new MediaTracker(this);
        String path = ".//Animations//" + a.getN_Animation();
        File folder = new File(path);
        File [] listOfFiles = folder.listFiles();
        int index =0;
        for(int i=0; i<listOfFiles.length; i++) {
            try{
                if (listOfFiles[i].getName().endsWith(".gif")){
                    anim[index] = ImageIO.read(listOfFiles[i]);
                    tracker.addImage(anim[index],1);
                    index++;
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            repaint();
        }

    }
    public void startAnimation(){
        currentFrameIndex = 0;
        threadPaused = false;
        animationThread = new Thread(this);
        animationThread.start();

    }
    public synchronized void pauseAnimation(){
        threadPaused = true;
    }


    public synchronized void resumeAnimation(){
        threadPaused = false;
        notify();
    }


    public synchronized void stopAnimation(){
        animationThread = null;
        notify();
        currentFrameIndex = 0;
    }
    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        if (anim!=null){
            if(currentFrameIndex == anim.length){
                currentFrameIndex =0;
            }
            g.drawImage(anim[currentFrameIndex],startX,startY,this);
            currentFrameIndex++;
        }
    }
    @Override
    public  void run(){

        try {
            tracker.waitForID(1);
        } catch (InterruptedException e) {
            return;
        }

        Thread thisThread = Thread.currentThread();
        while (animationThread == thisThread){
            try{
                Thread.sleep(frameDelay);
                synchronized (this){
                    while (threadPaused && animationThread == thisThread)   wait();
                }
            }catch ( InterruptedException e){
                System.out.println("Thread was interrupted");
            }
            repaint();

        }
    }




}


