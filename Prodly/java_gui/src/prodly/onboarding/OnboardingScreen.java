package prodly.onboarding;

import javax.swing.*;

public class OnboardingScreen extends JFrame {

    public OnboardingScreen() {
        setTitle("Your Onboarding Journey");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JProgressBar progress = new JProgressBar();
        progress.setValue(30);

        JList<String> tasks = new JList<>(
            new String[]{"Company Intro", "Tool Setup"}
        );

        add(progress, "North");
        add(new JScrollPane(tasks), "Center");
    }
}
