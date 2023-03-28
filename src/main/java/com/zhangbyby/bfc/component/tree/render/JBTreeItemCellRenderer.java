package com.zhangbyby.bfc.component.tree.render;

import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.ui.JBColor;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.zhangbyby.bfc.component.item.FOPItemWrapper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

import static com.intellij.icons.AllIcons.Nodes.*;

public class JBTreeItemCellRenderer implements TreeCellRenderer {
    private final Tree anotherTree;
    private final boolean isTarget;

    public JBTreeItemCellRenderer(Tree anotherTree, boolean isTarget) {
        this.anotherTree = anotherTree;
        this.isTarget = isTarget;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object node, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
        FOPItemWrapper value = (FOPItemWrapper) treeNode.getUserObject();

        if (value == null) {
            return null;
        }

        JLabel container = new JLabel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setText(value.getFopName() + ": " + value.getActualTypeName());

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

        if (value.isFromSuperType()) {
            JLabel jLabel = new JLabel("↑ " + value.getFopDeclareClass());
            jLabel.setBorder(JBUI.Borders.empty(1, (int) Math.ceil(container.getPreferredSize().getWidth() + 5), 0, 0));
            jLabel.setForeground(JBColor.GRAY);
            container.add(jLabel);
        }

        if (anotherTree == null || anotherTree.getModel() == null) {
            return container;
        }

        DefaultMutableTreeNode anotherRoot = (DefaultMutableTreeNode) anotherTree.getModel().getRoot();
        int i = 0;
        for (; i < anotherRoot.getChildCount(); i++) {
            if (value.fullSame((FOPItemWrapper) ((DefaultMutableTreeNode) anotherRoot.getChildAt(i)).getUserObject())) {
                break;
            }
        }
        if (i == anotherRoot.getChildCount()) {
            container.setForeground(JBColor.RED);
        }


        return container;
    }
}
