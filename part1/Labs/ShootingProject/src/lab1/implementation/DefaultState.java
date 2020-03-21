/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *
 * @author druid
 */
public class DefaultState extends AbstractAppState{
    private SimpleApplication app;
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private Camera            cam;
    
    private MyActionListener  actionListener;
    private MySceneManager sceneManager;
    private final Geometry floorGeo;
    private BitmapText aim_char;
    private BitmapText bulletRenderText;
    private String propertiesText;
    private BulletAppState physics_state;
    
    private final float cam_speed;
    private final float cam_height;
    
    public DefaultState(){
        cam_speed = 20f;
        cam_height = 4f;
        floorGeo = new Geometry();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app          = (SimpleApplication)app;
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        
        this.cam          = this.app.getCamera();
        this.physics_state = stateManager.getState(BulletAppState.class);
        
        setSceneSettings();
   }
    
    private void setSceneSettings() {
        cam.setLocation(new Vector3f(0, cam_height, 0f));
        stateManager.getState(FlyCamAppState.class).getCamera().setMoveSpeed(cam_speed);
                
        sceneManager = new MySceneManager(assetManager, physics_state.getPhysicsSpace(), app);
        sceneManager.initBullet();
        sceneManager.initFloor(floorGeo);
        aim_char = new BitmapText(sceneManager.getGuiFont(), false);
        sceneManager.initCrossHairs(aim_char);
        
        bulletRenderText = new BitmapText(sceneManager.getGuiFont(), false);
        sceneManager.initBulletRenderText(bulletRenderText);
        
        this.actionListener = new MyActionListener();
        actionListener.setUp(this, this.app.getInputManager());   
    }

    public void produceBullet() {
        Bullet ball = new Bullet(cam.getLocation(), cam.getDirection());
        rootNode.attachChild(ball.getGeometry());
    }
    
    public void bulletPropertiesChanged(){
        bulletRenderText.setText(String.format("Gravity=%d, Speed=%d", 
                Bullet.gravity, Bullet.speed));
    }
    
    public float getCamSpeed(){
        return cam_speed;
    }
    
    public float getCamHeight(){
        return cam_height;
    }
    
    public Geometry getFloor(){
        return floorGeo;
    }
}
