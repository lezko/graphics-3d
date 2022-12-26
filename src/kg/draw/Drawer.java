/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.draw;

import java.util.Collection;
import kg.third.PolyLine3D;

/**
 * Интерфейс, описывающий в общем виде процесс рисования
 */
public interface Drawer {
    /**
     * Очищает область заданным цветом
     * @param color цвет
     */
    void clear(int color);
    
    /**
     * Рисует все полилинии
     * @param polyLines набор рисуемых полилиний.
     */
    void drawPolyLines(Collection<PolyLine3D> polyLines);
}
