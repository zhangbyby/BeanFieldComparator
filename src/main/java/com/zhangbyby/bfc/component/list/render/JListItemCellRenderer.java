package com.zhangbyby.bfc.component.list.render;

import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.ui.JBColor;
import com.zhangbyby.bfc.component.list.item.JListItemWrapper;
import com.zhangbyby.bfc.ui.BFCMainUI;

import javax.swing.*;
import java.awt.*;

import static com.intellij.icons.AllIcons.Nodes.*;

/**
 * JList组件 元素渲染器
 *
 * @author zhangbyby
 */
public class JListItemCellRenderer implements ListCellRenderer<JListItemWrapper> {
    private final JList<JListItemWrapper> anotherList;
    private final BFCMainUI mainUI;
    private final boolean isTarget;

    public JListItemCellRenderer(JList<JListItemWrapper> anotherList, BFCMainUI mainUI, boolean isTarget) {
        this.anotherList = anotherList;
        this.mainUI = mainUI;
        this.isTarget = isTarget;
    }

    @Override
    public Component getListCellRendererComponent(JList list, JListItemWrapper value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel container = new JLabel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setText(value.getName() + ": " + value.getSimpleTypeText());
        container.setToolTipText(value.getTipText());

        if (!value.isProperty()) {
            container.setIcon(Field);

            PsiModifierList modifierList = value.getPsiField().getModifierList();
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
        } else {
            container.setIcon(Property);
        }

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