/*
 * Copyright (C) 2015 Shotaro Uchida <fantom@xmaker.mx>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.adavr.player;

import com.adavr.player.hmd.HMDRenderContextFactory;
import com.adavr.player.example.PictureApplication;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class Launcher {

	public static final String APPLICATION_NAME = "com.adavr.applicationContextName";
	public static final String DEFAULT_APPLICATION = "com.adavr.player.vlc.VLCApplication";
	public static final String HMD_FACTORY_NAME = "com.adavr.hmd.factoryName";
	private static final Options OPTIONS = createOptions();

	public static void main(String[] args) {
		CommandLine cl;
		try {
			cl = new BasicParser().parse(OPTIONS, args);
		} catch (ParseException ex) {
			showUsage();
			return;
		}

		if (cl.hasOption("help")) {
			showUsage();
			return;
		}

		String src = cl.getOptionValue("source");
		
		ApplicationContext appCtx = loadApplicationContext();
		if (appCtx == null) {
			appCtx = new PictureApplication(src);
		}
		appCtx.getMediaContext().setMedia(src);
		
		Application application = new Application(appCtx);
		application.setVSync(cl.hasOption("vsync"));
		try {
			application.setHMDRenderContextFactory(
					loadHMDRenderContextFactory());
		} catch (Exception ignore) { }

		application.run();
	}

	private static void showUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("adavr", OPTIONS);
	}

	private static Options createOptions() {
		Options options = new Options();
		options.addOption(
				OptionBuilder
				.withLongOpt("source")
				.withArgName("file")
				.hasArgs(1)
				.isRequired()
				.withDescription("Select source file")
				.create("s")
		);
		options.addOption(
				OptionBuilder
				.withLongOpt("help")
				.withDescription("Show usage")
				.create("h")
		);
		options.addOption(
				OptionBuilder
				.withLongOpt("vsync")
				.withDescription("Enable Vsync")
				.create()
		);
		return options;
	}
	
	public static HMDRenderContextFactory loadHMDRenderContextFactory() throws Exception {
		Class clazz = Class.forName(System.getProperty(HMD_FACTORY_NAME));
		Object obj = clazz.newInstance();
		if (!(obj instanceof HMDRenderContextFactory)) {
			return null;
		}
		return (HMDRenderContextFactory) obj;
	}
	
	public static ApplicationContext loadApplicationContext() {
		try {
			Class clazz = Class.forName(System.getProperty(APPLICATION_NAME, DEFAULT_APPLICATION));
			return (ApplicationContext) clazz.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}
}
