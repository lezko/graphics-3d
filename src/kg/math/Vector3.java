/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.math;

/**
 * Класс, хранящий трёхмерный вектор / точку в трёхмерном пространстве.
 *
 * @author Alexey
 */
public class Vector3 {

    public static double E = 1e-6;
    private float[] values; /*Значения хранятся в виде массива из трёх элементов*/

    /**
     * Создаёт экземпляр вектора на основе значений трёх составляющих
     *
     * @param x первая составляющая, описывающая X-координату
     * @param y вторая составляющая, описывающая Y-координату
     * @param z третья составляющая, описывающая Z-координату
     */
    public Vector3(float x, float y, float z) {
        values = new float[] { x, y, z };
    }

    /**
     * X-составляющая вектора
     *
     * @return X-составляющая вектора
     */
    public float x() {
        return values[0];
    }

    /**
     * Y-составляющая вектора
     *
     * @return Y-составляющая вектора
     */
    public float y() {
        return values[1];
    }

    /**
     * Z-составляющая вектора
     *
     * @return Z-составляющая вектора
     */
    public float z() {
        return values[2];
    }

    public float cos(Vector3 v) {
        return (float) ((v.x()*x() + v.y()*y() + v.z()*z())/(Math.sqrt(Math.pow(v.x(), 2) + Math.pow(v.y(), 2) + Math.pow(v.z(), 2))*Math.sqrt(Math.pow(x(), 2) + Math.pow(y(), 2) + Math.pow(z(), 2))));
    }

    public Vector3 normalize() {
        double l = Math.sqrt(x() * x() + y() * y() + z() * z());
        values[0] /= l;
        values[1] /= l;
        values[2] /= l;
        return this;
    }

    public Vector3 rotateAround(Vector3 p1, Vector3 p2, float t) {
        Matrix4 tr = Matrix4Factories.translation(new Vector3(-p1.x(), -p1.y(), -p1.z()));
        Matrix4 tr1 = Matrix4Factories.translation(p1);

        Matrix4 rx = Matrix4.one(), rx1 = Matrix4.one();
        Vector3 u = Vector3.sub(p1, p2).normalize();
        float d = (float) Math.sqrt(u.y() * u.y() + u.z() * u.z());
        float a = u.x(), b = u.y(), c = u.z();
        if (d > E) {
            rx = new Matrix4(
                1, 0, 0, 0,
                0, c / d, -b / d, 0,
                0, b / d, c / d, 0,
                0, 0, 0, 1
            );
            rx1 = new Matrix4(
                1, 0, 0, 0,
                0, c / d, b / d, 0,
                0, -b / d, c / d, 0,
                0, 0, 0, 1
            );
        }

        Matrix4 ry = new Matrix4(
            d, 0, -a, 0,
            0, 1, 0, 0,
            a, 0, d, 0,
            0, 0, 0, 1
        );
        Matrix4 ry1 = new Matrix4(
            d, 0, a, 0,
            0, 1, 0, 0,
            -a, 0, d, 0,
            0, 0, 0, 1
        );

        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        Matrix4 rz = new Matrix4(
            cos, sin, 0, 0,
            -sin, cos, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );

        Vector3 res = tr1.mul(
            rx1.mul(
                ry1.mul(
                    rz.mul(
                        ry.mul(
                            rx.mul(
                                tr.mul(
                                    new Vector4(this, 1)
                                )
                            )
                        )
                    )
                )
            )
        ).asVector3();
//        values[0] = res.x();
//        values[1] = res.y();
//        values[2] = res.z();

//        System.out.println(res.x() + " " + res.y() + " " + res.z());

        return res;
    }

    public static Vector3 intersection(Vector3 p1, Vector3 dir1, Vector3 p2, Vector3 dir2) {
        float a1 = dir1.x(), b1 = dir1.y(), c1 = dir1.z();
        float a2 = dir2.x(), b2 = dir2.y(), c2 = dir2.z();

        float m = (p2.y() - p1.y() + b1 / a1 * (p1.x() - p2.x())) / (b1 * a2 / a1 - b2);
        return new Vector3(a2 * m + p2.x(), b2 * m + p2.y(), c2 * m + p2.z());
//        return null;
    }

    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(
            v1.x() + v2.x(),
            v1.y() + v2.y(),
            v1.z() + v2.z()
        );
    }

    public static Vector3 sub(Vector3 v1, Vector3 v2) {
        return new Vector3(
            v1.x() - v2.x(),
            v1.y() - v2.y(),
            v1.z() - v2.z()
        );
    }

    public static Vector3 mid(Vector3 v1, Vector3 v2) {
        return new Vector3(
            (v1.x() + v2.x()) / 2,
            (v1.y() + v2.y()) / 2,
            (v1.z() + v2.z()) / 2
        );
    }

    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return new Vector3(
            v1.y() * v2.z() - v1.z() * v2.y(),
            v1.z() * v2.x() - v1.x() * v2.z(),
            v1.x() * v2.y() - v1.y() * v2.x()
        );
    }

    public static float dot(Vector3 v1, Vector3 v2) {
        return v1.len() * v2.len() * v1.cos(v2);
    }

    /**
     * Метод, возвращающий составляющую вектора по порядковому номеру
     *
     * @param idx порядковый номер
     * @return значение составляющей вектора
     */
    public float at(int idx) {
        return values[idx];
    }

    private static final float EPSILON = 1e-10f;

    /**
     * Метод, возвращающий длину вектора
     *
     * @return длина вектора
     */
    public float len() {
        float lenSqr = values[0] * values[0] + values[1] * values[1] + values[2] * values[2];
        if (lenSqr < EPSILON)
            return 0;
        return (float) Math.sqrt(lenSqr);
    }
}
