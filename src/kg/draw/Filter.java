/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.draw;

/**
 * Интерфейс декларирует метод, который будет отвечать, подходит ли заданный экземпляр класса какому-либо условию
 * @author Alexey
 */
public interface Filter<T> {
    boolean permit(T value);
}
