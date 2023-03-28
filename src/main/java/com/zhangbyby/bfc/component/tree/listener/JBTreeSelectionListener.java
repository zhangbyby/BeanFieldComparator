package com.zhangbyby.bfc.component.tree.listener;

import com.intellij.ui.treeStructure.Tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class JBTreeSelectionListener implements TreeSelectionListener {
    private final Tree self;
    private final Tree another;

    public JBTreeSelectionListener(Tree self, Tree another) {
        this.self = self;
        this.another = another;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        System.out.println("selection");
    }
}
