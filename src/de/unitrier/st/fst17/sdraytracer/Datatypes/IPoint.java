package de.unitrier.st.fst17.sdraytracer.Datatypes;

public class IPoint {
    public final static float epsilon = 0.0001f;
    public Triangle triangle;
    public Vec3D ipoint;
    public float dist;

    public IPoint(Triangle tt, Vec3D ip, float d) {
        triangle = tt;
        ipoint = ip;
        dist = d;
    }
}
