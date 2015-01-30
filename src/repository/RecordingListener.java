package repository;

/**
 * The recording listener interface
 * to get notified when a recording is startet or stopped
 *
 */
public interface RecordingListener {
	void recordingStarted();
	void recordingStopped();
}
