package test.com.nrchallenge;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.nrchallenge.NRChallenge;

public class NRChallengeTest {

	private static final String INPUT_FOLDER = "src/input/";
	private static final String BLANK_FILE_PATH = INPUT_FOLDER + "blankfile.txt";
	private static final String INVALID_FILE_PATH = INPUT_FOLDER + "invalid.txt";
	private static final String VALID_FILE_PATH = INPUT_FOLDER + "pg2009.txt";

	@Test(expected = IOException.class)
	public void testNoFileFoundException() throws IOException {

		String[] args = new String[] {INVALID_FILE_PATH};
		NRChallenge.TOP_MOST_COUNT = 10;
		for (String file : args) {
			NRChallenge.processFile(file);
		}
	}

	@Test
	public void testEmptyProcessFile() throws IOException {

		Map<String, Integer> threeWordSeqMap = new HashMap<String, Integer>();
		String[] args = new String[] {BLANK_FILE_PATH};
		NRChallenge.TOP_MOST_COUNT = 10;
		for (String file : args) {
			threeWordSeqMap = NRChallenge.processFile(file);
		}
		assertTrue("Map is empty", threeWordSeqMap.isEmpty());
	}
	
	
	@Test
	public void testValidProcessFile() throws IOException {

		Map<String, Integer> threeWordSeqMap = new HashMap<String, Integer>();
		String[] args = new String[] {VALID_FILE_PATH};
		NRChallenge.TOP_MOST_COUNT = 10;
		for (String file : args) {
			threeWordSeqMap = NRChallenge.processFile(file);
		}
		assertTrue("Map is not empty", !threeWordSeqMap.isEmpty());
		assertTrue("File has more than 100 values",threeWordSeqMap.values().size() > 100);
	}
	
	/**
	 * more test cases can be
	 * 1. Verify the output and check if top count matches or not
	 * 2. Verify the output of data with various encoding format
	 * 3.  
	 */
}
