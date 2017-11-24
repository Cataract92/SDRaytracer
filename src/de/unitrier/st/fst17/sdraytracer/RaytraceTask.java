package de.unitrier.st.fst17.sdraytracer;

import de.unitrier.st.fst17.sdraytracer.Datatypes.RGB;

import java.util.concurrent.Callable;

class RaytraceTask implements Callable {
    private SDRaytracer tracer;
    private int i;

    RaytraceTask(SDRaytracer t, int ii) {
        tracer = t;
        i = ii;
    }

    public RGB[] call() {
        RGB[] col = new RGB[tracer.getHeight()];
        for (int j = 0; j < tracer.getHeight(); j++) {
            tracer.getImage()[i][j] = new RGB(0, 0, 0);
            for (int k = 0; k < tracer.getRenderer().getRayPerPixel(); k++) {
                double di = i + (Math.random() / 2 - 0.25);
                double dj = j + (Math.random() / 2 - 0.25);
                if (tracer.getRenderer().getRayPerPixel() == 1) {
                    di = i;
                    dj = j;
                }
                Ray eye_ray = new Ray();
                eye_ray.setStart(tracer.getScene().start);   // ro
                eye_ray.setDir((float) (((0.5 + di) * tracer.getRenderer().getTan_fovx() * 2.0) / tracer.getWidth() - tracer.getRenderer().getTan_fovx()),
                        (float) (((0.5 + dj) * tracer.getRenderer().getTan_fovy() * 2.0) / tracer.getHeight() - tracer.getRenderer().getTan_fovy()),
                        (float) 1f);    // rd
                eye_ray.normalize();
                col[j] = tracer.getImage()[i][j].add(eye_ray.rayTrace(0, tracer), 1.0f / tracer.getRenderer().getRayPerPixel());
            }
        }
        return col;
    }
}
