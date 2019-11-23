/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;

import com.jme3.asset.AssetKey;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import lab1.ShootingApp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

/**
 *
 * @author druid
 */
public class MySceneManagerTest {
    private MySceneManager sceneManager;
    private DesktopAssetManager assetManager;
    private Node dummy_node;
    private PhysicsSpace physics;
    private JmeContext dummy_context;
    
    private DesktopAssetManager spyAssetManager;
    
    private ShootingApp mockApp;
    
    public MySceneManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
        
        String[][] extensions_lists = { {"j3md"}, 
            {"jpg","jpeg","png"},
            {"fnt"}
        };
        String[] loader_classes = {"com.jme3.material.plugins.J3MLoader",
            "com.jme3.texture.plugins.AWTLoader",
            "com.jme3.font.plugins.BitmapFontLoader"
        };
        assetManager = new DesktopAssetManager();
        for(int i = 0; i < 3; ++i)
            assetManager.registerLoader(loader_classes[i], extensions_lists[i]);
        String rootPath = "/";
        String locatorClass = "com.jme3.asset.plugins.ClasspathLocator";
        assetManager.registerLocator(rootPath, locatorClass);
        spyAssetManager = spy(assetManager);
        
        Mockito.doReturn(assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg")).when(
                spyAssetManager).loadTexture("Textures/football.jpg");
        Mockito.doReturn(assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg")).when(
                spyAssetManager).loadTexture("Textures/Grass.jpg");
        
        physics = mock(PhysicsSpace.class);
        Mockito.doNothing().when(physics).add((RigidBodyControl)any());
        
        mockApp = mock(ShootingApp.class);
        dummy_node = new Node();
        Mockito.doReturn(dummy_node).when(mockApp).getRootNode();
        Mockito.doReturn(dummy_node).when(mockApp).getGuiNode();
        dummy_context = JmeSystem.newContext(new AppSettings(true),
                JmeContext.Type.Canvas);
        Mockito.doReturn(dummy_context).when(mockApp).getContext();
        
        sceneManager = new MySceneManager(spyAssetManager, physics, mockApp);
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of initBullet method, of class MySceneManager.
     */
    @Test
    public void testCtorBullet() {
        System.out.println("ctorBullet");
        
        Mockito.verify(mockApp, times(1)).getContext();
        Mockito.verify(mockApp, times(1)).getRootNode();
        Mockito.verify(mockApp, times(1)).getGuiNode();
        
        Mockito.verify(spyAssetManager, times(3)).loadAsset((AssetKey)any());
        Mockito.verify(spyAssetManager, times(1)).loadFont("Interface/Fonts/Default.fnt");
        Mockito.verify(spyAssetManager, times(1)).loadTexture("Textures/football.jpg");
        Mockito.verify(spyAssetManager, times(1)).loadTexture("Textures/Grass.jpg");
        
        Mesh bullet_shape = sceneManager.getBulletShape(), floor_shape = sceneManager.getFloorShape();
        assertTrue(bullet_shape.getClass().equals(Sphere.class));
        assertTrue(floor_shape.getClass().equals(Box.class));
        Sphere expected_sphere = new Sphere(32, 32, 0.4f, true, false);
        expected_sphere.setTextureMode(Sphere.TextureMode.Projected);
        Box expected_box = new Box(100f, 0.1f, 50f);
        expected_box.scaleTextureCoordinates(new Vector2f(30, 60));
        
        Sphere s1 = (Sphere)bullet_shape, s2 = expected_sphere;
        assertEquals(s1.getRadius(), s2.getRadius(), 1e-4);
        assertEquals(s1.getRadialSamples(), s2.getRadialSamples());
        assertEquals(s1.getZSamples(), s2.getZSamples());
        assertEquals(s1.getBound().getVolume(), s2.getBound().getVolume(), 1e-4);
        
        Box b1 = (Box)floor_shape, b2 = expected_box;
        assertEquals(b1.getCenter(), b2.getCenter());
        assertEquals(b1.getBound().getVolume(), b2.getBound().getVolume(), 1e-4);
    }
    
    /**
     * Test of initFloor method, of class MySceneManager.
     */
    @Test
    public void testInitFloor() {
        System.out.println("initFloor");
        
        Geometry floor_spy = spy(new Geometry());
        
        sceneManager.initFloor(floor_spy);
        
        Mockito.verify(floor_spy).setName("Floor");
        Mockito.verify(floor_spy).setMesh((Mesh)any());
        Mockito.verify(floor_spy).setMaterial((Material)any());
        Mockito.verify(floor_spy).setLocalTranslation(0, -0.1f, 0);
        
        Mockito.verify(physics).add((RigidBodyControl)any());
        assertEquals(floor_spy.getControl(RigidBodyControl.class).getMass(), 0f, 1e-4);
        
        Material expected_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        assertEquals(floor_spy.getMaterial().toString(),expected_mat.toString());
        
        Box expected_box = new Box(100f, 0.1f, 50f);
        assertEquals(floor_spy.getModelBound().getVolume(), expected_box.getBound().getVolume(), 1e-4);
    }

    /**
     * Test of initCrossHairs method, of class MySceneManager.
     */
    @Test
    public void testInitCrossHairs() {
        System.out.println("initCrossHairs");
        BitmapText aim_char = mock(BitmapText.class);
        sceneManager.initCrossHairs(aim_char);
        
        int font_size = sceneManager.getGuiFont().getCharSet().getRenderedSize();
        AppSettings settings = dummy_context.getSettings();
        Mockito.verify(aim_char).setSize(font_size * 2);
        Mockito.verify(aim_char).setText("+");
        Mockito.verify(aim_char).setLocalTranslation(
                settings.getWidth()/ 2 - font_size / 3 * 2,
                settings.getHeight() / 2 + aim_char.getLineHeight() / 2, 
                0);
    }

    /**
     * Test of initBulletRenderText method, of class MySceneManager.
     */
    @Test
    public void testInitBulletRenderText() {
        System.out.println("initBulletRenderText");
        BitmapText bulletRenderText = mock(BitmapText.class);
        sceneManager.initBulletRenderText(bulletRenderText);
        
        Mockito.verify(bulletRenderText).setSize(sceneManager.getGuiFont().getCharSet().getRenderedSize());
        Mockito.verify(bulletRenderText).setText("Gravity=-5, Speed=25");
        Mockito.verify(bulletRenderText).setLocalTranslation(
                0, 
                dummy_context.getSettings().getHeight(), 
                0);
        
        
    }
}
