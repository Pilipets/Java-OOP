package testing_serialization;

import client_serialization.SClient;
import data.Vehicle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.powermock.core.classloader.annotations.PrepareForTest;
import server_serialization.SServer;

@PrepareForTest(SClient.class)
public class SimpleTest {
    static Vehicle example_data = null;
    static SServer server = null;
    static SClient client = null;


    @BeforeClass
    public static void setUp(){
        server = new SServer(1111);
        client = new SClient("127.0.0.1",1111);
        example_data = new Vehicle(325654564, "Bugatti Veyron", Vehicle.VehicleType.SportCar, 4.5e8);
    }

    @Test
    public void ClientCtorTest(){
        Assert.assertEquals(1111, client.getRunningPort());
        Assert.assertEquals("127.0.0.1", client.getHostAddress());
    }

    @Test
    public void ServerCtorTest(){
        Assert.assertEquals(1111, server.getRunningPort());
    }
}
