package de.unitrier.st.fst17.sdraytracer;

import de.unitrier.st.fst17.sdraytracer.Datatypes.RGB;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Renderer {

    private SDRaytracer rayTracer;

    private int maxRec;
    private int rayPerPixel = 1;

    private float fovx;
    private float fovy;

    private double tan_fovx;
    private double tan_fovy;

    private int y_angle_factor = 4;
    private int x_angle_factor = -4;

    private Future[] futureList;
    private int nrOfProcessors = Runtime.getRuntime().availableProcessors();
    private ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

    private RGB[][] image;

    Renderer(SDRaytracer rayTracer, int maxRec, float fovx, float fovy) {
        this.rayTracer = rayTracer;
        this.maxRec = maxRec;
        this.fovx = fovx;
        this.fovy = fovy;

        futureList = new Future[rayTracer.getWidth()];

        image = new RGB[rayTracer.getWidth()][rayTracer.getHeight()];
    }

    void profileRenderImage() {
        long end, start, time;

        renderImage(); // initialisiere Datenstrukturen, erster Lauf verfälscht sonst Messungen

        for (int procs = 1; procs < 6; procs++) {

            maxRec = procs - 1;
            System.out.print(procs);
            for (int i = 0; i < 10; i++) {
                start = System.currentTimeMillis();

                renderImage();

                end = System.currentTimeMillis();
                time = end - start;
                System.out.print(";" + time);
            }
            System.out.println("");
        }
    }

    void renderImage() {
        tan_fovx = Math.tan(fovx);
        tan_fovy = Math.tan(fovy);
        for (int i = 0; i < rayTracer.getWidth(); i++) {
            futureList[i] = (Future) eservice.submit(new RaytraceTask(rayTracer, i));
        }

        for (int i = 0; i < rayTracer.getWidth(); i++) {
            try {
                RGB[] col = (RGB[]) futureList[i].get();
                for (int j = 0; j < rayTracer.getHeight(); j++)
                    image[i][j] = col[j];
            } catch (InterruptedException | ExecutionException e) {
            }
        }
    }

    public int getMaxRec() {
        return maxRec;
    }

    public int getNrOfProcessors() {
        return nrOfProcessors;
    }

    public float getFovx() {
        return fovx;
    }

    public float getFovy() {
        return fovy;
    }

    public double getTan_fovx() {
        return tan_fovx;
    }

    public double getTan_fovy() {
        return tan_fovy;
    }

    public int getRayPerPixel() {
        return rayPerPixel;
    }

    public RGB[][] getImage() {
        return image;
    }

    public int getY_angle_factor() {
        return y_angle_factor;
    }

    public void setY_angle_factor(int y_angle_factor) {
        this.y_angle_factor = y_angle_factor;
    }

    public int getX_angle_factor() {
        return x_angle_factor;
    }

    public void setX_angle_factor(int x_angle_factor) {
        this.x_angle_factor = x_angle_factor;
    }
}
