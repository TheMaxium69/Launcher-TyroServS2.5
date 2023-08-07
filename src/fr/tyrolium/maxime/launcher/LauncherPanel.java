package fr.tyrolium.maxime.launcher;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {

    private Image background = Swinger.getResource("background.png");

    private Saver saver = new Saver(new File(Launcher.TY_DIR, "launcher.properties"));

    public JTextField usernameField = new JTextField(this.saver.get("username"));
    public JPasswordField passwordField = new JPasswordField();

    private STexturedButton connBtn = new STexturedButton(Swinger.getResource("launch.png"));
    private STexturedButton TextCompte = new STexturedButton(Swinger.getResource("TextCompte.png"));
    private STexturedButton TextEmail = new STexturedButton(Swinger.getResource("TextEmail.png"));
    private STexturedButton TextMdp = new STexturedButton(Swinger.getResource("TextMdp.png"));
    private STexturedButton quitBtn = new STexturedButton(Swinger.getResource("quit.png"));
    private STexturedButton hideBtn = new STexturedButton(Swinger.getResource("hide.png"));
    private STexturedButton siteBtn = new STexturedButton(Swinger.getResource("site.png"));
    private STexturedButton shopBtn = new STexturedButton(Swinger.getResource("shop.png"));
    private STexturedButton instaBtn = new STexturedButton(Swinger.getResource("insta.png"));
    private STexturedButton discBtn = new STexturedButton(Swinger.getResource("disc.png"));
    private STexturedButton tweetBtn = new STexturedButton(Swinger.getResource("tweet.png"));
    private STexturedButton icone = new STexturedButton(Swinger.getResource("logo.png"));

    private SColoredBar progressBar = new SColoredBar(new Color(255, 255, 255, 15));
    private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);

    public LauncherPanel() {
        this.setLayout(null);

        usernameField.setForeground(Color.BLACK);
        usernameField.setCaretColor(Color.BLACK);
        usernameField.setFont(usernameField.getFont().deriveFont(20F));
        usernameField.setOpaque(true);
        usernameField.setBorder(null);
        usernameField.setBounds(95, 156, 300, 42);
        this.add(usernameField);

        passwordField.setForeground(Color.BLACK);
        passwordField.setCaretColor(Color.BLACK);
        passwordField.setFont(passwordField.getFont().deriveFont(20F));
        passwordField.setOpaque(true);
        passwordField.setBorder(null);
        passwordField.setBounds(95, 234, 300, 42);
        this.add(passwordField);

        TextCompte.setBounds(156, 80);
        this.add(this.TextCompte);

        TextEmail.setBounds(95, 100);
        this.add(this.TextEmail);

        TextMdp.setBounds(95, 178);
        this.add(this.TextMdp);

        quitBtn.setBounds(920, 7, 45, 45);
        quitBtn.addEventListener(this);
        this.add(quitBtn);

        hideBtn.setBounds(867, 7, 45, 45);
        hideBtn.addEventListener(this);
        this.add(hideBtn);

        connBtn.setBounds(142, 290, 206, 74);
        connBtn.addEventListener(this);
        this.add(connBtn);

        icone.setBounds(137, 380, 216, 151);
        this.add(icone);

        progressBar.setBounds(12, 593, 951, 20);
        this.add(progressBar);

        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(usernameField.getFont());
        infoLabel.setBounds(12, 560, 951, 25);
        this.add(infoLabel);

        siteBtn.setBounds(8, 9, 40, 40);
        siteBtn.addEventListener(this);
        this.add(this.siteBtn);

        shopBtn.setBounds(58, 9, 40, 40);
        shopBtn.addEventListener(this);
        this.add(this.shopBtn);

        instaBtn.setBounds(108, 9, 40, 40);
        instaBtn.addEventListener(this);
        this.add(this.instaBtn);

        discBtn.setBounds(158, 9, 40, 40);
        discBtn.addEventListener(this);
        this.add(this.discBtn);

        tweetBtn.setBounds(208, 9, 40, 40);
        tweetBtn.addEventListener(this);
        this.add(this.tweetBtn);
    }

    public static String findIP(String site, String prefixe, String suffixe) throws Exception
    {
        Scanner sc = new Scanner(new URL(site).openStream());

        while (sc.hasNextLine())
        {
            String line = sc.nextLine();

            int a = line.indexOf(prefixe);
            if (a!=-1)
            {
                int b = line.indexOf(suffixe,a);
                if (b!=-1)
                {
                    sc.close();
                    String ip = line.substring(a+prefixe.length(),b);
                    return line.substring(a+prefixe.length(),b);
                }
            }
        }

        sc.close();
        return null;
    }

    @Override
    public void onEvent (SwingerEvent e){
        Desktop desktop;
        if(e.getSource() == connBtn)	{

            setFieldsEnabled(false);

            if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0) {
                JOptionPane.showMessageDialog(this,  "Erreur, veuillez entrer un e-mail et un mot de passe valides.");
                setFieldsEnabled(true);
                return;
            }
            try {
                String email = usernameField.getText();
                String pass = passwordField.getText();
                String url = "jdbc:mysql://54.37.64.176:3306/user_mc";
                String user = iugiUGIVUYVIVBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9Hfc;
                String passwords = BKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9Hfc;
                String sql = "SELECT * FROM account WHERE email = '"+ email +"'";

                Connection conn = DriverManager.getConnection(url, user, passwords);
                System.out.println("----------|BASE DONNER [GOOD]|----------");
                
                try {

                    Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result = state.executeQuery(sql);
                    if (result.next() == true) {

                        String spass = result.getString("password");
                        if(spass.equals(pass)){
                            setFieldsEnabled(true);
                            String pseudo = result.getString("pseudo");

                            InetAddress ipP = InetAddress.getLocalHost();                                                                             
                            String ip = findIP("http://www.monip.org/","<BR>IP : ","<br>");
                            String sqlip = "INSERT INTO connexion(nb_compte, pseudo, ip_public, ip_priver) VALUES ('"+ result.getString("id") +"', '"+ result.getString("pseudo") +"', '"+ ip +"', '"+ ipP +"')";
                            state.executeUpdate(sqlip);
                            
                            Thread t = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("Launch Discord");
                                        LauncherFrame.discordRPC(pseudo);
                                        Launcher.auth(pseudo);
                                    } catch (AuthenticationException/* | SQLException */e) {
                                        System.out.println("----------------------Erreur Auth-----------------------");
                                    }

                                    try {
                                        Launcher.update();
                                    } catch (Exception e) {
                                        Launcher.interruptThread();
                                        System.out.println("-----------------------Erreur Uptade---------------------");
                                    }

                                    try {
                                        Launcher.launch();
                                    } catch (LaunchException e) {
                                        System.out.println("--------------------Erreur Lancement-----------------------");
                                    }
                                }
                            };
                            t.start();

                        }  else {
                            JOptionPane.showMessageDialog(this,  "Votre mot de passe est incorrecte");
                            setFieldsEnabled(true);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,  "Votre e-mail '"+ email +"' n'est pas dans notre base de donnée");
                        setFieldsEnabled(true);
                        return;
                    }
                    System.out.println("----------|REQUETE [GOOD]|----------");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println("----------|REQUETE [ERREUR]|----------");
                    JOptionPane.showMessageDialog(this,  "Une erreur de requête entre votre ordinateur et notre base de donnée est survenue");
                    setFieldsEnabled(true);
                    return;
                }

            } catch (Exception e3) {
                e3.printStackTrace();
                System.out.println("----------|BASE DONNER [ERREUR]|----------");
                JOptionPane.showMessageDialog(this,  "Une erreur de requête entre votre ordinateur et notre base de donnée est survenue");
                setFieldsEnabled(true);
                return;
            }
        }
        else if (e.getSource() == this.siteBtn) {
            desktop = Desktop.getDesktop();

            try {
                desktop.browse((new URL("http://tyroserv.fr/")).toURI());
            } catch (MalformedURLException var10) {
                var10.printStackTrace();
            } catch (IOException var11) {
                var11.printStackTrace();
            } catch (URISyntaxException var12) {
                var12.printStackTrace();
            }
        }
        else if (e.getSource() == this.shopBtn) {
            desktop = Desktop.getDesktop();

            try {
                desktop.browse((new URL("http://boutique.tyroserv.fr/")).toURI());
            } catch (MalformedURLException var10) {
                var10.printStackTrace();
            } catch (IOException var11) {
                var11.printStackTrace();
            } catch (URISyntaxException var12) {
                var12.printStackTrace();
            }
        }
        else if (e.getSource() == this.instaBtn) {
            desktop = Desktop.getDesktop();

            try {
                desktop.browse((new URL("https://www.instagram.com/tyroliumserver/")).toURI());
            } catch (MalformedURLException var10) {
                var10.printStackTrace();
            } catch (IOException var11) {
                var11.printStackTrace();
            } catch (URISyntaxException var12) {
                var12.printStackTrace();
            }
        }
        else if (e.getSource() == this.discBtn) {
            desktop = Desktop.getDesktop();

            try {
                desktop.browse((new URL("https://discord.gg/mtDx9ceS7n")).toURI());
            } catch (MalformedURLException var10) {
                var10.printStackTrace();
            } catch (IOException var11) {
                var11.printStackTrace();
            } catch (URISyntaxException var12) {
                var12.printStackTrace();
            }
        }
        else if (e.getSource() == this.tweetBtn) {
            desktop = Desktop.getDesktop();

            try {
                desktop.browse((new URL("https://twitter.com/tyrolium")).toURI());
            } catch (MalformedURLException var10) {
                var10.printStackTrace();
            } catch (IOException var11) {
                var11.printStackTrace();
            } catch (URISyntaxException var12) {
                var12.printStackTrace();
            }
        }
        else if(e.getSource() == quitBtn)
            System.exit(0);
        else if(e.getSource() == hideBtn)
            LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
    }
    String crypt = "dpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHm";
    String iugiUGIVUYVIVBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9Hfc= "BA5e@qs&mgqPxM8#";
    String BKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfcBKbdpM758KSEMfcTxFtTGfFKgfdSdSXKKLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9HfciQCcLKsQYXSMB3ctk7BtF6YxnTGfEoSMHmhGDhzD9Hfc = "ABtnJGh?##JXDD9dDE4ecR!9R#nffnmBerrP8r4$";

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawFullsizedImage(graphics, this, background);
    }

    private void setFieldsEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        connBtn.setEnabled(enabled);
    }

    public SColoredBar getProgressBar()	{
        return progressBar;

    }

    public void setInfoText(String text) {
        infoLabel.setText(text);
    }

}