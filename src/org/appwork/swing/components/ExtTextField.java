package org.appwork.swing.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.appwork.app.gui.copycutpaste.ContextMenuAdapter;
import org.appwork.utils.StringUtils;
import org.appwork.utils.swing.SwingUtils;

public class ExtTextField extends JTextField implements CaretListener, FocusListener, DocumentListener, ContextMenuAdapter, TextComponentInterface {
    /**
     * 
     */
    private static final long serialVersionUID     = -3625278218179478516L;
    protected Color           defaultColor;
    protected Color           helpColor;

    {
        this.addCaretListener(this);
        this.addFocusListener(this);
        this.defaultColor = this.getForeground();
        this.helpColor = (Color) UIManager.get("TextField.disabledForeground");
        if (this.helpColor == null) {
            this.helpColor = Color.LIGHT_GRAY;
        }
        this.getDocument().addDocumentListener(this);

    }
    protected String          helpText             = null;
    private boolean           setting;
    private boolean           clearHelpTextOnFocus = true;
    private boolean           helperEnabled        = true;

    public void caretUpdate(final CaretEvent arg0) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void changedUpdate(final DocumentEvent e) {
        if (!this.isHelperEnabled()) {
            this.onChanged();
            return;
        }
        if (!this.setting) {
            this.onChanged();
        }
    }

    public void focusGained(final FocusEvent arg0) {
        if (!this.isHelperEnabled()) { return; }
        if (super.getText().equals(this.helpText)) {
            if (this.isClearHelpTextOnFocus()) {
                this.setText("");
            } else {
                this.selectAll();
            }

        }
        this.setForeground(this.defaultColor);

    }

    public void focusLost(final FocusEvent arg0) {
        if (!this.isHelperEnabled()) { return; }
        if (this.getDocument().getLength() == 0 || super.getText().equals(this.helpText)) {
            this.setText(this.helpText);
            this.setForeground(this.helpColor);
        }

    }

    /**
     * @return
     */
    public Color getDefaultColor() {
        // TODO Auto-generated method stub
        return this.defaultColor;
    }

    public Color getHelpColor() {
        return this.helpColor;
    }

    public String getHelpText() {
        return this.helpText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.appwork.app.gui.copycutpaste.ContextMenuAdapter#getPopupMenu(org.
     * appwork.app.gui.copycutpaste.CutAction,
     * org.appwork.app.gui.copycutpaste.CopyAction,
     * org.appwork.app.gui.copycutpaste.PasteAction,
     * org.appwork.app.gui.copycutpaste.DeleteAction,
     * org.appwork.app.gui.copycutpaste.SelectAction)
     */
    @Override
    public JPopupMenu getPopupMenu(final AbstractAction cutAction, final AbstractAction copyAction, final AbstractAction pasteAction, final AbstractAction deleteAction, final AbstractAction selectAction) {
        final JPopupMenu menu = new JPopupMenu();

        menu.add(cutAction);
        menu.add(copyAction);
        menu.add(pasteAction);
        menu.add(deleteAction);
        menu.add(selectAction);
        return menu;
    }

    @Override
    public void replaceSelection(final String content) {
        if (this.isHelperEnabled() && super.getText().equals(this.helpText) && StringUtils.isNotEmpty(content)) {
            super.setText("");
        }
        super.replaceSelection(content);
        this.setForeground(this.defaultColor);
    }

    @Override
    public String getText() {
        String ret = super.getText();
        if (!this.isHelperEnabled()) { return ret; }
        if (ret.equals(this.helpText) && this.getForeground() == this.helpColor) {
            ret = "";
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void insertUpdate(final DocumentEvent e) {
        if (!this.isHelperEnabled()) {
            this.onChanged();
            return;
        }
        if (!this.setting) {
            this.onChanged();
        }
    }

    public boolean isClearHelpTextOnFocus() {
        return this.clearHelpTextOnFocus;
    }

    public boolean isHelperEnabled() {
        return this.helperEnabled;
    }

    /**
     * 
     */
    public void onChanged() {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void removeUpdate(final DocumentEvent e) {
        if (!this.isHelperEnabled()) {
            this.onChanged();
            return;
        }
        if (!this.setting) {
            this.onChanged();
        }
    }

    /**
     * @param b
     */
    public void setClearHelpTextOnFocus(final boolean b) {
        this.clearHelpTextOnFocus = b;

    }

    public void setHelpColor(final Color helpColor) {
        this.helpColor = helpColor;
    }

    public void setHelperEnabled(final boolean helperEnabled) {
        this.helperEnabled = helperEnabled;
    }

    /**
     * @param addLinksDialog_layoutDialogContent_input_help
     */
    public void setHelpText(final String helpText) {
        final String old = this.helpText;
        this.helpText = helpText;
        if (this.getText().length() == 0 || this.getText().equals(old)) {
            this.setText(this.helpText);
            this.setForeground(this.helpColor);
        }
    }

    public void setLabelMode(final boolean b) {
        this.setEditable(!b);
        this.setFocusable(!b);
        this.setBorder(b ? null : new JTextArea().getBorder());
        SwingUtils.setOpaque(this, !b);
    }

    @Override
    public void setText(String t) {
        if (!this.isHelperEnabled()) {
            super.setText(t);
            return;
        }
        if (!this.setting) {
            this.setting = true;
            try {
                if (!this.hasFocus() && this.helpText != null && (t == null || t.length() == 0)) {
                    t = this.helpText;
                }
                super.setText(t);
                if (this.helpText != null) {
                    if (this.helpText.equals(t)) {
                        this.setForeground(this.helpColor);
                    } else {

                        this.setForeground(this.defaultColor);
                    }
                }
            } finally {
                this.setting = false;
                this.onChanged();
            }
        }
    }

}
