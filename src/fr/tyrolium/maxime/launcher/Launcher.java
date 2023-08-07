package fr.tyrolium.maxime.launcher;

import java.io.File;

import javax.swing.JFrame;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

public class Launcher {

    public static final GameVersion TY_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
    public static final GameInfos TY_INFOS = new GameInfos("Tyrolium", TY_VERSION, new GameTweak[] {GameTweak.FORGE});
    public static final	File TY_DIR = TY_INFOS.getGameDir();

    public static AuthInfos authInfos;
    public static Thread updateThread;

    public static void auth(String username) throws AuthenticationException {
        authInfos = new AuthInfos(username, "X", "X");
    }

    public static void update() throws Exception {
        SUpdate su = new SUpdate("http://tyrolium.fr/s-update-mc/", TY_DIR);
        su.addApplication(new FileDeleter());

        updateThread = new Thread() {
            private int val;
            private int max;

            @Override
            public void run() {
                while(!this.isInterrupted()) {
                    if(BarAPI.getNumberOfFileToDownload() == 0) {
                        LauncherFrame.getInstance().getLauncherPanel().setInfoText("Verification des fichier");
                        continue;
                    }

                    val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
                    max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);

                    LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
                    LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);

                    LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des Fichier " + BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " + Swinger.percentage(val, max) + "%");
                }
            }
        };
        updateThread.start();

        su.start();
        updateThread.interrupt();
    }

    public static void launch() throws LaunchException
    {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(TY_INFOS, GameFolder.BASIC, authInfos);

        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process p = launcher.launch();

        try
        {
            Thread.sleep(5000L);
            LauncherFrame.getInstance().setVisible(false);
            p.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        System.exit(0);
    }

    public static void interruptThread() {
        updateThread.interrupt();
    }
    
}
