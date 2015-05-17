package pl.java.scalatech.retry;

import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
public interface RemoteCallService {
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    String call() throws Exception;
}