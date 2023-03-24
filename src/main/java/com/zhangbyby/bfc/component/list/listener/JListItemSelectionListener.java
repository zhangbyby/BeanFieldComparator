package com.zhangbyby.bfc.component.list.listener;

import com.zhangbyby.bfc.component.list.item.JListItemWrapper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * JList component selection listener
 *
 * @author zhangbyby
 */
public class JListItemSelectionListener implements ListSelectionListener {

    private final JList<JListItemWrapper> self;
    private final JList<JListItemWrapper> other;

    public JListItemSelectionListener(JList<JListItemWrapper> self, JList<JListItemWrapper> other) {
        this.self = self;
        this.other = other;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!self.hasFocus()) {
            return;
        }

        JListItemWrapper selectedValue = self.getSelectedValue();

        other.clearSelection();
        for (int i = 0; i < other.getModel().getSize(); i++) {
            if (other.getModel().getElementAt(i).getName().equals(selectedValue.getName())) {
                other.addSelectionInterval(i, i);
                break;
            }
        }
    }
}