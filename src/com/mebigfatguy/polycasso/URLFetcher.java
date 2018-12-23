/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2018 MeBigFatGuy.com
 * Copyright 2009-2018 Dave Brosius
 * Inspired by work by Roger Alsing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mebigfatguy.polycasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

/**
 * manages downloading data from a url
 */
public class URLFetcher {

	/**
	 * private to avoid construction of this static access only class
	 */
	private URLFetcher() {
	}

	/**
	 * retrieve arbitrary data found at a specific url - either http or file urls
	 * for http requests, sets the user-agent to mozilla to avoid sites being cranky
	 * about a java sniffer
	 *
	 * @param url       the url to retrieve
	 * @param proxyHost the host to use for the proxy
	 * @param proxyPort the port to use for the proxy
	 * @return a byte array of the content
	 *
	 * @throws IOException the site fails to respond
	 */
	public static byte[] fetchURLData(String url, String proxyHost, int proxyPort) throws IOException {

		try (InputStream is = openStream(url, proxyHost, proxyPort)) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(is, baos);
			return baos.toByteArray();

		} catch (MalformedURLException e) {
			if (url.startsWith("data:")) {
				int commaPos = url.indexOf(",");
				if (commaPos < 0) {
					throw new IOException("Poorly formatted data url");
				}
				String prefix = url.substring("data:".length(), commaPos);
				String[] attributes = prefix.trim().split(";");
				if (attributes.length == 0) {
					throw new IOException("Data url doesn't specify mime type");
				}

				switch (attributes[0]) {
				case "image/jpeg":
				case "image/png":
				case "image/gif":
					String data = url.substring(commaPos + 1);
					return Base64.getDecoder().decode(data);

				default:
					throw new IOException("Unsupported data url mimetype: " + attributes[0]);
				}
			} else {
				throw e;
			}
		}
	}

	private static InputStream openStream(String url, String proxyHost, int proxyPort) throws IOException {
		URL u = new URL(url);
		if (url.startsWith("file://")) {
			return new BufferedInputStream(u.openStream());
		}

		Proxy proxy;
		if (proxyHost != null) {
			proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		} else {
			proxy = Proxy.NO_PROXY;
		}
		HttpURLConnection con = (HttpURLConnection) u.openConnection(proxy);
		con.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6");
		con.addRequestProperty("Accept-Charset", "UTF-8");
		con.addRequestProperty("Accept-Language", "en-US,en");
		con.addRequestProperty("Accept", "text/html,image/*");
		con.setDoInput(true);
		con.setDoOutput(false);
		con.connect();

		return new ConnectionStream(con);
	}

	private static class ConnectionStream extends FilterInputStream {

		private HttpURLConnection connection;

		public ConnectionStream(HttpURLConnection con) throws IOException {
			super(new BufferedInputStream(con.getInputStream()));
			connection = con;
		}

		@Override
		public void close() {
			try {
				super.close();
				connection.disconnect();
			} catch (IOException e) {
			}
		}
	}
}
