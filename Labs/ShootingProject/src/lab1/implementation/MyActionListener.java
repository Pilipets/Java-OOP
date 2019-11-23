/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 *
 * @author druid
 */
public class MyActionListener implements ActionListener{
    private DefaultState state;
    
    public void setUp(DefaultState state, InputManager inputManager){
        this.state = state;
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("gravity_up", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("gravity_down", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("speed_up", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("speed_down", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addListener(this, "shoot");
        inputManager.addListener(this, "gravity_up");
        inputManager.addListener(this, "gravity_down");
        inputManager.addListener(this, "speed_up");
        inputManager.addListener(this, "speed_down");
    }
    
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("shoot") && !keyPressed) {
            state.produceBullet();
        }
        else if(!keyPressed){
            switch(name)
            {
                case "gravity_up":
                    Bullet.adjustGravity(0, 1);
                    break;
                case "gravity_down":
                    Bullet.adjustGravity(0, -1);
                    break;
                case "speed_up":
                    Bullet.adjustGravity(1, 0);
                    break;
                case "speed_down":
                    Bullet.adjustGravity(-1, 0);
                    break;
            }
            state.bulletPropertiesChanged();
        }
    }
    
}
