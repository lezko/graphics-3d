/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.third;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import kg.math.Vector3;

/**
 * Полилиния в трёхмерном пространстве.
 * Описывает ломанную в трёхмерном пространстве по опорным точкам
 * @author Alexey
 */
public class PolyLine3D {

    private final List<Vector3> points;
    private final boolean closed;

    /**
     * Создаёт новую полилинию на основе трёхмерных точек.
     *
     * @param points список точек-вершин ломанной
     * @param closed признак замкнутостит линии
     */
    public PolyLine3D(Collection<Vector3> points, boolean closed) {
        this.points = new LinkedList<>(points);
        this.closed = closed;
    }

    /**
     * Признак закрытости
     * @return возвращает истину, если линия замкнута, иначе - ложь.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Список вершин линии
     * @return возвращает список точек.
     */
    public List<Vector3> getPoints() {
        return points;
    }

    public Vector3 normal() {
        Vector3 normal, line1, line2;
        line1 = Vector3.sub(points.get(1), points.get(0));
        line2 = Vector3.sub(points.get(2), points.get(0));
        normal = new Vector3(
            line1.y() * line2.z() - line1.z() * line2.y(),
            line1.z() * line2.x() - line1.x() * line2.z(),
            line1.x() * line2.y() - line1.y() * line2.x()
        );

        return normal;
    }

    /**
     * Вычисляет среднее арифметическое по оси Z.
     * @return среднее по Z для полилинии.
     */
    public float avgZ() {
        if (points == null || points.size() == 0)
            return 0;
        float sum = 0;
        for (Vector3 v : points)
            sum += v.z();
        return sum / points.size();
    }

    public boolean isTriangle() {
        return false;
    }
}
