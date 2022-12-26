package models;

import kg.math.Matrix4;
import kg.math.Matrix4Factories;
import kg.math.Vector3;
import kg.math.Vector4;
import kg.third.Model;
import kg.third.PolyLine3D;

import java.util.Arrays;

public class Tor extends Model {

    private final int outerCount = 20, innerCount = 20;
    private final float R, r;

    public Tor(float R, float r) {
        this.R = R;
        this.r = r;
        createLines();
    }

    private void createLines() {
        Matrix4 rx = Matrix4Factories.rotationXYZ(Math.PI * 2 / innerCount, Matrix4Factories.Axis.X);
        Matrix4 ry = Matrix4Factories.rotationXYZ(Math.PI * 2 / outerCount, Matrix4Factories.Axis.Y);

        Vector3[] points1 = new Vector3[innerCount];
        Vector3 p = new Vector3(0, 0, r);
        Matrix4 tr = Matrix4Factories.translation(new Vector3(0, 0, R));
        for (int i = 0; i < innerCount; i++) {
            points1[i] = tr.mul(new Vector4(p, 1)).asVector3();
            p = rx.mul(new Vector4(p, 1)).asVector3();
        }

        Vector3[] points2 = new Vector3[innerCount];
        for (int i = 0; i < innerCount; i++) {
            points2[i] = ry.mul(new Vector4(points1[i], 1)).asVector3();
        }

        for (int i = 0; i < outerCount; i++) {
            for (int j = 1; j < innerCount; j++) {
                addLine(new PolyLine3D(Arrays.asList(points2[j - 1], points1[j - 1], points1[j]), true));
                addLine(new PolyLine3D(Arrays.asList(points2[j], points2[j - 1], points1[j]), true));
            }
            addLine(new PolyLine3D(Arrays.asList(points2[innerCount - 1], points1[innerCount - 1], points1[0]), true));
            addLine(new PolyLine3D(Arrays.asList(points2[0], points2[innerCount - 1], points1[0]), true));
            for (int j = 0; j < innerCount; j++) {
                points1[j] = ry.mul(new Vector4(points1[j], 1)).asVector3();
                points2[j] = ry.mul(new Vector4(points2[j], 1)).asVector3();
            }
        }
    }
}
