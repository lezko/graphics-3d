/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.LinkedList;

import kg.math.Vector3;
import kg.screen.ScreenConverter;
import kg.screen.ScreenCoordinates;
import kg.screen.ScreenPoint;
import kg.third.PolyLine3D;

/**
 * Реализация рисователя полигонов с помощью рёбер.
 *
 * @author Alexey
 */
public class SimpleDrawer extends ScreenGraphicsDrawer {

    public SimpleDrawer(ScreenConverter sc, Graphics2D g) {
        super(sc, g);
    }

    /**
     * Рисует одну полилинию на графиксе.
     *
     * @param polyline полилиния
     */
    @Override
    protected void oneDraw(PolyLine3D polyline) {
        LinkedList<ScreenPoint> points = new LinkedList<>();
        /*переводим все точки в экранные*/
        for (Vector3 v : polyline.getPoints())
            points.add(getScreenConverter().r2s(v));
        getGraphics().setColor(Color.BLACK);
        /*если точек меньше двух, то рисуем отдельными алгоритмами*/
        if (points.size() < 2) {
            if (points.size() > 0)
                getGraphics().fillRect(points.get(0).getI(), points.get(0).getJ(), 1, 1);
            return;
        }
        /*создаём хранилище этих точек в виде двух массивов*/
        ScreenCoordinates crds = new ScreenCoordinates(points);
        /*если линия замкнута - рисем полиго, иначе - полилинию*/
        if (polyline.isClosed()) {
//            getGraphics().drawPolygon(crds.getXx(), crds.getYy(), crds.size());

            Vector3 normal = polyline.normal();
            double cos = -normal.cos(new Vector3(0, 0, 1));
            if (cos > 0) {
                getGraphics().setColor(new Color((int) (30 * (cos)), (int) (130 * (cos)), (int) (30 * (cos))));
            }
            getGraphics().fillPolygon(crds.getXx(), crds.getYy(), crds.size());
        } else {
            getGraphics().drawPolyline(crds.getXx(), crds.getYy(), crds.size());
        }
    }

    /**
     * В данной реализации возвращаем фильтр, который одобряет все полилинии.
     *
     * @return фильтр полилиний
     */
    @Override
    protected Filter<PolyLine3D> getFilter() {
        return new Filter<PolyLine3D>() {
            @Override
            public boolean permit(PolyLine3D line) {
                return true;
            }
        };
    }

    /**
     * Сравниваем полилинии по среднему Z.
     *
     * @return компаратор
     */
    @Override
    protected Comparator<PolyLine3D> getComparator() {
        return new Comparator<PolyLine3D>() {
            private static final float EPSILON = 1e-10f;

            @Override
            public int compare(PolyLine3D o1, PolyLine3D o2) {
                float d = o1.avgZ() - o2.avgZ();
                if (-EPSILON < d && d < EPSILON)
                    return 0;
                return -(d < 0 ? -1 : 1);
            }
        };
    }
}
