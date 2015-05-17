package pl.java.scalatech.retry;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service(value = "exampleService")
@Slf4j
public class ExampleServiceImpl implements ExampleService {

	
	private int times = 0;

	public String sendMail() throws Exception {

		log.info("Sending mail");

		if (times < 4) {
			times++;
			throw new Exception("Retrying mail sending...");
		}

		log.info("Mail sent!");

		return "OK";
	}

	public int getTimes() {
		return times;
	}

}