package pl.java.scalatech.retry;
public interface ExampleService {

	String sendMail() throws Exception;
	
	int getTimes();
}