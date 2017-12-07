package cq_server.handler;

import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.IIdleTimeoutHandler;

public interface IInputMessageHandler extends IDataHandler, IConnectHandler, IDisconnectHandler, IIdleTimeoutHandler {
}
