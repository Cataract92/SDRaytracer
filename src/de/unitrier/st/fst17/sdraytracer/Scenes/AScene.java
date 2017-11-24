package de.unitrier.st.fst17.sdraytracer.Scenes;

import de.unitrier.st.fst17.sdraytracer.Datatypes.Light;
import de.unitrier.st.fst17.sdraytracer.Datatypes.RGB;
import de.unitrier.st.fst17.sdraytracer.Datatypes.Triangle;
import de.unitrier.st.fst17.sdraytracer.Datatypes.Vec3D;
import de.unitrier.st.fst17.sdraytracer.SDRaytracer;

import java.util.List;

public abstract class AScene {

    SDRaytracer rayTracer;

    public List<Triangle> triangles;

    public Light[] lights;

    public RGB ambient_color;
    public RGB background_color;

    public Vec3D start = new Vec3D(0,0,0);

    public AScene(SDRaytracer rayTracer) {
        this.rayTracer = rayTracer;
    }

    public abstract void createScene();
}
