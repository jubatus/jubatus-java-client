package us.jubat.testutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JubaServer {
	public static final String NAME = System.getProperty("jubatus.name");

	private List<String> command = new ArrayList<String>();
	private Process process;
	private StdoutReader stdout_reader;

	public JubaServer(Engine engine) {
		command.add(engine.getCommandName());
		command.add("-p");
		command.add(String.valueOf(engine.getPort()));
		command.add("-n");
		command.add(NAME == null ? "" : NAME);
	}

	public void start() throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		process = pb.start();
		stdout_reader = new StdoutReader(process.getInputStream());
		stdout_reader.setDaemon(true);
		stdout_reader.start();
		// sleep　1 second.
		Thread.sleep(1000);
	}

	public void stop() throws IOException, InterruptedException {
		stdout_reader.dump();
		process.getInputStream().close();
		process.getErrorStream().close();
		process.getOutputStream().close();
		process.destroy();
		process.waitFor();
	}

	private class StdoutReader extends Thread {
		private BufferedReader br;
		private List<String> stdout = new ArrayList<String>();

		public StdoutReader(InputStream is) {
			br = new BufferedReader(new InputStreamReader(is));
		}

		@Override
		public void run() {
			try {
				try {
					String line;
					while ((line = br.readLine()) != null) {
						stdout.add(line);
					}
				} finally {
					br.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// for debug
		public void dump() {
			for (String line : stdout) {
				System.out.println(line);
			}
		}
	}

	public enum Engine {
		classifier, recommender, regression, stat, graph;

		public static final int BASEPORT = Integer.parseInt(System
				.getProperty("jubatus.baseport"));

		public String getCommandName() {
			return "juba" + this.name();
		}

		public int getPort() {
			return BASEPORT + this.ordinal();
		}
	}
}
