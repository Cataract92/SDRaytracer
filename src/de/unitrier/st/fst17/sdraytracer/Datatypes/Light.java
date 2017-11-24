package de.unitrier.st.fst17.sdraytracer.Datatypes;

public class Light {
    public RGB color;
    public Vec3D position;

    public Light(Vec3D pos, RGB c) {
        position = pos;
        color = c;
    }
}
