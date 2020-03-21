/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 *
 * @author druid
 */
public class MySceneManager {
    private final AssetManager assetManager;
    private final AppSettings settings;
    private final Node rootNode;
    private final Node guiNode;
    private final PhysicsSpace physics;
    
    private Box floor_shape;
    private Material floor_mat;
    
    private Sphere bullet_shape;
    private Material bullet_mat;
    
    private final BitmapFont guiFont;
    
    private final String bullet_mat_path;
    private final String bullet_tex_path;
    private final String floor_mat_path;
    private final String floor_tex_path;
    private final String font_path;
    
    public MySceneManager(AssetManager assetManager, PhysicsSpace physics, SimpleApplication app){
        this.assetManager = assetManager;
        this.settings = app.getContext().getSettings();
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.physics = physics;
        
        bullet_mat_path = "Common/MatDefs/Misc/Unshaded.j3md";
        bullet_tex_path = "Textures/football.jpg";
        floor_mat_path = "Common/MatDefs/Misc/Unshaded.j3md";
        floor_tex_path = "Textures/Grass.jpg";
        font_path = "Interface/Fonts/Default.fnt";
        
        initShapes();
        initMaterials();
        
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont(font_path);
    }
    
    private void initShapes(){
        /** Initialize the cannon ball geometry */
        bullet_shape = new Sphere(32, 32, 0.4f, true, false);
        bullet_shape.setTextureMode(TextureMode.Projected);
        /** Initialize the floor geometry */
        floor_shape = new Box(100f, 0.1f, 50f);
        floor_shape.scaleTextureCoordinates(new Vector2f(30, 60));
    }
    private void initMaterials(){
        bullet_mat = new Material(assetManager, bullet_mat_path);
        Texture tex2 = assetManager.loadTexture(bullet_tex_path);
        bullet_mat.setTexture("ColorMap", tex2);

        floor_mat = new Material(assetManager, floor_mat_path);
        Texture tex3 = assetManager.loadTexture(floor_tex_path);
        tex3.setWrap(WrapMode.Repeat);
        floor_mat.setTexture("ColorMap", tex3);
    }
    
    public void initBullet(){
        Bullet.setComponents(physics, bullet_shape, bullet_mat);
    }
    public void initFloor(Geometry floor_geo){
        floor_geo.setName("Floor");
        floor_geo.setMesh(floor_shape);
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(floor_geo);
        
        /* Make the floor physical with mass 0.0f! */
        RigidBodyControl floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        physics.add(floor_phy);
    }
    
    public void initCrossHairs(BitmapText aim_char){
        int font_size = guiFont.getCharSet().getRenderedSize();
        aim_char.setSize(font_size * 2);
        aim_char.setText("+");        // fake crosshairs :)
        aim_char.setLocalTranslation( // center
            settings.getWidth()/ 2 - font_size / 3 * 2,
            settings.getHeight() / 2 + aim_char.getLineHeight() / 2, 0);
        guiNode.attachChild(aim_char);
    }
    
    public void initBulletRenderText(BitmapText bulletRenderText){
        bulletRenderText.setSize(guiFont.getCharSet().getRenderedSize());
        bulletRenderText.setText(String.format("Gravity=%d, Speed=%d", 
                Bullet.gravity, Bullet.speed));
        bulletRenderText.setLocalTranslation( // top-left
            0,
            settings.getHeight(), 
            0);
        guiNode.attachChild(bulletRenderText);
    }
    public Mesh getBulletShape(){
        return bullet_shape;
    }
    public BitmapFont getGuiFont(){
        return guiFont;
    }
    public Mesh getFloorShape(){
        return floor_shape;
    }
}
