import jssc.SerialPort;
import jssc.SerialPortException;
import godot.global.GD; // Added GD import here for error logging

class SerialPortHandle {

    SerialPort sp;
    String path;
    // --- NEW FIELD: Buffer to hold incomplete lines ---
    private final StringBuilder buffer = new StringBuilder();
    // --------------------------------------------------

    // Constructor remains the same (sp.openPort() etc.)
    public SerialPortHandle(String path) {
        super();
        this.sp = new SerialPort(path);
        this.path = path;
        try {
            sp.openPort();
            sp.setParams(9600, 8, 1, 0);

            // Flush garbage data on initial open
            while (sp.getInputBufferBytesCount() > 0) {
                sp.readBytes();
            }

        } catch (SerialPortException e) {
            // You could use GD.INSTANCE.print("Error opening serial port: " + e.getMessage()); here
            e.printStackTrace();
        }
    }

    // --- NEW METHOD: Non-Blocking Read ---
    // Reads all available bytes into the internal buffer.
    private void readToBuffer() {
        try {
            int bytesAvailable = sp.getInputBufferBytesCount();
            if (bytesAvailable > 0) {
                byte[] incomingBytes = sp.readBytes(bytesAvailable);
                // Convert bytes to a String and append to the buffer
                buffer.append(new String(incomingBytes));
            }
        } catch (SerialPortException e) {
            GD.INSTANCE.print("Serial Read Error: " + e.getMessage());
        }
    }

    // --- MODIFIED readLine() Method (Non-Blocking Line Extractor) ---
    // Call this from _process(). It reads the internal buffer for a full line.
    public String readLine() {
        // 1. First, read all available data from the port into the buffer.
        readToBuffer();

        // 2. Check the buffer for a newline character ('\n').
        int newlineIndex = buffer.indexOf("\n");

        // If a newline is NOT found, return an empty string immediately.
        if (newlineIndex == -1) {
            return "";
        }

        // 3. If a newline IS found, extract the line (up to and including the \n).
        String line = buffer.substring(0, newlineIndex + 1);

        // 4. Remove the extracted line (and the newline char) from the buffer.
        buffer.delete(0, newlineIndex + 1);

        // 5. Clean up the line (removes '\r' if using CRLF) and return it.
        return line.trim();
    }

    // ... printLine(String s) remains the same ...
}