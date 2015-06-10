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
import com.adavr.player.vlc.VLCApplication;
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
		
		ApplicationContext appCtx = (ApplicationContext) loadClass(cl.getOptionValue("context"));
		if (appCtx == null) {
			appCtx = new VLCApplication();
		}
		if (cl.hasOption("source")) {
			String src = cl.getOptionValue("source");
			appCtx.getMediaContext().setMedia(src);
		}
		
		Application application = new Application(appCtx);
		application.setVSync(cl.hasOption("vsync"));
		if (cl.hasOption("factory")) {
			String className = cl.getOptionValue("factory");
			HMDRenderContextFactory factory = (HMDRenderContextFactory) loadClass(className);
			if (factory != null) {
				application.setHMDRenderContextFactory(factory);
			}
		}

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
				.withDescription("Select source file")
				.create("s")
		);
		options.addOption(
				OptionBuilder
				.withLongOpt("context")
				.withArgName("class")
				.hasArgs(1)
				.create("c")
		);
		options.addOption(
				OptionBuilder
				.withLongOpt("factory")
				.withArgName("class")
				.hasArgs(1)
				.create("f")
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
	
	public static Object loadClass(String className) {
		try {
			Class clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}
}
