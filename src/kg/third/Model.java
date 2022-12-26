/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.third;

import kg.math.Matrix4;
import kg.math.Vector3;
import kg.math.Vector4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Описывает в общем виде любую модель
 *
 * @author Alexey
 */
public class Model {

    private final List<PolyLine3D> polyLines = new ArrayList<>();

    public Model(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream(filename));
        List<Vector3> vertices = new ArrayList<>();
        while (in.hasNext()) {
            String[] arr = in.nextLine().split("\\s");
            if (arr[0].equals("v")) {
                vertices.add(new Vector3(
                    Float.parseFloat(arr[1]),
                    Float.parseFloat(arr[2]),
                    Float.parseFloat(arr[3]))
                );
            } else if (arr[0].equals("f")) {
                int idx1, idx2, idx3;
                idx1 = Integer.parseInt(arr[1].split("/")[0]) - 1;
                idx2 = Integer.parseInt(arr[2].split("/")[0]) - 1;
                idx3 = Integer.parseInt(arr[3].split("/")[0]) - 1;

                polyLines.add(new PolyLine3D(Arrays.asList(
                    vertices.get(idx1),
                    vertices.get(idx2),
                    vertices.get(idx3)
                ), true));
            }
        }

        in.close();
    }

    public Model() {
    }

    /**
     * Любая модель - это набор полилиний.
     *
     * @return Списко полилиний модели.
     */
    public List<PolyLine3D> getLines() {
        return polyLines;
    }

    public void addLine(PolyLine3D line) {
        polyLines.add(line);
    }

    public void addLines(Collection<PolyLine3D> polyLines) {
        this.polyLines.addAll(polyLines);
    }

    public Model transform(Matrix4 m) {
        List<PolyLine3D> newLines = new ArrayList<>();
        for (PolyLine3D line : polyLines) {
            List<Vector3> newPoints = new ArrayList<>();
            for (Vector3 v : line.getPoints()) {
                newPoints.add(m.mul(new Vector4(v, 1)).asVector3());
            }
            newLines.add(new PolyLine3D(newPoints, true));
        }
        polyLines.clear();
        polyLines.addAll(newLines);

        return this;
    }
}
