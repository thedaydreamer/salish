package schilling.richard.util;

public final class Duration {

	/**
	 * Number of milliseconds in a second.
	 */
	public static final int SECONDS = 1000;

	/**
	 * Number of milliseconds in a minute.
	 */
	public static final int MINUTES = 60 * SECONDS;

	/**
	 * Number of milliseconds in an hour.
	 */
	public static final int HOURS = 60 * MINUTES;

	/**
	 * Number of milliseconds in a day.
	 */
	public static final int DAYS = HOURS * 24;

	/**
	 * Number of milliseconds in a year.
	 * 
	 */
	public static final int YEARS = DAYS * 365;

	public long days = 0;
	public long hours = 0;
	public long minutes = 0;
	public long seconds = 0;
	public long milliseconds = 0;

	private Duration() {
	}

	/**
	 * Creates a duration object based on a number of elapsed milliseconds.
	 * 
	 * @param elapsed
	 *            the number of milliseconds to calculate the duration from.
	 * @return
	 */
	public final static Duration create(long elapsed) {
		Duration result = new Duration();

		if (elapsed >= DAYS) {
			result.days = elapsed / DAYS;
			elapsed -= (result.days * DAYS);
		}

		if (elapsed >= HOURS) {
			result.hours = elapsed / HOURS;
			elapsed -= (result.hours * HOURS);
		}

		if (elapsed >= MINUTES) {

			result.minutes = elapsed / MINUTES;
			elapsed -= (result.minutes * MINUTES);
		}

		if (elapsed >= SECONDS) {
			result.seconds = elapsed / SECONDS;
			elapsed -= (result.seconds * SECONDS);
		}

		result.milliseconds = elapsed;

		return result;
	}
}
