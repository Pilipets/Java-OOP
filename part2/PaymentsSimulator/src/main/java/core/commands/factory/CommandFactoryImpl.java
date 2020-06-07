package core.commands.factory;

import core.commands.*;
import core.database.ConcreteDB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandFactoryImpl implements CommandFactory {
    private static final Logger LOGGER = Logger.getLogger(CommandFactoryImpl.class.getName());
    private CommandFactoryImpl(){};
    private static CommandFactoryImpl factory=new CommandFactoryImpl();
    public static CommandFactory getFactory(){
        return factory;
    }
    @Override
    public BaseCmd getCommand(String name, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException {
        if (name==null){
            LOGGER.log(Level.SEVERE, "command name is NULL ");
            throw new IllegalArgumentException("Name cannot be NULL") ;
        }
        else{
            LOGGER.log(Level.INFO, "Factory getCommand " + name);
            BaseCmd command=null;

            //----------------------------//
            if (name.equals("Block")) {
                command = new BlockCommand();
            }
            else if (name.equals("TopUp")){
                command = new TopUpCommand();
            }
            else if(name.equals("Pay")){
                command = new PayCommand();
            }
            else if (name.equals("Authorization")){
                command = new AuthorizationCmd();
            }
            else if (name.equals("Payments")){
                command = new PaymentsCommand();
            }
            else if (name.equals("Unblock")){
                command = new UnblockCommand();
            }
            else if (name.equals("Registration")){
                command = new RegistrationCmd();
            }

            //---------------------------//


            if (command==null){
                throw new IllegalArgumentException("Wrong command name");
            }

            command.setResp(response);
            command.setReq(request);
            command.setDB(ConcreteDB.getInstance());
            return command;
        }


    }

}
