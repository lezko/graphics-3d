/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg;

import kg.draw.Drawer;
import kg.draw.SimpleDrawer;
import kg.math.Matrix4;
import kg.math.Matrix4Factories;
import kg.math.TorusFactory;
import kg.math.Vector3;
import kg.screen.ScreenConverter;
import kg.third.Model;
import kg.third.Scene;
import kg.third.SimpleCamera;
import models.Tor;
import models.Torus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

/**
 * @author Alexey
 */
public class DrawPanel extends JPanel
    implements CameraController.RepaintListener {
    private Scene scene;
    private ScreenConverter sc;
    private SimpleCamera cam;
    private CameraController camController;

    public DrawPanel() {
        super();
        sc = new ScreenConverter(-1, 1, 2, 2, 1, 1);
        cam = new SimpleCamera();
        camController = new CameraController(cam, sc);
        scene = new Scene(Color.WHITE.getRGB());
        scene.showAxes();

        try {
            scene.getModelsList().add(new Model("Mike.obj"));
            scene.getModelsList().add(new Model("Knuckles.obj")
                    .transform(Matrix4Factories.translation(new Vector3(500, 105, 50)))
            .transform(Matrix4Factories.scale(.005f)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        scene.getModelsList().add(new Tor(1f, .5f));
        scene.getModelsList().add(TorusFactory.knot().transform(Matrix4Factories.translation(new Vector3(-7, 0, 0))));
        scene.getModelsList().add(TorusFactory.classic().transform(Matrix4Factories.translation(new Vector3(7, 0, 0))));


        camController.addRepaintListener(this);
        addMouseListener(camController);
        addMouseMotionListener(camController);
        addMouseWheelListener(camController);
    }

    @Override
    public void paint(Graphics g) {
        sc.setScreenSize(getWidth(), getHeight());
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) bi.getGraphics();
        Drawer dr = new SimpleDrawer(sc, graphics);
        scene.drawScene(dr, cam);
        g.drawImage(bi, 0, 0, null);
        graphics.dispose();
    }

    @Override
    public void shouldRepaint() {
        repaint();
    }
}
