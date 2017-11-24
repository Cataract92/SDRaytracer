package de.unitrier.st.fst17.sdraytracer.Scenes;

import de.unitrier.st.fst17.sdraytracer.Datatypes.*;
import de.unitrier.st.fst17.sdraytracer.SDRaytracer;

import java.util.ArrayList;

public class Scene1 extends AScene {
    public Scene1(SDRaytracer rayTracer) {
        super(rayTracer);

        triangles = new ArrayList<>();

        lights = new Light[]{
                new Light(new Vec3D(0,100,0), new RGB(0.1f,0.1f,0.1f)),
                new Light(new Vec3D(100,200,300), new RGB(0.5f,0,0.0f)),
                new Light(new Vec3D(-100,200,300), new RGB(0.0f,0,0.5f))
                //,new src.Light(new src.Vec3D(-100,0,0), new src.RGB(0.0f,0.8f,0.0f))
        };

        ambient_color = new RGB(0.01f,0.01f,0.01f);
        background_color = new RGB(0.05f,0.05f,0.05f);

    }


    @Override
    public void createScene() {

        triangles = new ArrayList<>();

        Triangle.addCube(triangles, 0, 35, 0, 10, 10, 10, new RGB(0.3f, 0, 0), 0.4f);       //rot, klein
        Triangle.addCube(triangles, -70, -20, -20, 20, 100, 100, new RGB(0f, 0, 0.3f), .4f);
        Triangle.addCube(triangles, -30, 30, 40, 20, 20, 20, new RGB(0, 0.4f, 0), 0.2f);        // gr�n, klein
        Triangle.addCube(triangles, 50, -20, -40, 10, 80, 100, new RGB(.5f, .5f, .5f), 0.2f);
        Triangle.addCube(triangles, -70, -26, -40, 130, 3, 40, new RGB(.5f, .5f, .5f), 0.2f);

        Matrix mRx = Matrix.createXRotation((float) (rayTracer.getX_angle_factor() * Math.PI / 16));
        Matrix mRy = Matrix.createYRotation((float) (rayTracer.getY_angle_factor() * Math.PI / 16));
        Matrix mT = Matrix.createTranslation(0, 0, 200);
        Matrix m = mT.mult(mRx).mult(mRy);
        System.out.println(m);
        m.apply(triangles);

    }
}
