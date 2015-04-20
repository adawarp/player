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

import com.adavr.http.ClientException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class XVClient extends AbstractMediaURLClient {

	@Override
	public boolean isSupported(URL url) {
		return url.getHost().endsWith("xvideos.com");
	}
	
	@Override
	public String getStreamURL(String url) throws ClientException {
		Map<String, String> flashVars = getFlashVars(url);
		if (flashVars == null) {
			return null;
		}
		String flvUrl = flashVars.get("flv_url");
		try {
			return new URLCodec().decode(flvUrl);
		} catch (DecoderException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private Map<String, String> getFlashVars(String url) throws ClientException {
		client.get(url);
		Document document = client.getJsoupDocument();
		Element player = document.getElementById("player");
		Elements candidates = player.getElementsByAttribute("flashvars");
		if (candidates.isEmpty()) {
			return null;
		}
		return parseFlashVars(candidates.get(0).attr("flashvars"));
	}
}