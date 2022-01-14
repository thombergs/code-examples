/**
 * 
 */
package io.pratik.restclient.models;

/**
 * @author pratikdas
 *
 */
public class RestTemplateError {
	private String timestamp;
	private String status;
	private String error;
	private String path;
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

}
