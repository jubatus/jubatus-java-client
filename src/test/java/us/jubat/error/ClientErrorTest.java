package us.jubat.error;

import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.jubat.common.Datum;
import us.jubat.common.TypeMismatch;
import us.jubat.common.UnknownMethod;
import us.jubat.regression.RegressionClient;
import us.jubat.regression.ScoredDatum;
import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubatusClientTest;

public class ClientErrorTest extends JubatusClientTest {
	public ClientErrorTest() {
		super(JubaServer.classifier);
	}

	RegressionClient client;

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		this.client = new RegressionClient(server.getHost(), server.getPort(),
				NAME, TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test(expected = UnknownMethod.class)
	public void testUnknownMethod() {
		// classifier doesn't have 'estimate' function
		this.client.estimate(Collections.<Datum> emptyList());
	}

	@Test(expected = TypeMismatch.class)
	public void testTypeMismatch() {
		// Types of arguemnts of train are different in classifier and
		// regression
		this.client.train(Arrays.asList(new ScoredDatum(1.0f, new Datum())));
	}
}
