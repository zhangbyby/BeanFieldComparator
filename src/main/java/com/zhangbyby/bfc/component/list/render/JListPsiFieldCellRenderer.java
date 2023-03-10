package com.zhangbyby.bfc.component.list.render;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static com.intellij.icons.AllIcons.Nodes.*;

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
        JLabel container = new JLabel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        PsiModifierList modifierList = value.getModifierList();
        if (modifierList != null) {
            if (modifierList.hasExplicitModifier(PsiModifier.STATIC)) {
                JLabel staticMarkLabel = new JLabel();
                staticMarkLabel.setIcon(StaticMark);
                container.add(staticMarkLabel);
            }
            if (modifierList.hasExplicitModifier(PsiModifier.FINAL)) {
                JLabel finalMarkLabel = new JLabel();
                finalMarkLabel.setIcon(FinalMark);
                container.add(finalMarkLabel);
            }
        }

        container.setText(value.getName() + ": " + value.getType().getPresentableText());
        container.setIcon(Field);
        container.setToolTipText(Optional.ofNullable(value.getContainingClass())
                .map(PsiClass::getQualifiedName).orElse("") + "#" + value.getName());

        int i = 0;
        for (; i < anotherList.getModel().getSize(); i++) {
            if (anotherList.getModel().getElementAt(i).getName().equals(value.getName())) {
                break;
            }
        }
        if (i == anotherList.getModel().getSize()) {
            container.setForeground(JBColor.RED);
        }

        return container;
    }
}