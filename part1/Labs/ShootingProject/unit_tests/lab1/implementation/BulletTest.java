/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.shape.Sphere;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 *
 * @author druid
 */
public class BulletTest {
    /**
     * Test of Bullet method, of class Bullet.
     */
    @Test
    public void testBullet() {
        System.out.println("Bullet");
        Sphere dummy_shape = mock(Sphere.class);
        Material dummy_material = mock(Material.class);
        PhysicsSpace dummy_space = mock(PhysicsSpace.class);
        doNothing().when(dummy_space).add((RigidBodyControl)any());
        
        Bullet.setComponents(dummy_space, dummy_shape, dummy_material);
        Vector3f location = new Vector3f(1,1,1),
                direction = new Vector3f(-1,-1,-1);
        
        Bullet ball = new Bullet(location, direction);
        RigidBodyControl ball_phy = ball.getRigitBodyComponent();
        assertEquals(ball_phy.getMass(), 1,0f);
        assertEquals(ball_phy.getGravity(), new Vector3f(0, -5, 0));
        assertEquals(ball_phy.getLinearVelocity(), direction.mult(25));
    }
    
    /**
     * Test of adjustGravity method, of class Bullet.
     */
    @Test
    public void testAdjustGravity() {
        System.out.println("adjustGravity");
        Bullet.speed = 25;
        Bullet.gravity = -5;
        
        int[] delta_speed =   { 0, 0, 0, -1, 1 };
        int[] delta_gravity = { 0, 1, -1, 0, 0 };
        for(int i = 0; i < 5; ++i)
        {
            int pre_speed = Bullet.speed, pre_gravity = Bullet.gravity;
            Bullet.adjustGravity(delta_speed[i], delta_gravity[i]);
            assertEquals(Bullet.speed, pre_speed + delta_speed[i]);
            assertEquals(Bullet.gravity, pre_gravity + delta_gravity[i]);
        }
    }
}
