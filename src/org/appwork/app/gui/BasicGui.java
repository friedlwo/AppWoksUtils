package org.appwork.app.gui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.appwork.shutdown.ShutdownController;
import org.appwork.storage.JSonStorage;
import org.appwork.storage.Storage;
import org.appwork.swing.ExtJFrame;
import org.appwork.swing.action.BasicAction;
import org.appwork.swing.components.ExtButton;
import org.appwork.swing.trayicon.AWTrayIcon;
import org.appwork.utils.os.CrossSystem;
import org.appwork.utils.swing.EDTRunner;
import org.appwork.utils.swing.LockPanel;
import org.appwork.utils.swing.WindowManager;
import org.appwork.utils.swing.WindowManager.FrameState;
import org.appwork.utils.swing.dialog.AbstractDialog;

public abstract class BasicGui {

    public static void main(final String[] args) {
        try {
            for (int i = 2; i >= 0; i--) {
                Thread.sleep(1000);
                System.out.println(i);
            }
        } catch (final InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        new EDTRunner() {

            @Override
            protected void runInEDT() {

                final BasicGui bg = new BasicGui("Test") {

                    @Override
                    protected void layoutPanel() {

                        final ExtButton bt;
                        this.getFrame().add(bt = new ExtButton(new BasicAction(" button") {

                            /**
                             * 
                             */
                            private static final long serialVersionUID = -4007724735998967065L;

                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            for (int i = 2; i >= 0; i--) {
                                                Thread.sleep(1000);
                                                System.out.println(i);
                                            }

                                            getFrame().setAlwaysOnTop(false);
                                            WindowManager.getInstance().setZState(getFrame(), FrameState.TO_FRONT_FOCUSED);
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().setZState(getFrame(),
                                            // FrameState.TO_FRONT_FOCUSED);
                                            //
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // Thread.sleep(200);
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame(),
                                            // FrameState.FOCUS);
                                            // Thread.sleep(200);
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame(),
                                            // FrameState.FOCUS);
                                            // Thread.sleep(200);
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame(),
                                            // FrameState.FOCUS);
                                            // Thread.sleep(200);
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame(),
                                            // FrameState.FOCUS);
                                            // Thread.sleep(200);
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame());
                                            // WindowManager.getInstance().toFront(getFrame(),
                                            // FrameState.FOCUS);
                                        } catch (final InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }

                                }.start();

                            }
                        }));

                        this.getFrame().addWindowFocusListener(new WindowFocusListener() {

                            @Override
                            public void windowGainedFocus(final WindowEvent windowevent) {
                                bt.setText("JIPPIE! I Got Focused");

                            }

                            @Override
                            public void windowLostFocus(final WindowEvent windowevent) {
                                bt.setText(" :( No Focus para mi");

                            }
                        });
                    }

                    @Override
                    protected void requestExit() {
                        ShutdownController.getInstance().requestShutdown();

                    }
                };

            }
        };

        try {
            Thread.sleep(1000000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * The Mainframe
     */
    private final JFrame  frame;

    private LockPanel     lockPanel;

    private AWTrayIcon    ti;

    private final Storage storage;

    protected BasicGui(final String title) {

        this.frame = new ExtJFrame(title) {
            /**
             * 
             */
            private static final long serialVersionUID = -8325715174242107194L;
            private final boolean     notToFront       = false;                 ;

            @Override
            public void setVisible(final boolean b) {
                // if we hide a frame which is locked by an active modal dialog,
                // we get in problems. avoid this!
                if (!b) {
                    for (final Window w : this.getOwnedWindows()) {
                        if (w instanceof JDialog && ((JDialog) w).isModal() && w.isActive()) {

                            Toolkit.getDefaultToolkit().beep();
                            throw new ActiveDialogException((JDialog) w);
                        }

                    }
                }

                super.setVisible(b);

            }

            @Override
            public void toFront() {
                // if (notToFront) { return; }
                super.toFront();
            }
        };

        // dilaog init
        this.storage = JSonStorage.getPlainStorage("BasicGui");

        AbstractDialog.setRootFrame(this.frame);

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent arg0) {
                if (!CrossSystem.isMac()) {
                    new Thread("Closer") {
                        @Override
                        public void run() {
                            BasicGui.this.requestExit();

                        }

                    }.start();
                } else {
                    if (BasicGui.this.getFrame().isVisible()) {

                        WindowManager.getInstance().setVisible(BasicGui.this.getFrame(), false);
                    }
                }
            }
        });
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // set appicon

        try {
            this.lockPanel = LockPanel.create(this.frame);
        } catch (final AWTException e1) {

            org.appwork.utils.logging.Log.exception(e1);
        }
        this.frame.setIconImages(this.getAppIconList());
        // Set Application dimensions and locations

        // set extended state

        this.frame.setExtendedState(JSonStorage.getPlainStorage("Interface").get("EXTENDEDSTATE", Frame.NORMAL));
        final Dimension dim = new Dimension(JSonStorage.getPlainStorage("Interface").get("DIMENSION_WIDTH", 1000), JSonStorage.getPlainStorage("Interface").get("DIMENSION_HEIGHT", 600));
        // restore size
        this.frame.setSize(dim);
        this.frame.setPreferredSize(dim);

        this.frame.setMinimumSize(new Dimension(100, 100));
        //

        this.layoutPanel();
        // setGlasPane();

        // restore location. use center of screen as default.
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = screenSize.width / 2 - this.frame.getSize().width / 2;
        final int y = screenSize.height / 2 - this.frame.getSize().height / 2;

        this.frame.setLocation(JSonStorage.getPlainStorage("Interface").get("LOCATION_X", x), JSonStorage.getPlainStorage("Interface").get("LOCATION_Y", y));

        this.frame.pack();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                new EDTRunner() {

                    @Override
                    protected void runInEDT() {
                        WindowManager.getInstance().setZState(BasicGui.this.frame, FrameState.TO_FRONT);

                    }
                };

                try {
                    Thread.sleep(5000);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                new EDTRunner() {

                    @Override
                    protected void runInEDT() {
                        // WindowManager.getInstance().setZState(BasicGui.this.frame,
                        // FrameState.TO_BACK);

                    }
                };
            };

        }.start();
        WindowManager.getInstance().show(this.frame, FrameState.TO_BACK);

        // frame.setLocation(new Point(0,0));
        // frame.setExtendedState(Frame.NORMAL);
        // try {
        // Thread.sleep(5000);
        // } catch (final InterruptedException e) {sky
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // WindowManager.getInstance().toFront(frame);
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // try {
        // Thread.sleep(100);
        // } catch (final InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // try {
        // Thread.sleep(100);
        // } catch (final InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // WindowManager.getInstance().show(frame, FrameState.TO_FRONT);
        // try {
        // Thread.sleep(100);
        // } catch (final InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // WindowManager.getInstance().show(frame, FrameState.FOCUS);

    }

    public void dispose() {
        if (this.frame.getExtendedState() == Frame.NORMAL && this.frame.isShowing()) {

            JSonStorage.getPlainStorage("Interface").put("LOCATION_X", this.frame.getLocationOnScreen().x);
            JSonStorage.getPlainStorage("Interface").put("LOCATION_Y", this.frame.getLocationOnScreen().y);
            JSonStorage.getPlainStorage("Interface").put("DIMENSION_WIDTH", this.frame.getSize().width);
            JSonStorage.getPlainStorage("Interface").put("DIMENSION_HEIGHT", this.frame.getSize().height);

        }

        JSonStorage.getPlainStorage("Interface").put("EXTENDEDSTATE", this.frame.getExtendedState());
        if (this.ti != null) {
            this.ti.dispose();
        }
        WindowManager.getInstance().setVisible(this.frame, false);
        this.frame.dispose();
    }

    /**
     * @return
     */
    protected List<? extends Image> getAppIconList() {
        final java.util.List<Image> list = new ArrayList<Image>();

        return list;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * @return the {@link GUI#lockPanel}
     * @see GUI#lockPanel
     */
    protected LockPanel getLockPanel() {
        return this.lockPanel;
    }

    public Storage getStorage() {
        return this.storage;
    }

    /**
     * Creates the whole mainframework panel
     * 
     * @throws IOException
     */
    protected abstract void layoutPanel();

    protected abstract void requestExit();
}
