package me.hysong.app.screenlock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Application extends JFrame {

    // --- Hardcoded password for unlocking ---
    private static String correctPassword = "";
    private static final int TIMEOUT_LENGTH_SECONDS = 60;
    private static final int TRYOUT_COUNT = 5;

    // Strings
    private static final String PASSWORD_ERROR_MESSAGE = "비밀번호 오류: (%d/%d 회 오류)";
    private static final String PASSWORD_LOCKED_MESSAGE = "입력 잠김 (1회 잠금 해제까지 %d 초 남음)";

    // --- GUI Components ---
    private JPasswordField passwordField;
    private JLabel titleLabel;
    private JLabel errorLabel;

    private static boolean enforcerEnabled = true;
    private static long startTime;
    private static int triedTime = 0;
    private static boolean timeoutTriggered = false;

    public Application() {
        startTime = System.currentTimeMillis();
        // --- 1. Basic Frame Setup ---
        setTitle("Locked");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);

        // --- 2. Full-Screen Setup ---
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(this);

        // --- 3. Content Pane and Layout ---
        Container contentPane = getContentPane();
        contentPane.setBackground(Color.BLACK);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);

        // --- 4. GUI Components ---
        titleLabel = new JLabel("잠김");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        contentPane.add(titleLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.addActionListener(e -> checkPassword());
        contentPane.add(passwordField, gbc);

        errorLabel = new JLabel("비밀번호 오류");
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        contentPane.add(errorLabel, gbc);

        // --- 5. THE FIX: Start the aggressive enforcement thread ---
        startEnforcerThread();

        // Make frame visible and request initial focus
        setVisible(true);
        passwordField.requestFocusInWindow();
    }

    private void checkPassword() {
        String enteredPassword = new String(passwordField.getPassword());
        if (correctPassword.equals(enteredPassword)) {
            enforcerEnabled = false;
            long endTime = System.currentTimeMillis();
            long[] timeElapsed = toHumanReadable(endTime - startTime);
            dispose();
            JOptionPane.showMessageDialog(this, String.format("총 %d시간 %d분 %d초 동안 잠겨있었으며, 총 %d회의 실패한 잠금해제 시도가 있었습니다.", timeElapsed[0], timeElapsed[1], timeElapsed[2], triedTime));
            System.exit(0);
        } else {
            errorLabel.setVisible(true);
            triedTime++;
            errorLabel.setText(String.format(PASSWORD_ERROR_MESSAGE, triedTime, TRYOUT_COUNT));
            passwordField.setText("");
            passwordField.requestFocusInWindow();

            if (TRYOUT_COUNT == 0) return;

            if (TRYOUT_COUNT <= triedTime) timeoutTriggered = true;

            if (timeoutTriggered) asyncTimeout();
        }
    }

    private void asyncTimeout() {
        new Thread(() -> {
            int timeElapsed = 0;
            passwordField.setEnabled(false);
            while (timeElapsed < TIMEOUT_LENGTH_SECONDS) {
                errorLabel.setText(String.format(PASSWORD_LOCKED_MESSAGE, TIMEOUT_LENGTH_SECONDS - timeElapsed));
                timeElapsed += 1;
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            }
            errorLabel.setVisible(false);
            passwordField.setEnabled(true);
        }).start();
    }

    private static long[] toHumanReadable(long totalSeconds) {
        totalSeconds /= 1000;
        long hours = totalSeconds / 3600;
        long remainder = totalSeconds % 3600;
        long minutes = remainder / 60;
        long seconds = remainder % 60;
        return new long[]{hours, minutes, seconds};
    }


    /**
     * This method runs a continuous loop in a background thread.
     * It aggressively brings the window to the front ONLY IF it is not the currently focused window.
     * This prevents Alt-Tab and other focus-stealing actions without interrupting typing.
     */
    private void startEnforcerThread() {
        KeyHook.blockWindowsKey();
        new Thread(() -> {
            try {
                Robot robot = new Robot();
                while (enforcerEnabled) {
                    // A small "nudge" to disrupt the Alt-Tab key release
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyRelease(KeyEvent.VK_ALT);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_WINDOWS);
                    // This is the key condition. We only act if the frame is NOT focused.
                    // This prevents the loop from interfering with the password field when the user is typing.
                    if (!isFocused() || !isVisible()) {
                        // Bring the window to the front
                        toFront();
                        requestFocus();
                        // Re-focus the password field
                        passwordField.requestFocusInWindow();
                    }
                    // A short sleep to prevent 100% CPU usage
                    Thread.sleep(5);
                }
            } catch (AWTException | InterruptedException e) {
                e.printStackTrace();
            }
            KeyHook.unblockWindowsKey();
        }).start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Arrays.stream(args).filter(e -> e.startsWith("pw=")).findFirst().ifPresent(e -> {correctPassword = e.replace("pw=", "");});

        SwingUtilities.invokeLater(Application::new);
    }
}

