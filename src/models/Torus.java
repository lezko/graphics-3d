package models;

import kg.math.*;
import kg.third.Model;
import kg.third.PolyLine3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Torus extends Model {

    interface FunctionQ {
        Function getX();
        Function getY();
        Function getZ();
        float getFrom();
        float getTo();
    }

    private final Function x, y, z;
    private final float r;
    private final int count = 20;

    public Torus(Function x, Function y, Function z, float r) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;

        createLines();
    }

    public Torus(Function x, Function y, Function z) {
        this(x, y, z, .3f);
    }

    private void createLines() {
        List<Vector3> pointsList = new ArrayList<>();
//        int n = 30;
//        for (int i = 0; i <= n; i++) {
//            float t = (float) (2 * Math.PI * i / n);
//            Vector3 newP = new Vector3(x.compute(t), y.compute(t), z.compute(t));
//            pointsList.add(newP);
//        }

        for (float t = 0; t < Math.PI * 2.01; t += .02f) {
            Vector3 newP = new Vector3(x.compute(t), y.compute(t), z.compute(t));
            pointsList.add(newP);
        }

        Vector3[] points = new Vector3[pointsList.size() + 2];
        for (int i = 0; i < points.length - 2; i++) {
            points[i] = pointsList.get(i);
        }
        points[pointsList.size()] = pointsList.get(1);
        points[pointsList.size() + 1] = pointsList.get(2);

        Vector3[] vertices = new Vector3[count], nextVertices = new Vector3[count];
        boolean f = false;
        for (int i = 2; i < points.length; i++) {
            Vector3 pa = points[i - 2];
            Vector3 pb = points[i - 1];
            Vector3 pc = points[i];
            Vector3 mid1 = Vector3.mid(pa, pb);
            Vector3 mid2 = Vector3.mid(pb, pc);

            Vector3 normal = new PolyLine3D(Arrays.asList(pa, pb, pc), true).normal();

            Vector3 ab = Vector3.sub(pa, pb);
            Vector3 bc = Vector3.sub(pb, pc);
            Vector3 dir1 = Vector3.cross(normal, ab);
            Vector3 dir2 = Vector3.cross(normal, bc);

            Vector3 center = Vector3.intersection(mid1, dir1, mid2, dir2);

            if (i == 2) {
                Vector3 rDir = Vector3.cross(dir1, ab).normalize();
                rDir = Matrix4Factories.scale(r).mul(new Vector4(rDir, 1)).asVector3();
                Vector3 p = Vector3.add(mid1, rDir);
                for (int j = 0; j < count; j++) {
                    vertices[j] = new Vector3(p.x(), p.y(), p.z());
                    p = p.rotateAround(pa, pb, (float) (Math.PI * 2 / count));
                }
            }

            for (int j = 0; j < count; j++) {
                System.out.println("start" + vertices[j].x() + " " + vertices[j].y() + " " + vertices[j].z());

                nextVertices[j] = vertices[j].rotateAround(
                    Vector3.add(center, normal),
                    Vector3.sub(center, normal),
                    (float) (-Math.acos(dir1.cos(dir2)))
                );
                System.out.println(nextVertices[j].x() + " " + nextVertices[j].y() + " " + nextVertices[j].z());

            }

            for (int j = 1; j < count; j++) {
                addLine(new PolyLine3D(Arrays.asList(nextVertices[j - 1], vertices[j - 1], vertices[j]), true));
                addLine(new PolyLine3D(Arrays.asList(nextVertices[j], nextVertices[j - 1], vertices[j]), true));
            }
            addLine(new PolyLine3D(Arrays.asList(nextVertices[count - 1], vertices[count - 1], vertices[0]), true));
            addLine(new PolyLine3D(Arrays.asList(nextVertices[0], nextVertices[count - 1], vertices[0]), true));

            for (int j = 0; j < count; j++) {
                vertices[j] = nextVertices[j];
            }

            if (f) {
                addLine(new PolyLine3D(Arrays.asList(mid1, Vector3.add(mid1, dir1)), false));
                addLine(new PolyLine3D(Arrays.asList(mid2, Vector3.add(mid2, dir2)), false));

//                addLine(new PolyLine3D(Arrays.asList(mid1, Vector3.add(mid1, rDir)), false));


//                addLine(new PolyLine3D(Arrays.asList(center, rotP1), false));
//                addLine(new PolyLine3D(Arrays.asList(center, rotP2), false));
                addLine(new PolyLine3D(Arrays.asList(center, pa), false));
                addLine(new PolyLine3D(Arrays.asList(center, pb), false));
                addLine(new PolyLine3D(Arrays.asList(center, pc), false));


                f = false;
            }
        }

//        addLine(new PolyLine3D(pointsList, false));
    }
}
