package networking;

import java.io.Serializable;

public abstract class Command<T> implements Serializable {
	abstract void execute(T executeOn);
}