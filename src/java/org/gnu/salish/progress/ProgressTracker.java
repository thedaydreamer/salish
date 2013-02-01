
package org.gnu.salish.progress;

/**
 * Classes that implement this object can track the progress of AUT processing.
 * 
 * @author rschilling
 */
public interface ProgressTracker {

    /**
     * Sets the current status of processing.
     * 
     * @param status an arbitrary string.
     */
    public void setStatus(String status);

    /**
     * Returns the current status
     * 
     * @return expected to return the last value passed to setStatus.
     */
    public String getCurrentStatus();

    // TODO add javadoc.

    public void updateProgressBarMessage(String message);

    public String getCurrentProgressMessage();

    public void updateProgressBarMax(int maxCount);

    public int getCurrentMax();

    public void updateVariableCount(int fieldCount);

    public void fail();

    public void updateProgressBarProgress(int currentProgress);

    public void incrementProgressBarProgress(int incrementAmount);

    public int getCurrentProgress();

}
