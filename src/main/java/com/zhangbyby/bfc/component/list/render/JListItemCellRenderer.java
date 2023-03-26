package com.zhangbyby.bfc.component.list.render;

import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.zhangbyby.bfc.component.list.item.JListItemWrapper;

import javax.swing.*;
import java.awt.*;

import static com.intellij.icons.AllIcons.Nodes.*;

/**
 * JList item render
 *
 * @author zhangbyby
 */
public class JListItemCellRenderer implements ListCellRenderer<JListItemWrapper> {
    private final JList<JListItemWrapper> anotherList;
    private final boolean isTarget;

    public JListItemCellRenderer(JList<JListItemWrapper> anotherList, boolean isTarget) {
        this.anotherList = anotherList;
        this.isTarget = isTarget;
    }

    @Override
    public Component getListCellRendererComponent(JList list, JListItemWrapper value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel container = new JLabel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setText(value.getFopName() + ": " + value.getFopReturnType().getPresentableText());

        if (!value.isProperty()) {
            container.setIcon(Field);
            container.setToolTipText(value.getFieldToolTipText());

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
            boolean read = value.getPropertyGetterMethod() != null;
            boolean write = value.getPropertySetterMethod() != null;
            if (read && write) {
                container.setIcon(PropertyReadWrite);
            } else if (read) {
                container.setIcon(PropertyRead);
            } else if (write) {
                container.setIcon(PropertyWrite);
            }
            container.setToolTipText(isTarget ? value.getPropertySetterMethodToolTipText() : value.getPropertyGetterMethodToolTipText());
        }

        int i = 0;
        for (; i < anotherList.getModel().getSize(); i++) {
            if (value.fullSame(anotherList.getModel().getElementAt(i))) {
                break;
            }
        }
        if (i == anotherList.getModel().getSize()) {
            container.setForeground(JBColor.RED);
        }

        if (value.isFromSuperType()) {
            JLabel jLabel = new JLabel("↑ " + value.getFopTypeOwnerType());
            jLabel.setBorder(JBUI.Borders.empty(1, (int) Math.ceil(container.getPreferredSize().getWidth() + 5), 0, 0));
            jLabel.setForeground(JBColor.GRAY);
            container.add(jLabel);
        }

        return container;
    }
}