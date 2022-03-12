/**
 * 
 */
package io.pratik.models;

/**
 * @author pratikdas
 *
 */
public class LogRecord {
	private String ip;
	private String httpStatus;
	private String url;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "LogRecord [ip=" + ip + ", httpStatus=" + httpStatus + ", url=" + url + "]";
	}
	
	
	

}
