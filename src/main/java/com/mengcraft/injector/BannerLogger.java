package com.mengcraft.injector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class BannerLogger {
	private final static BannerLogger LOGGER = new BannerLogger();

	private final File file = new File("injector-banner.log");
	private Writer writer;

	public void log(String in) {
		try {
			this.writer = new FileWriter(file, true);
			this.writer.write(in);
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BannerLogger getLogger() {
		return LOGGER;
	}

}
