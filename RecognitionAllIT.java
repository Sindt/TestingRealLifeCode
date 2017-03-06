package net.sf.javaanpr.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;

@RunWith(Parameterized.class)
public class RecognitionAllIT {

	private File snapshot;
	private String plateExpected;
	private Intelligence intel;

	public RecognitionAllIT(File snapshot, String plateExpected) {
		this.snapshot = snapshot;
		this.plateExpected = plateExpected;
	}

	@Before
	public void initialize() throws ParserConfigurationException, SAXException, IOException {
		intel = new Intelligence();
	}

	@Parameterized.Parameters
	public static Collection<Object[]> testDataCreator() throws IOException {
		String snapshotDirPath = "src/test/resources/snapshots";
		String resultsPath = "src/test/resources/results.properties";
		InputStream resultsStream = new FileInputStream(new File(resultsPath));

		Properties properties = new Properties();
		properties.load(resultsStream);
		resultsStream.close();
		assertTrue(properties.size() > 0);

		File snapshotDir = new File(snapshotDirPath);
		File[] snapshots = snapshotDir.listFiles();
		Collection<Object[]> dataForOneImage = new ArrayList<Object[]>();
		for (File file : snapshots) {
			String name = file.getName();
			String plateExpected = properties.getProperty(name);
			dataForOneImage.add(new Object[] { file, plateExpected });
		}
		return dataForOneImage;
	}

	@Test
	public void testWithParams() throws FileNotFoundException, IOException {
		CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snapshot));
		assertThat(intel.recognize(carSnap), is(equalTo(plateExpected)));
	}

}
