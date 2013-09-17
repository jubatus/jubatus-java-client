package us.jubat.common;

import java.net.UnknownHostException;
import java.util.Map;

import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

import us.jubat.common.ClientBase.RPCInterfaceBase;
import us.jubat.common.type.TString;

public class ClientBase<I extends RPCInterfaceBase> {
	private Client client;
	protected I iface;
	protected String name;

	public static interface RPCInterfaceBase {
		String get_config(String name);

		boolean save(String name, String id);

		boolean load(String name, String id);

		Map<String, Map<String, String>> get_status(String name);
	}

	public ClientBase(Class<I> interfaceClass, String host, int port,
			String name, int timeoutSec) throws UnknownHostException {
		EventLoop loop = EventLoop.defaultEventLoop();
		this.client = new Client(host, port, loop);
		this.client.setRequestTimeout(timeoutSec);
		this.iface = this.client.proxy(interfaceClass);
		this.name = name;
	}
	
	public String getConfig() {
		return iface.get_config(this.name);
	}

	public boolean save(String id) {
		TString.instance.check(id);
		return iface.save(this.name, id);
	}

	public boolean load(String id) {
		TString.instance.check(id);
		return iface.load(this.name, id);
	}

	public Map<String, Map<String, String>> getStatus() {
		return iface.get_status(this.name);
	}

	public Client getClient() {
		return client;
	}
}
