package de.unitrier.st.fst17.sdraytracer;

import de.unitrier.st.fst17.sdraytracer.Datatypes.*;

import java.util.List;

public class Ray {
    private Vec3D start = new Vec3D(0, 0, 0);
    private Vec3D dir = new Vec3D(0, 0, 0);

    void setStart(Vec3D start) {
        this.start = start;
    }

    void setDir(float dx, float dy, float dz) {
        dir = new Vec3D(dx, dy, dz);
    }

    void normalize() {
        dir.normalize();
    }

    // see M�ller&Haines, page 305
    private IPoint intersect(Triangle t) {
        float epsilon = IPoint.epsilon;
        Vec3D e1 = t.p2.minus(t.p1);
        Vec3D e2 = t.p3.minus(t.p1);
        Vec3D p = dir.cross(e2);
        float a = e1.dot(p);
        if ((a > -epsilon) && (a < epsilon)) return new IPoint(null, null, -1);
        float f = 1 / a;
        Vec3D s = start.minus(t.p1);
        float u = f * s.dot(p);
        if ((u < 0.0) || (u > 1.0)) return new IPoint(null, null, -1);
        Vec3D q = s.cross(e1);
        float v = f * dir.dot(q);
        if ((v < 0.0) || (u + v > 1.0)) return new IPoint(null, null, -1);
        // intersection point is u,v
        float dist = f * e2.dot(q);
        if (dist < epsilon) return new IPoint(null, null, -1);
        Vec3D ip = t.p1.mult(1 - u - v).add(t.p2.mult(u)).add(t.p3.mult(v));
        //src.Logger.debug("Intersection point: "+ip.x+","+ip.y+","+ip.z);
        return new IPoint(t, ip, dist);
    }

    private IPoint hitObject(List<Triangle> triangles) {
        IPoint isect = new IPoint(null, null, -1);
        float idist = -1;
        for (Triangle t : triangles) {
            IPoint ip = intersect(t);
            if (ip.dist != -1)
                if ((idist == -1) || (ip.dist < idist)) { // save that intersection
                    idist = ip.dist;
                    isect.ipoint = ip.ipoint;
                    isect.dist = ip.dist;
                    isect.triangle = t;
                }
        }
        return isect;  // return intersection point and normal
    }

    private RGB lighting(IPoint ip, int rec, SDRaytracer rayTracer) {
        Vec3D point = ip.ipoint;
        Triangle triangle = ip.triangle;
        RGB color = triangle.color.add(rayTracer.getScene().ambient_color, 1);
        Ray shadow_ray = new Ray();
        for (Light light : rayTracer.getScene().lights) {
            shadow_ray.start = point;
            shadow_ray.dir = light.position.minus(point).mult(-1);
            shadow_ray.dir.normalize();
            IPoint ip2 = shadow_ray.hitObject(rayTracer.getScene().triangles);
            if (ip2.dist < IPoint.epsilon) {
                float ratio = Math.max(0, shadow_ray.dir.dot(triangle.normal));
                color = color.add(light.color, ratio);
            }
        }
        Ray reflection = new Ray();
        //R = 2N(N*L)-L)    L ausgehender Vektor
        Vec3D L = dir.mult(-1);
        reflection.start = point;
        reflection.dir = triangle.normal.mult(2 * triangle.normal.dot(L)).minus(L);
        reflection.dir.normalize();
        RGB rcolor = reflection.rayTrace(rec+1,rayTracer);
        float ratio = (float) Math.pow(Math.max(0, reflection.dir.dot(L)), triangle.shininess);
        color = color.add(rcolor, ratio);
        return (color);
    }

    RGB rayTrace(int rec, SDRaytracer rayTracer) {
        if (rec > rayTracer.getRenderer().getMaxRec()) return RGB.BLACK;
        IPoint ip = hitObject(rayTracer.getScene().triangles);  // (ray, p, n, triangle);
        if (ip.dist > IPoint.epsilon)
            return lighting(ip, rec, rayTracer);
        else
            return RGB.BLACK;
    }
}
