package com.zhangbyby.bfc.component.list.listener;

import com.zhangbyby.bfc.component.item.FOPItemWrapper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * JList component selection listener
 *
 * @author zhangbyby
 */
public class JListItemSelectionListener implements ListSelectionListener {

    private final JList<FOPItemWrapper> self;
    private final JList<FOPItemWrapper> other;

    public JListItemSelectionListener(JList<FOPItemWrapper> self, JList<FOPItemWrapper> other) {
        this.self = self;
        this.other = other;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!self.hasFocus()) {
            return;
        }

        FOPItemWrapper selectedValue = self.getSelectedValue();

        other.clearSelection();
        for (int i = 0; i < other.getModel().getSize(); i++) {
            if (selectedValue.nameSame(other.getModel().getElementAt(i))) {
                other.addSelectionInterval(i, i);
                break;
            }
        }
    }
}