package de.tototec.utils.functional;

import de.tobiasroeser.lambdatest.testng.FreeSpec;
import static de.tobiasroeser.lambdatest.Expect.*;

public class TryTest extends FreeSpec {
	public TryTest() {

		setExpectFailFast(false);

		test("success", () -> {
			final Try<String> success = Try.success("OK");
			expectTrue(success.isSuccess());
			expectFalse(success.isFailure());
			expectEquals(success.get(), "OK");
		});

		test("failure", () -> {
			final Try<String> failure = Try.failure(new RuntimeException("FAILURE"));
			expectFalse(failure.isSuccess());
			expectTrue(failure.isFailure());
			intercept(RuntimeException.class, "\\QFAILURE\\E", () -> failure.get());
		});

	}
}
