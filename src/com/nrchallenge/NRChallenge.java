package com.nrchallenge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * New relic coding challenge
 * The program accepts as arguments a list of one or more file paths (e.g. ./solution.rb file1.txt file2.txt ...).
 * The program also accepts input on stdin (e.g. cat file1.txt | ./solution.rb).
 * The program outputs a list of the 100 most common three word sequences in the text, along with a count of how many times each occurred in the text. For example: 231 - i will not, 116 - i do not, 105 - there is no, 54 - i know not, 37 - i am not …
 * The program ignores punctuation, line endings, and is case insensitive (e.g. “I love\nsandwiches.” should be treated the same as "(I LOVE SANDWICHES!!)")
 * The program is capable of processing large files and runs as fast as possible.
 * The program should be tested. Provide a test file for your solution.
 */
public class NRChallenge {
	public static final String SPACE = " ";
	public static final String NEXT_LINE_CHAR = "\n";
	public static final String FILE_NAME_SPLIT = "%";
	public static int TOP_MOST_COUNT = 100; //can be configured
	public static final String PATTERN = "((\\w+'\\w+)|(\\w+-?\\w+)|(\\w+))";
	public static final boolean PROCESS_FILE_INDIVIDUALLY = false; //configuration to process files together or individually

	public static void main(String[] args) {
		if(args.length > 0) {
			//found arguments
			processFiles(args);
		}
		else {
			// no args found, process scanner input
			System.out.println("Enter file/s path for processing:");
			Scanner scanner = new Scanner(System.in);
			StringBuilder filesStr = new StringBuilder();
			while(scanner.hasNext()) {
				filesStr.append(scanner.next()).append(FILE_NAME_SPLIT); //using % as it is not a valid file name/path
			}
			String[] scannedArgs = filesStr.toString().split(FILE_NAME_SPLIT);
			processFiles(scannedArgs);
			scanner.close();
		}
	}

	/**
	 * Method to process the given set of files and print out the output in desc order
	 * @param files
	 */
	public static void processFiles(String[] files) {
		Map<String, Integer> threeWordSeqMap = new HashMap<>();
		for (String fileName : files) {
			try {
				Map<String, Integer> currMap = (processFile(fileName));
				if(PROCESS_FILE_INDIVIDUALLY) {
					printOutput(currMap);
				}
				else {
					// getting current map and then adding the values if exists in the threeWordSeqMap
					currMap.forEach((k, v) -> threeWordSeqMap.put(k, threeWordSeqMap.getOrDefault(k, 0) + currMap.get(k)));
				}
			} catch (IOException ioe) {
				System.err.println("File not found.");
				ioe.printStackTrace();
			} catch (Exception e) {
				System.err.println("Unexpected Error occurred");
				e.printStackTrace();
			}
		}
		if(threeWordSeqMap.isEmpty() && !PROCESS_FILE_INDIVIDUALLY)
			System.out.println("Empty file found.. nothing to process ");
		else
			printOutput(threeWordSeqMap);

	}

	/**
	 * Method to printout the data in beautified manner
	 * @param threeWordSeqMap
	 */
	public static void printOutput(Map<String, Integer> threeWordSeqMap) {
		//Using comparator class to get the top most sequences
		List<String> topThreeWordSeqList = getTopThreeWordSeqList(threeWordSeqMap);
		System.out.println(String.format("%-30s : %s","Top Three Word Sequence", "Count"));
		for (String topSeq : topThreeWordSeqList) {
			System.out.println(String.format("%-30s : %d",topSeq, threeWordSeqMap.get(topSeq)));
		}
	}
	
	/**
	 * Method to get the get the sorted list by the count using collections sort comparator
	 * @param threeWordSeqMap
	 * @return
	 */

	public static List<String> getTopThreeWordSeqList(Map<String, Integer> threeWordSeqMap) {
		List<String> result = new ArrayList<>();
		for(String word: threeWordSeqMap.keySet()) {
			result.add(word);
		}

		Collections.sort(result, (word1, word2) -> {
			int comparison = threeWordSeqMap.get(word2).compareTo(threeWordSeqMap.get(word1));
			if(comparison == 0)
				return word1.compareTo(word2);
			return comparison;
		});

		return result.size() > 100 ? result.subList(0, TOP_MOST_COUNT) : result;
	}

	public static Map<String, Integer> processFile(String fileName) throws IOException {
		Stream<String> lines = Files.lines( Paths.get(fileName), StandardCharsets.UTF_8 );
		return processFileData(lines);
	}

	/**
	 * Method to process the file data and populate map using the pattern
	 * @param lines
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Integer> processFileData(Stream<String> lines) throws IOException {
		Map<String, Integer> threeWordSeqMap = new HashMap<String, Integer>();

		// Store file data into StringBuilder for processing
		StringBuilder sb = new StringBuilder();
		for( String line : (Iterable<String>) lines::iterator ) {
			sb.append(line).append(NEXT_LINE_CHAR);
		}
		// words from file which match our word pattern
		List<String> matches = new ArrayList<String>();
		Pattern p = Pattern.compile(PATTERN);
		Matcher m = p.matcher(sb.toString());
		while (m.find()) {
			matches.add(m.group().toLowerCase());
		}

		// iterate with three words at a time to add to the map
		for (int i = 0; i < matches.size() - 3; i++) { //starting from zero and processing till the 2nd last element
			StringBuilder threeWordSeq = new StringBuilder();
			//creating three word sequence with space in between
			threeWordSeq.append(matches.get(i)).append(SPACE).append(matches.get(i + 1)).append(SPACE).append(matches.get(i + 2));
			threeWordSeqMap.put(threeWordSeq.toString(), threeWordSeqMap.getOrDefault(threeWordSeq.toString(), 0) + 1);
		}

		return threeWordSeqMap;
	}

}
