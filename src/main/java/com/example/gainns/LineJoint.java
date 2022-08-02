package com.example.gainns;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

public class LineJoint {

    World world;
    private Line line;
    public LineJoint(float x1, float y1, float x2, float y2) {
        line = new Line(x1, y1, x2, y2);
    }

    public void create_joint(PhysObj obj1, PhysObj obj2) {
        RevoluteJoint<PhysObj> joint = new RevoluteJoint<PhysObj>(obj1, obj2, new Vector2(0.08, -0.2));
        world.addJoint(joint);
    }
}