/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.lang.Integer;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import org.bff.javampd.MPD;
import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.monitor.MPDStandAloneMonitor;
import org.bff.javampd.objects.MPDSong;


/**
 *
 * @author spine
 */
public class main extends javax.swing.JFrame {
    private mpdWrapper wrapper;
    private ArrayList<Collection<MPDSong>> searches;
    private Collection<MPDSong> search;
    private boolean refresh = true;
    private String old_pls = "";
    private ArrayList<String> commands;
    private int com_position = -1;
    private MPD mpd;
    private Boolean error = false;
    private Thread updater;
    /**
     * Creates new form main
     */
    public main() {
        wrapper = new mpdWrapper();
        searches = new ArrayList();
        search = wrapper.getPlaylist();
        search.clear();
        initComponents();
        commands = new ArrayList();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                wrapper.close();
            }
        });
        
        createMonitors();
        refresh();
        
        DefaultCaret c = (DefaultCaret)playlist.getCaret();
        c.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

    }
    
    private Runnable getUpdater() {
        
        Runnable runnable  = 
            new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            };
        
        return runnable;
    }

    private void createMonitors() {
        if(wrapper != null) {
            wrapper.close();
            wrapper = new mpdWrapper();
        }
        
        if(mpd == null) {
            try {
                mpd = new MPD("127.0.0.1", 6600);
            } catch (UnknownHostException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MPDConnectionException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        MPDStandAloneMonitor monitor = new MPDStandAloneMonitor(mpd) {};
        
        PlayerBasicChangeListener pbcl = new PlayerBasicChangeListener() {
            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                SwingUtilities.invokeLater(getUpdater());
            }
        };
        TrackPositionChangeListener tpcl = new TrackPositionChangeListener() {
            @Override
            public void trackPositionChanged(TrackPositionChangeEvent event) {
                SwingUtilities.invokeLater(getUpdater());
            }
        };
        
        monitor.addPlayerChangeListener(pbcl);
        monitor.addTrackPositionChangeListener(tpcl);
        monitor.addMPDErrorListener(new MPDErrorListener() {
            @Override
            public void errorEventReceived(MPDErrorEvent event) {
                System.out.println(event.getMsg());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

                createMonitors();
            }
        });
        updater = new Thread(monitor);
        updater.start();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        now_playing = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        input = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        volume = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        playlist = new javax.swing.JEditorPane();
        browsing = new javax.swing.JLabel();
        time = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JPC");

        now_playing.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        now_playing.setText("jLabel1");

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Status:");

        status.setText("jLabel2");

        input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputActionPerformed(evt);
            }
        });
        input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputFocusLost(evt);
            }
        });
        input.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Volume:");

        volume.setText("jLabel3");

        playlist.setContentType("text/html");
        playlist.setEditable(false);
        playlist.setToolTipText(null);
        playlist.setFocusable(false);
        jScrollPane1.setViewportView(playlist);

        browsing.setText("current");

        time.setText("jLabel3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(input, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(status)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(volume)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 310, Short.MAX_VALUE)
                        .addComponent(time))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(now_playing)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(browsing)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(now_playing)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(status)
                            .addComponent(jLabel2)
                            .addComponent(volume)
                            .addComponent(time)))
                    .addComponent(browsing))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputActionPerformed
        String in = input.getText();
        com_position = -1;
        commands.add(0, in);
        
        for(String com: in.split("; *"))
            execute(com);
        
        if(!error)
            input.setText("");
    }//GEN-LAST:event_inputActionPerformed

    private void execute(String in)  {
        if(in.startsWith(":"))
            in = in.substring(1);
        
        int reps = findReps(in);
        
        if(!in.matches("^[0-9]+$"))
            in = in.replaceAll("^[0-9]+", "");
        
        refresh = true;
        error = false;
        
        for(int i = 0; i < reps; i++) {
            if (in.equals("pause") || in.equals("p") || in.equals("play"))
                wrapper.togglePlay();
            else if (in.equals("stop") || in.equals("s"))
                wrapper.stop();
            else if (in.equals("quit") || in.equals("q")) {
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MPDConnectionException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
            else if (in.equals("prev") || in.equals("previous"))
                wrapper.previous();
            else if (in.equals("next") || in.equals("n"))
                wrapper.next();
            else if (in.equals("delete") || in.equals("x"))
                wrapper.delete();
            else if (in.equals("back") || in.equals("b"))
                goBack();
            else if (in.split(" ")[0].equals("seek") || in.split(" ")[0].equals("sk")) {
                manageSeek(in.split(" ")[1]);
            }
            else if (in.equals("update"))
                wrapper.update();
            else if (in.equals("load") || in.equals("l")) {
                wrapper.setPlaylist(search);
                search.clear();
            }
            else if (in.equals("clear") || in.equals("C")) {
                search.clear();
                wrapper.setPlaylist(search);
            }
            else if (in.equals("playlists") || in.equals("pls")) {
                refresh = false;
                playlists();
            }
            else if (in.split(" ")[0].equals("loadplaylist") || in.split(" ")[0].equals("lpl")) {
                wrapper.loadPlaylist(in.split(" ")[1]);
            }
            else if (in.split(" ")[0].equals("saveplaylist") || in.split(" ")[0].equals("spl")) {
                wrapper.savePlaylist(in.split(" ")[1]);
            }
            else if (in.split(" ")[0].equals("removeplaylist") || in.split(" ")[0].equals("rpl")) {
                wrapper.deletePlaylist(in.split(" ")[1]);
            }
            else if (in.equals("current") || in.equals("c") || in.equals(""))
                search.clear();
            else if (in.equals("add") || in.equals("a")) {
                if(isSearching()) {
                    ArrayList<MPDSong> tracks = getSelection((ArrayList<MPDSong>) search, "-");
                    wrapper.addTracks(tracks);
                }
                else {
                    ArrayList<MPDSong> tracks = getSelection((ArrayList<MPDSong>) wrapper.getPlaylist(), "-");
                    wrapper.addTracks(tracks);
                }
            }
            else if (in.split(" ")[0].equals("add") || in.split(" ")[0].equals("a")) {
                if(isSearching()) {
                    ArrayList<MPDSong> tracks = getSelection((ArrayList<MPDSong>) search, in.split(" ")[1]);
                    wrapper.addTracks(tracks);
                }
                else {
                    ArrayList<MPDSong> tracks = getSelection((ArrayList<MPDSong>) wrapper.getPlaylist(), in.split(" ")[1]);
                    wrapper.addTracks(tracks);
                }
            }
            else if (in.equals("remove") || in.equals("r")) {
                if(!isSearching()) {
                    wrapper.setPlaylist(new ArrayList<MPDSong>());
                }
                else {
                    searches.add(search = new ArrayList<MPDSong>());
                }
            }
            else if (in.split(" ")[0].equals("remove") || in.split(" ")[0].equals("r")) {
                if(!isSearching()) {
                    ArrayList<MPDSong> tracks = getSelection((ArrayList<MPDSong>) wrapper.getPlaylist(), in.split(" ")[1]);
                    wrapper.removeTracks(tracks);
                }
                else {
                    Collection<MPDSong> old = search;
                    ArrayList<MPDSong> tracks = getSelection((ArrayList<MPDSong>) search, in.split(" ")[1]);
                    old.removeAll(tracks);
                    searches.add(search = old);
                }
            }
            else if (in.split(" ")[0].equals("goto") || in.split(" ")[0].equals("g"))
                wrapper.skip(Integer.parseInt(in.split(" ")[1]));
            else if (in.matches("^[0-9]+"))
                wrapper.skip(Integer.parseInt(in));
            else if (in.split(" ")[0].equals("volume") || in.split(" ")[0].equals("v"))
                modify_volume(in.split(" ")[1]);
            else if (in.split(" ", 2)[0].equals("artist"))
                searches.add(search = wrapper.setSearch("artist", in.split(" ", 2)[1]));
            else if (in.split(" ", 2)[0].equals("album"))
                searches.add(search = wrapper.setSearch("album", in.split(" ", 2)[1]));
            else if (in.split(" ", 2)[0].equals("track"))
                searches.add(search = wrapper.setSearch("track", in.split(" ", 2)[1]));
            else if (in.split(" ", 2)[0].equals("file"))
                searches.add(search = wrapper.setSearch("filename", in.split(" ", 2)[1]));
            else if (in.equals("help") || in.equals("h")) {
                refresh = false;
                printHelp();
            }
            else {
                System.err.println("Unrecognised input:" + in);
                error = true;
            }
        }
        
        refresh();
    }
    
    private void inputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputFocusLost

    }//GEN-LAST:event_inputFocusLost

    private void inputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputKeyReleased
        switch(evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(com_position + 1 < commands.size()) {
                    com_position++;
                    input.setText(commands.get(com_position));
                }
                break;
            case KeyEvent.VK_DOWN:
                if(com_position > 0) {
                    com_position--;
                    input.setText(commands.get(com_position));
                }
                else {
                    com_position = -1;
                    input.setText("");
                }
                break;
            case KeyEvent.VK_ESCAPE:
                input.setText("");
        }
    }//GEN-LAST:event_inputKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel browsing;
    private javax.swing.JTextField input;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel now_playing;
    private javax.swing.JEditorPane playlist;
    private javax.swing.JLabel status;
    private javax.swing.JLabel time;
    private javax.swing.JLabel volume;
    // End of variables declaration//GEN-END:variables

    private void refresh() {
        now_playing.setText(wrapper.getNowPlaying());
        status.setText(wrapper.getStatus());
        volume.setText(wrapper.getVolume());
        
        if(wrapper.getNowPlayingSong() != null) {
           long elapsedTime = wrapper.getElepsedTime();
           int ttime = wrapper.getNowPlayingSong().getLength();
           time.setText(formatTime((int)elapsedTime) + "/" + formatTime(ttime));
        }
        else {
             time.setText("--/--");
        }
        
        if(error)
            input.setBackground(new Color(232, 67, 67));
        else
            input.setBackground(Color.white);
        
        if(refresh) {
            if(!isSearching()) {
                browsing.setText("Current");
                printPlaylist(wrapper.getPlaylist());
            }
            else {
                browsing.setText("Searching");
                printPlaylist(search);
            }
        }
    }

    private void printPlaylist(Collection<MPDSong> list) {
        String pls = "";
        
        MPDSong current = wrapper.getNowPlayingSong();
        Boolean highlight = true;
        
        if(current == null)
            highlight = false;
        
        int i = 1;
        for(MPDSong song: list) {
            
            if(highlight) {
                if(current.equals(song))
                    pls += "<b>";
            }
            
            pls += Integer.toString(i)
                    + ". " + song.getArtist().getName()
                    + " - " + song.getAlbum().getName()
                    + " - " + song.getTitle() + "<br>";
            
            if(highlight) {
                if(current.equals(song))
                    pls += "</b>";
            }
            
            i++;
        }

        if(!old_pls.equals(pls)) {
            playlist.setText(pls);
            old_pls = pls;
        }
    }
    
    private boolean isSearching() {
        return !search.isEmpty();
    }
    
    private void printHelp() {
        String text = 
            "<b>JPC HELP</b>" + "<br><table>"
                + "<tr><td><b>" + "Meta controls" + "</b></tr></td>"
                + "<tr><td>" + "[0-9]+x" + "</td><td>" + "Repeat x given times" + "</td></tr>"
                + "<tr><td>" +  "a;b;..z" + "</td><td>" + "Execute a, then b.. z" + "</td></tr>"
                + "<tr><td><b>" + "Playback control" + "</b></tr></td>"
                + "<tr><td>" + "p|play" + "</td><td>" + "Toggle play/pause" + "</td></tr>"
                + "<tr><td>" + "s|stop" + "</td><td>" + "Stop playback" + "</td></tr>"
                + "<tr><td>" + "v|volume +|-{0,1}x" + "</td><td>" + "Modify volume by x. If sign not given, voume set to x exactly." + "</td></tr>"
                + "<tr><td>" + "n|next" + "</td><td>" + "Skip to next" + "</td></tr>"
                + "<tr><td>" + "p|prev" + "</td><td>" + "Skip to previous" + "</td></tr>"
                + "<tr><td>" + "sk|seek x" + "</td><td>" + "Go to time x, where x is format M:SS or S." + "</td></tr>"
                + "<tr><td>" + "(g|goto ){0,1}x" + "</td><td>" + "Skip to track" + "</td></tr>"
                + "<tr><td>" + "x|delete" + "</td><td>" + "Delete playing track" + "</td></tr>"
                + "<tr><td>" + "q|quit" + "</td><td>" + "Quit JPC" + "</td></tr>"
                + "<tr><td><b>" + "Playlist control" + "</b></tr></td>"
                + "<tr><td>" + "track" + "</td><td>" + "Search for a track" + "</td></tr>"
                + "<tr><td>" + "album" + "</td><td>" + "Search for an album" + "</td></tr>"
                + "<tr><td>" + "artist" + "</td><td>" + "Search for an artist" + "</td></tr>"
                + "<tr><td>" + "filename" + "</td><td>" + "Search for a filename" + "</td></tr>"
                + "<tr><td>" + "c|current" + "</td><td>" + "Show current playlist" + "</td></tr>"
                + "<tr><td>" + "l|load" + "</td><td>" + "Load the current search" + "</td></tr>"
                + "<tr><td>" + "a|add" + "</td><td>" + "Add all to current queue" + "</td></tr>"
                + "<tr><td>" + "a|add x" + "</td><td>" + "Add selection x to current queue" + "</td></tr>"
                + "<tr><td>" + "r|remove" + "</td><td>" + "Remove all from viewed queue" + "</td></tr>"
                + "<tr><td>" + "r|remove x" + "</td><td>" + "Remove selection x from viewed queue" + "</td></tr>"
                + "<tr><td>" + "spl|saveplaylist x" + "</td><td>" + "Save the currently playing queue as a x" + "</td></tr>"
                + "<tr><td>" + "lpl|loadplaylist x" + "</td><td>" + "Load previously saved playlist x" + "</td></tr>"
                + "<tr><td>" + "rpl|removeplaylist x" + "</td><td>" + "Delete playlist x" + "</td></tr>"
                + "<tr><td>" + "pls|playlists" + "</td><td>" + "Show all playlists" + "</td></tr>"
                + "<tr><td>" + "C|clear" + "</td><td>" + "Remove all from currently playing" + "</td></tr>"
                + "<tr><td><b>" + "Selections" + "</b></tr></td>"
                + "<tr><td>" + "[0-9]+" + "</td><td>" + "Select a single track" + "</td></tr>"
                + "<tr><td>" + "[0-9]+{0,1}-[0-9]+{0,1}" + "</td><td>" + "Select from first number to second number of playlist, "
                    + "where a number is omitted, largest possible range is assumed" + "</td></tr>"
                + "<tr><td>" + "(selection {0,1},{0,1})+" + "</td><td>" + "Select the given comma seperated tracks" + "</td></tr>"
                + "<tr><td><b>" + "Other" + "</b></tr></td>"
                + "<tr><td>" + "update" + "</td><td>" + "Refresh the database" + "</td></tr>"
                + "<tr><td>" + "help" + "</td><td>" + "Display this message" + "</td></tr>" +
            "</table>";
        old_pls = text;
        playlist.setText(text);
    }
    
    private void goBack() {
        System.out.println(searches.size());
        if(searches.size() > 1)
            wrapper.setPlaylist(searches.remove(searches.size() - 2));
    }

    private int findReps(String in) {
        Matcher matcher = Pattern.compile("^[0-9]+").matcher(in);
        
        if(matcher.find() && !in.matches("^[0-9]+$"))
            return Integer.parseInt(in.substring(matcher.start(), matcher.end()));
        else
            return 1;
    }

    private ArrayList<MPDSong> getSelection(ArrayList<MPDSong> playlist, String string) {
        ArrayList<MPDSong> selection = new ArrayList<MPDSong>();
        
        for(String section: string.split(" {0,1}, {0,1}")) {
            if(section.matches("^[0-9]+$")) {
                selection.add(playlist.get(Integer.parseInt(section) - 1));
            }
            else {
                String[] bounds = section.split(" {0,1}- {0,1}");
                String[] rbounds = {"1", Integer.toString(playlist.size())};
                
                if(bounds.length != 0) {
                    if(!bounds[0].isEmpty())
                        rbounds[0] = bounds[0];

                    if(bounds.length != 1)
                        rbounds[1] = bounds[1];
                }
                
                for(int i = Integer.parseInt(rbounds[0]); i <= Integer.parseInt(rbounds[1]); i++)
                    selection.add(playlist.get(i - 1));
            }
        }
        return selection;
    }

    private String formatTime(int i) {
        int minutes = i / 60;
        int seconds = i % 60;
        String strseconds = Integer.toString(seconds);
        
        if(strseconds.length() < 2)
            strseconds = "0" + strseconds;
        
        return Integer.toString(minutes) + ":" + strseconds;
    }

    private void manageSeek(String string) {
        if(string.contains(":")) {
            String[] sections = string.split(":", 2);
            wrapper.seek((Integer.parseInt(sections[0])*60) + Integer.parseInt(sections[1]));
        }
        else {
            wrapper.seek(Integer.parseInt(string));
        }
    }

    private void playlists() {
        String plss = "";
        int i = 1;
        for(String pls: wrapper.listPlaylists()) {
            plss += Integer.toString(i)
                    + ". " + pls + "<br>";
            i++;
        }
        
        if(plss.isEmpty())
            plss = "<b>No playlists currently saved.</b>";
        
        old_pls = plss;
        playlist.setText(plss);
    }

    private void modify_volume(String str) {
        String old_vols = wrapper.getVolume();
        int vol = 0;
        
        if(!old_vols.equals("mute"))
            vol = Integer.parseInt(old_vols);
        
        if(str.startsWith("+"))
            vol += Integer.parseInt(str.substring(1));
        else if(str.startsWith("-"))
            vol -= Integer.parseInt(str.substring(1));
        else
            vol = Integer.parseInt(str);
        
        wrapper.setVolume(vol);
    }
}