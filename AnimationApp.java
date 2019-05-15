import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;
public class AnimationApp extends JFrame implements ActionListener, ListSelectionListener {

    private static final long serialVersionUID = 1L;
    private JButton startButton = new JButton("Start");
    private JButton pauseButton = new JButton("Pause");
    private JButton stopButton = new JButton("Stop");
    JList<Animation> myList ;
    AnimationPanel ap = new AnimationPanel();

    public static void main(String[] args) {

        try {
           UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                                      
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | ClassNotFoundException
                | InstantiationException ex) {
            ex.printStackTrace();
        }
       UIManager.put("swing.boldMetal", Boolean.FALSE);
        javax.swing.SwingUtilities.invokeLater(() -> {
            AnimationApp an = new AnimationApp("Animation");
            an.createAndShowGUI();
            an.pack();
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) (dimension.getWidth() - an.getWidth())/2;
            int y = (int) (dimension.getHeight() - an.getHeight())/2;
            an.setLocation(x,y);

        });
    }

    private AnimationApp(String title) {
        super(title);
    }

    private void createAndShowGUI() {
        readAnimation();
        addComponentsToPane();
        myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myList.addListSelectionListener(this);
        startButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this );
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addComponentsToPane() {
        JPanel Panel1 = new JPanel(new BorderLayout());
        Panel1.setPreferredSize(new Dimension(600, 600));
        Panel1.setBackground(Color.BLACK);
        Panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Panel1.add(myList, BorderLayout.WEST);
        Panel1.add(ap, BorderLayout.CENTER);
        JPanel panel2 = new JPanel(new FlowLayout());
        panel2.add(startButton);
        panel2.add(pauseButton);
        pauseButton.setEnabled(false);
        panel2.add(stopButton);
        stopButton.setEnabled(false);
        Panel1.add(panel2, BorderLayout.SOUTH);
        add(Panel1, BorderLayout.CENTER);
    }
    private void readAnimation() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(".//Animations//animations.txt"));
            Vector<Animation> anim = new Vector<>();
            String line = br.readLine();
            while (line!= null){
                String [] data = line.split(":");
                String animationName = data[0].replace(' ', '_');
                int width = Integer.parseInt(data[1]);
                int height = Integer.parseInt(data[2]);
                int numberOfFrames = Integer.parseInt(data[3]);
                int delay = Integer.parseInt(data[4]);
                anim.add(new Animation(animationName, width, height, numberOfFrames, delay));
                line= br.readLine();
            }
            br.close();
            anim.sort(new AnimationComparator()); 
            myList = new JList<Animation>(anim);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(0);

        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            ap.startAnimation();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }

        if(e.getSource() == stopButton) {
            ap.stopAnimation();
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            pauseButton.setText("Pause");
        }
        if(e.getSource() == pauseButton){
            ap.pauseAnimation();
            pauseButton.setText("Resume");
        }

        if(e.getActionCommand() == "Resume"){
            ap.resumeAnimation();
            pauseButton.setText("Pause");
        }

    }
    public void valueChanged(ListSelectionEvent e){
        ap.stopAnimation();
        Animation selected = myList.getSelectedValue();
        ap.loadAnimation(selected);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        pauseButton.setText("Pause");
    }
}
