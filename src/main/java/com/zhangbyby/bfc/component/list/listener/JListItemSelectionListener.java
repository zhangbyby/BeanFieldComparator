package com.zhangbyby.bfc.component.list.listener;

import com.intellij.psi.PsiField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * JList组件 元素选择监听器
 *
 * @author zhangbyby
 */
public class JListItemSelectionListener implements ListSelectionListener {

    private final JList<PsiField> self;
    private final JList<PsiField> other;

    public JListItemSelectionListener(JList<PsiField> self, JList<PsiField> other) {
        this.self = self;
        this.other = other;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!self.hasFocus()) {
            return;
        }

        PsiField selectedValue = self.getSelectedValue();

        other.clearSelection();
        for (int i = 0; i < other.getModel().getSize(); i++) {
            if (other.getModel().getElementAt(i).getName().equals(selectedValue.getName())) {
                other.addSelectionInterval(i, i);
                break;
            }
        }
    }
}