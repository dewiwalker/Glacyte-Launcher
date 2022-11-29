package net.runelite.launcher.beta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.launcher.SplashScreen;

@Slf4j
public class SplashScreenBeta extends JFrame
{
    private static SplashScreenBeta INSTANCE;
    static final Dimension FRAME_SIZE = new Dimension(600, 350);

    @Getter
    private final MessagePanel messagePanel = new MessagePanel();

    private SplashScreenBeta()
    {
        this.setTitle("Glacyte");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(FRAME_SIZE);
        this.setLayout(new BorderLayout());
        this.setUndecorated(true);
        try {
            this.setIconImage(ImageIO.read(SplashScreen.class.getResourceAsStream("runelite.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(SplashScreenBeta.FRAME_SIZE);

        panel.add(new InfoPanel("df"), BorderLayout.EAST);
        panel.add(messagePanel, BorderLayout.WEST);

        this.setContentPane(panel);
        pack();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void setBarText(final String text)
    {
        final JProgressBar bar = messagePanel.getBar();
        bar.setString(text);
        bar.setStringPainted(text != null);
        bar.revalidate();
        bar.repaint();
    }

    private void setMessage(final String msg, final double value)
    {
        messagePanel.getBarLabel().setText(msg);
        messagePanel.getBar().setMaximum(1000);
        messagePanel.getBar().setValue((int) (value * 1000));
        setBarText(null);

        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    public static void init()
    {
        try
        {
            SwingUtilities.invokeAndWait(() ->
            {
                if (INSTANCE != null)
                {
                    return;
                }

                try
                {
                    INSTANCE = new SplashScreenBeta();
                }
                catch (Exception e)
                {
                    log.warn("Unable to start splash screen", e);
                }
            });
        }
        catch (InterruptedException | InvocationTargetException bs)
        {
            throw new RuntimeException(bs);
        }
    }

    public static void close()
    {
        SwingUtilities.invokeLater(() ->
        {
            if (INSTANCE == null)
            {
                return;
            }

            // The CLOSE_ALL_WINDOWS quit strategy on MacOS dispatches WINDOW_CLOSING events to each frame
            // from Window.getWindows. However, getWindows uses weak refs and relies on gc to remove windows
            // from its list, causing events to get dispatched to disposed frames. The frames handle the events
            // regardless of being disposed and will run the configured close operation. Set the close operation
            // to DO_NOTHING_ON_CLOSE prior to disposing to prevent this.
            INSTANCE.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            INSTANCE.setVisible(false);
            INSTANCE.dispose();
            INSTANCE = null;
        });
    }

    public static void stage(double startProgress, double endProgress, String progressText, int done, int total)
    {
        String progress = done + " / " + total;
        stage(startProgress + ((endProgress - startProgress) * done / total), progressText + " " + progress);
    }

    public static void stage(double startProgress, double endProgress, String progressText, int done, int total, boolean mib)
    {
        String progress;
        if (mib)
        {
            final double Mb = 1000 * 1000;
            progress = String.format("%.1f / %.1f MB", done / Mb, total / Mb);
        }
        else
        {
            progress = done + " / " + total;
        }
        stage(startProgress + ((endProgress - startProgress) * done / total), progressText + " " + progress);
    }

    public static void stage(double overallProgress, String progressText)
    {
        if (INSTANCE != null)
        {
            INSTANCE.setMessage(progressText, overallProgress);
        }
    }

    public static void barMessage(String barMessage)
    {
        if (INSTANCE != null)
        {
            INSTANCE.setMessage(barMessage, 0);
        }
    }

    public static void message(String message)
    {
        if (INSTANCE != null)
        {
            INSTANCE.messagePanel.setMessageContent(message);
        }
    }

    public static List<JButton> addButtons()
    {
        if (INSTANCE != null)
        {
            return INSTANCE.messagePanel.addButtons();
        }

        return null;
    }
}