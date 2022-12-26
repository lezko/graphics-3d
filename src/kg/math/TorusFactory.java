package kg.math;

import models.Torus;

public class TorusFactory {

    public static Torus classic() {
        return new Torus(
            t -> sin(t) + 2 * sin(2 * t),
            t -> cos(t) - 2 * cos(2 * t),
            t -> -sin(3 * t)
        );
    }

    public static Torus knot() {
        return new Torus(
                t -> (cos(7 * t) + 2) * cos(3 * t),
                t -> (cos(7 * t) + 2) * sin(3 * t),
                t -> -sin(7 * t)
        );
    }

    private static float sin(float x) {
        return (float) Math.sin(x);
    }

    private static float cos(float x) {
        return (float) Math.cos(x);
    }
}
