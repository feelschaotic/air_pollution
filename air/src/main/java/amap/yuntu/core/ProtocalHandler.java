package amap.yuntu.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

public abstract class ProtocalHandler {
	private static final int CONN_TIMEOUT = 10 * 1000;
	private static final int READ_TIMEOUT = 15 * 1000;
	protected String recommandURL = "";

	protected byte[] getProtoBufferRequest() {
		String userdata = getUserJSONString();
		String unencoder = getRequestString(userdata);
		return unencoder.getBytes();
	}

	protected String getRequestString(String userdata) {
		StringBuffer sb = new StringBuffer();
		sb.append("loctype=1");
		sb.append("&tableid=").append(getTableID());
		sb.append("&key=").append(getKEY());
		sb.append("&data=").append(userdata);
		return sb.toString();
	}

	protected abstract String getTableID();

	protected abstract String getKEY();

	protected abstract String getUserJSONString();

	protected String getUrl() {
		return "http://yuntuapi.amap.com/datamanage/data/create";
	}

	public String getData() {
		String result = null;
		result = getDataMayThrow();
		return result;
	}

	private String getDataMayThrow() {
		HttpURLConnection conn = null;
		InputStream input = null;
		String result = null;
		recommandURL = getUrl();
		byte[] entityBytes = getProtoBufferRequest();
		conn = makePostRequest(recommandURL, entityBytes);
		try {
			input = conn.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		result = parseResponse(input);
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
			}
			input = null;
		}
		if (conn != null) {
			conn.disconnect();
			conn = null;

		}
		return result;
	}

	protected String parseResponse(InputStream inputStream) {
		try {
			return new String(inputStreamToByte(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected HttpURLConnection makePostRequest(String path, byte[] entityBytes) {
		if (path == null) {
			return null;
		}
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(CONN_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					String.valueOf(entityBytes.length));
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			OutputStream outStream = conn.getOutputStream();
			outStream.write(entityBytes);
			outStream.flush();
			outStream.close();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			}
		} catch (UnknownHostException e) {
		} catch (MalformedURLException e) {
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		return conn;
	}

	protected byte[] readStream(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = (inStream.read(buffer))) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}
		} catch (IOException e) {
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
		return outStream.toByteArray();
	}

	public byte[] inputStreamToByte(InputStream in) throws IOException {
		int BUFFER_SIZE = 2048;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return outStream.toByteArray();
	}
}
