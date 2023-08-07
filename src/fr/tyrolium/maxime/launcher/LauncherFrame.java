package fr.tyrolium.maxime.launcher;

import javax.swing.JFrame;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class    LauncherFrame extends JFrame {

    private static LauncherFrame instance;
    public LauncherPanel launcherPanel;

    public LauncherFrame() {
        this.setTitle("Tyrolium Launcher");
        this.setSize(975, 625);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setIconImage(Swinger.getResource("icon.png"));
        this.setContentPane(launcherPanel = new LauncherPanel());

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    public static void discordRPC(String arg1) {
    	DiscordRPC DiscStatus = DiscordRPC.INSTANCE;
    	String IdApp= "849915439844687893";
    	String IdSteam = "";
    	DiscordEventHandlers DiscHandler = new DiscordEventHandlers();
    	DiscStatus.Discord_Initialize( IdApp, DiscHandler, true, IdSteam);
    	DiscordRichPresence status = new DiscordRichPresence(); 	
    	status.startTimestamp = System.currentTimeMillis() / 1000;
    	status.largeImageKey = "tyrolium_servermc_v2_discord";
    	status.largeImageText = "TYROLIUM SERVEUR MINECRAFT";
    	status.details = "http://tyroserv.fr/";
    	status.state = arg1 + " Joue depuis : ";
    	DiscStatus.Discord_UpdatePresence(status);
    }

    public static void main(String[] args)	{
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/fr/tyrolium/maxime/launcher/ressource/");
        instance = new LauncherFrame();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("----------|Driver [GOOD]|----------");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----------|Driver [ERREUR]|----------");
        }
    }

    public static LauncherFrame getInstance()	{
        return instance;
    }

    public LauncherPanel getLauncherPanel()	{
        return this.launcherPanel;
    }
}
