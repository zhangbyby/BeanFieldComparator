package com.zhangbyby.bfc.component.list.render;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static com.intellij.icons.AllIcons.Nodes.Field;

/**
 * JList组件 元素渲染器
 *
 * @author zhangbyby
 */
public class JListPsiFieldCellRenderer implements ListCellRenderer<PsiField> {
    private final JList<PsiField> anotherList;

    public JListPsiFieldCellRenderer(JList<PsiField> anotherList) {
        this.anotherList = anotherList;
    }

    @Override
    public Component getListCellRendererComponent(JList list, PsiField value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel jLabel = new JLabel();
        jLabel.setText(value.getName() + ": " + value.getType().getPresentableText());
        jLabel.setIcon(Field);
        jLabel.setToolTipText(Optional.ofNullable(value.getContainingClass())
                .map(PsiClass::getQualifiedName).orElse("") + "#" + value.getName());

        int i = 0;
        for (; i < anotherList.getModel().getSize(); i++) {
            if (anotherList.getModel().getElementAt(i).getName().equals(value.getName())) {
                break;
            }
        }
        if (i == anotherList.getModel().getSize()) {
            jLabel.setForeground(JBColor.RED);
        }

        return jLabel;
    }
}