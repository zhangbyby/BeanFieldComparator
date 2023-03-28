package com.zhangbyby.bfc.component.tree.listener;

import com.intellij.ui.treeStructure.Tree;
import com.zhangbyby.bfc.component.item.FOPItemWrapper;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class JBTreeSelectionListener implements TreeSelectionListener {
    private final Tree self;
    private final Tree another;

    public JBTreeSelectionListener(Tree self, Tree another) {
        this.self = self;
        this.another = another;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // 只支持一级树结构
        TreePath selectedPath = e.getPath();
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        FOPItemWrapper wrapper = (FOPItemWrapper) lastPathComponent.getUserObject();

        for (int i = 0; i < another.getModel().getChildCount(another.getModel().getRoot()); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) another.getModel().getChild(another.getModel().getRoot(), i);
            FOPItemWrapper childWrapper = (FOPItemWrapper) childNode.getUserObject();
            if (wrapper.nameSame(childWrapper)) {
                another.setSelectionPath(new TreePath(childNode.getPath()));
            }
        }
    }
}
