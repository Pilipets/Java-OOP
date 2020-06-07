package core.commands.factory;

import core.commands.BaseCmd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandFactory {
    BaseCmd getCommand(String name, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException;
}
