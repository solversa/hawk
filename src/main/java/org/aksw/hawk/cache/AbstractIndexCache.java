package org.aksw.hawk.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class AbstractIndexCache {
	public static Logger log = LoggerFactory.getLogger(AbstractIndexCache.class);
	public String charset = "UTF-8";
	public static String cacheLocation;
	public HashMap<String, ArrayList<String>> cache;
	int i = 0;

	public AbstractIndexCache() {
		cacheLocation = new File("indexCache").getAbsolutePath();
		log.debug("cacheLocation: " + cacheLocation);
		readCache();
	}

	/**
	 * Read the cache to a file
	 */
	public void readCache() {
		cache = new HashMap<String, ArrayList<String>>();
		try {
			if (new File(cacheLocation).exists()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cacheLocation), "UTF8"));
				String s = reader.readLine();
				while (s != null) {
					String input = s.split("\t")[0];
					String[] output = s.split("\t")[1].split(";;;");
					cache.put(input, Lists.newArrayList(output));
					s = reader.readLine();
				}
				reader.close();
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Write the cache to a file
	 */
	public void writeCache() {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cacheLocation)));
			for (String input : cache.keySet()) {
				writer.print(input + "\t");
				for (String output : cache.get(input)) {
					writer.print(output + ";;;");
				}
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	public boolean containsKey(String input) {
		return cache.containsKey(input);
	}

	public ArrayList<String> get(String input) {
		return cache.get(input);
	}

	public void put(String input, ArrayList<String> output) {
		cache.put(input, output);
	}

	public int size() {
		return cache.size();
	}

}