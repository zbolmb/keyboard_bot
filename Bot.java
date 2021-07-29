import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Bot {

    // robots for keyboard input
    private static Robot holdRobot;

    // How much time for pressButton() to hold a keyboard input
    private static final int BUTTON_HOLD = 500;

    // Random to generate random delay between skills
    private static Random rand = new Random();

    // the start time and last buff times
    private static long buff;

    // flag to move left or right
    private static boolean left = true;

    // BUFFS holds an array of spells. Helps keep track of each buff and the previous time they were triggered
    private static Spell[] BUFFS = {
            // Kishin lasts for 150 seconds and is the most important for rebuffs.
            new Spell("Kishin", 61, 0, KeyEvent.VK_F1, true),
            // Domain has a 220s CD, but is very very important and does NOT spend mana
            // for failed attempts. So, try more often than is necessary.
            new Spell("Mihile", 90, 0, KeyEvent.VK_F2, false),
            new Spell("Haku", 300, 0, KeyEvent.VK_F3, true),
    };
    private static Spell[] SPELLS = {
            // new Spell("Teleport", 0, 0, KeyEvent.VK_E, true),
            new Spell("Shikigami Haunting", 0, 5, KeyEvent.VK_Q, false),
            new Spell("Tengu", 0, 5, KeyEvent.VK_W, false)
    };
    // How long the bot runs before pausing. This is in case you AFK for too long.
    private static final int RUNTIME_MINUTES = 120;
    // Self-imposed cooldown between buffs. Gives time for mana regen
    private static final int BUFF_CD_SECONDS = 61;

    public static void main(String[] args) throws Exception {
        // This hold robot is used to press and hold keys. This will be used for buffing.
        holdRobot = new Robot();
        holdRobot.setAutoWaitForIdle(true);
        buffUp();
        buff = now();

        while(true) {
            oneLoop();
        }
    }

    private static void oneLoop() {
        if (timeSince(buff) > BUFF_CD_SECONDS * 1000) {
            buffUp();
        }

        pause(rand.nextInt(5) * 1000);
        pressButton(KeyEvent.VK_Q);
        pressButton(KeyEvent.VK_T);
        if(left) {
            pressButton(KeyEvent.VK_LEFT);
        } else {
            pressButton(KeyEvent.VK_RIGHT);
        }
    }

    private static void buffUp() {
        pressButton(KeyEvent.VK_1);
        pressButton(KeyEvent.VK_2);
        pressButton(KeyEvent.VK_3);
    }

    private static void pressButton(int keyEvent, long length) {
        holdRobot.keyPress(keyEvent);
        pause(length);
        holdRobot.keyRelease(keyEvent);
    }

    private static void pressButton(int keyEvent) {
        pressButton(keyEvent, BUTTON_HOLD);
    }

    private static void shortButtonPress(int keyEvent) {
        pressButton(keyEvent, BUTTON_HOLD / 2);
    }

    private static void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {

        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private static long timeSince(long time) {
        return now() - time;
    }

    private static class Spell {
        public final String name;
        // Buff duration and frequency in seconds
        public final int duration;
        public final int freq;
        public final int key;
        public long lastUsed;
        public boolean selected;
        public Spell(String name, int duration, int freq, int key, boolean selected) {
            this.name = name;
            this.duration = duration;
            this.freq = freq;
            this.key = key;
            this.lastUsed = 0;
            this.selected = selected;
        }
    }
}
