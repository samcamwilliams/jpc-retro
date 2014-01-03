/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bff.javampd.MPD;
import org.bff.javampd.MPDDatabase;
import org.bff.javampd.exception.*;
import org.bff.javampd.objects.MPDSong;

/**
 *
 * @author spine
 */
public class mpdWrapper {
    MPD mpd;
    
    public mpdWrapper() {
        try {
            mpd = new MPD("127.0.0.1", 6600);
        } catch (UnknownHostException ex) {
            Logger.getLogger(mpdWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPDConnectionException ex) {
            Logger.getLogger(mpdWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() {
        try {
            mpd.close();
        } catch (MPDConnectionException ex) {
            Logger.getLogger(mpdWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPDResponseException ex) {
            Logger.getLogger(mpdWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Object execute(mpdAction action){
        try {
            Object res = action.execute(mpd);
            
            return res;
        } catch (MPDResponseException ex) {
            Logger.getLogger(mpdWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch(MPDConnectionException e) {
            System.out.println("Error Connecting:"+e.getMessage());
        }
        return null;
    }
    
    public String getNowPlaying() {
        return (String) execute(
            new mpdAction() {
                @Override
                public String execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    MPDSong track = mpd.getMPDPlaylist().getCurrentSong();
                    if (track != null) {
                        return track.getTitle() + " - " + track.getArtist();
                    }
                    else {
                        return "Not Playing";
                    }
                }
            }
        );
    }

    public MPDSong getNowPlayingSong() {
        return (MPDSong) execute(
            new mpdAction() {
                @Override
                public MPDSong execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    return mpd.getMPDPlaylist().getCurrentSong();
                }
            }
        );
    }

    public long getElepsedTime() {
        return (Long) execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    return mpd.getMPDPlayer().getElapsedTime();
                }
            }
        );
    }
    
    public void update() {
        execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDAdminException {
                    mpd.getMPDAdmin().updateDatabase();
                    return new Object();
                }
            }
        );
    }

    public String getStatus() {
        return (String) execute(
            new mpdAction() {
                @Override
                public String execute(MPD mpd) throws MPDConnectionException, MPDResponseException {
                    Collection<String> status = mpd.getStatus();
                    String state = (String) status.toArray()[10];
                    if(state.equals("state: play"))
                        return "Playing";
                    else if(state.equals("state: pause"))
                        return "Paused";
                    else
                        return "Stopped";
                }
            }
        );
    }

    public void seek(final int time) {
        execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    mpd.getMPDPlayer().seek(time);
                    return new Object();
                }
            }
        );
    }
    
    public Collection<MPDSong> setPlaylist(final Collection<MPDSong> list) {
        return (Collection<MPDSong>) execute(
            new mpdAction() {
                @Override
                public Collection<MPDSong> execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    mpd.getMPDPlaylist().clearPlaylist();
                    mpd.getMPDPlaylist().addSongs((List)list);
                    return list;
                }
            }
        );
    }
    
    public List<MPDSong> getPlaylist() {
        return (List<MPDSong>) execute(
            new mpdAction() {
                @Override
                public List<MPDSong> execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    return mpd.getMPDPlaylist().getSongList();
                }
            }
        );
    }

    public void savePlaylist(final String title) {
        execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    mpd.getMPDPlaylist().savePlaylist(title);
                    return new Object();
                }
            }
        );
    }

    public void loadPlaylist(final String title) {
        execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    mpd.getMPDPlaylist().clearPlaylist();
                    mpd.getMPDPlaylist().loadPlaylist(title);
                    return new Object();
                }
            }
        );
    }

    public ArrayList<String> listPlaylists() {
        return (ArrayList<String>) execute(
            new mpdAction() {
                @Override
                public Collection<String> execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDDatabaseException {
                    return mpd.getMPDDatabase().listPlaylists();
                }
            }
        );
    }
    
    public void deletePlaylist(final String title) {
        execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    mpd.getMPDPlaylist().deletePlaylist(title);
                    return new Object();
                }
            }
        );
    }
    
    public String getVolume() {
        return (String) execute(
            new mpdAction() {
                @Override
                public String execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    String volume = Integer.toString(mpd.getMPDPlayer().getVolume());
                    if (volume.equals("0") || volume.equals("-1"))
                        return "mute";
                    else
                        return volume;
                }
            }
        );
    }
    
    public Collection<MPDSong> setSearch(final String search, final String text) {
        return (Collection<MPDSong>) execute(
            new mpdAction() {
                @Override
                public Collection<MPDSong> execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException, MPDDatabaseException {
                    MPDDatabase mpddb = mpd.getMPDDatabase();
                    Collection<MPDSong> list;
                    
                    if(search.equals("artist")) {
                        list = mpddb.searchArtist(text);
                    }
                    else if(search.equals("album")) {
                        list = mpddb.searchAlbum(text);
                    }
                    else if(search.equals("track")) {
                        list = mpddb.searchTitle(text);
                    }
                    else if(search.equals("filename")) {
                        list = mpddb.searchFileName(text);
                    }
                    else {
                        list = mpddb.findAny(text);
                    }
                    return list;
                }
            }
        );
    }

    public void addTracks(final List<MPDSong> list) {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    mpd.getMPDPlaylist().addSongs(list);
                    return new Object();
                }
            }
        );
    }

    public void removeTracks(final List<MPDSong> list) {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException {
                    for(MPDSong track: list)
                        mpd.getMPDPlaylist().removeSong(track);
                    return new Object();
                }
            }
        );
    }

    public void togglePlay() {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    if (getStatus().equals("Playing"))
                        mpd.getMPDPlayer().pause();
                    else
                        mpd.getMPDPlayer().play();
                    return new Object();
                }
            }
        );
    }
    
    public void stop() {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    if (getStatus().equals("Stopped"))
                        mpd.getMPDPlayer().stop(); // BROKEN
                    else
                        mpd.getMPDPlayer().play();
                    return new Object();
                }
            }
        );
    }
    
    public void next() {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    mpd.getMPDPlayer().playNext();
                    return new Object();
                }
            }
        );
    }
    
    public void previous() {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    mpd.getMPDPlayer().playPrev();
                    return new Object();
                }
            }
        );
    }
    
    public void delete() {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    mpd.getMPDPlaylist().removeSong(mpd.getMPDPlayer().getCurrentSong());
                    return new Object();
                }
            }
        );
    }
    
    public void skip(final int track) {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    mpd.getMPDPlayer().seekId(getPlaylist().get(track - 1), 0);
                    return new Object();
                }
            }
        );
    }
    
    public void setVolume(final int volume) {
         execute(
            new mpdAction() {
                @Override
                public Object execute(MPD mpd) throws MPDConnectionException, MPDPlaylistException, MPDPlayerException {
                    mpd.getMPDPlayer().setVolume(volume);
                    return new Object();
                }
            }
        );
    }
}
