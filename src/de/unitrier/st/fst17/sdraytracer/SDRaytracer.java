package de.unitrier.st.fst17.sdraytracer;

import de.unitrier.st.fst17.sdraytracer.Datatypes.*;
import de.unitrier.st.fst17.sdraytracer.Scenes.AScene;
import de.unitrier.st.fst17.sdraytracer.Scenes.Scene1;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* Implementation of a very simple Raytracer
   Stephan Diehl, Universität Trier, 2010-2016
*/


public class SDRaytracer extends JFrame {
    private static final long serialVersionUID = 1L;
    private boolean profiling = false;
    private int width = 1000;
    private int height = 1000;

    private AScene scene;
    private Renderer renderer;

    public static void main(String argv[]) {
        long start = System.currentTimeMillis();
        SDRaytracer sdr = new SDRaytracer();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("time: " + time + " ms");
        System.out.println("nrprocs=" + sdr.getRenderer().getNrOfProcessors());
    }



    private SDRaytracer() {

        renderer = new Renderer(this,3,(float) 0.628,(float) 0.628);
        scene = new Scene1(this);

        scene.createScene();

        if (!profiling) renderer.renderImage();
        else renderer.profileRenderImage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel area = new JPanel() {
            public void paint(Graphics g) {
                System.out.println("fovx=" + renderer.getFovx() + ", fovy=" + renderer.getFovy() + ", xangle=" + renderer.getX_angle_factor() + ", yangle=" + renderer.getY_angle_factor());
                if (renderer.getImage() == null) return;
                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++) {
                        g.setColor(renderer.getImage()[i][j].color());
                        // zeichne einzelnen Pixel
                        g.drawLine(i, height - j, i, height - j);
                    }
            }
        };

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                boolean redraw = false;
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    renderer.setX_angle_factor(renderer.getX_angle_factor()-1);
                    //mainLight.position.y-=10;
                    //fovx=fovx+0.1f;
                    //fovy=fovx;
                    //maxRec--; if (maxRec<0) maxRec=0;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    renderer.setX_angle_factor(renderer.getX_angle_factor()+1);
                    //mainLight.position.y+=10;
                    //fovx=fovx-0.1f;
                    //fovy=fovx;
                    //maxRec++;if (maxRec>10) return;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    renderer.setY_angle_factor(renderer.getY_angle_factor()-1);
                    //mainLight.position.x-=10;
                    //startX-=10;
                    //fovx=fovx+0.1f;
                    //fovy=fovx;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    renderer.setY_angle_factor(renderer.getY_angle_factor()+1);
                    //mainLight.position.x+=10;
                    //startX+=10;
                    //fovx=fovx-0.1f;
                    //fovy=fovx;
                    redraw = true;
                }
                if (redraw) {
                    scene.createScene();
                    renderer.renderImage();
                    repaint();
                }
            }
        });

        area.setPreferredSize(new Dimension(width, height));
        contentPane.add(area);
        this.pack();
        this.setVisible(true);
    }

    public AScene getScene() {
        return scene;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}


