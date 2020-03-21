/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.NativeLibraryLoader;

/**
 *
 * @author Hlib Pylypets
 */
 public class Bullet{
    
    static int speed = 25;
    static int gravity = -5;
    
    private static PhysicsSpace physics;
    private static Sphere shape;
    private static Material material;

    private final RigidBodyControl ball_phy;
    private final Geometry ball_geo;
    
    public Bullet(Vector3f location, Vector3f direction)
    {
        ball_geo = new Geometry("cannon ball", shape);
        ball_geo.setMaterial(material);
        ball_geo.setLocalTranslation(location);
        
        if (NativeLibraryLoader.isUsingNativeBullet()) {
            NativeLibraryLoader.loadNativeLibrary("bulletjme", true);
        }
            
        /** Make the ball physcial with a mass > 0.0f */
        ball_phy = new RigidBodyControl(1f);
        
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        physics.add(ball_phy);
        
        /** Accelerate the physcial ball to shoot it. */
        ball_phy.setGravity(new Vector3f(0, gravity, 0));
        ball_phy.setLinearVelocity(direction.mult(speed));
    }
    
    public static void setComponents(PhysicsSpace physics, Sphere shape, Material material){
        Bullet.physics = physics;
        Bullet.shape = shape;
        Bullet.material = material;
    }
    
    public static void adjustGravity(int delta_speed, int delta_gravity){
        Bullet.speed += delta_speed;
        Bullet.gravity += delta_gravity;
    }
    
    public Geometry getGeometry(){
        return ball_geo;
    }
    public RigidBodyControl getRigitBodyComponent(){
        return ball_phy;
    }
 }
