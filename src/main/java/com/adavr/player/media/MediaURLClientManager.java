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
package com.adavr.player.media;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class MediaURLClientManager {
	
	private static MediaURLClientManager instance = null;
	
	public static MediaURLClientManager getInstance() {
		if (instance == null) {
			instance = new MediaURLClientManager();
			instance.urlClients.add(new XVClient());
		}
		return instance;
	}
	
	private final ArrayList<MediaURLClient> urlClients = new ArrayList<>();
	
	public MediaURLClient findURLClient(String uri) {
		URL url;
		try {
			url = new URI(uri).toURL();
		} catch (Exception ex) {
			return null;
		}
		for (MediaURLClient client : urlClients) {
			if (client.isSupported(url)) {
				return client;
			}
		}
		return null;
	}
}
