package lab1;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import lab1.implementation.DefaultState;

public class ShootingApp extends SimpleApplication {
    private BulletAppState physics_state;
    
    public static void main(String args[]) {
        ShootingApp app = new ShootingApp();
        app.start();
    }

    public ShootingApp() {
        super(new DefaultState(), new StatsAppState(), new FlyCamAppState());
    }
    
    @Override
    public void simpleInitApp() {
        physics_state = new BulletAppState();
        stateManager.attach(physics_state);
    }
}