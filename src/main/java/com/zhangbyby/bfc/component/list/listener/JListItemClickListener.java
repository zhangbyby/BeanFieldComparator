package com.zhangbyby.bfc.component.list.listener;

import com.intellij.pom.Navigatable;
import com.intellij.util.OpenSourceUtil;
import com.zhangbyby.bfc.component.list.item.JListItemWrapper;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * JList component item click listener
 *
 * @author zhangbyby
 */
public class JListItemClickListener extends MouseAdapter {
    public static final int DOUBLE_CLICK_COUNT = 2;
    private final JList<JListItemWrapper> selfList;
    private final boolean isTarget;

    public JListItemClickListener(JList<JListItemWrapper> list, boolean isTarget) {
        this.selfList = list;
        this.isTarget = isTarget;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= DOUBLE_CLICK_COUNT) {
            int index = selfList.locationToIndex(e.getPoint());
            JListItemWrapper itemWrapper = selfList.getModel().getElementAt(index);

            // close dialog
            // BuCPCompareDialogWrapper.getInstance().close(1, true);
            Navigatable navigatable;
            if (itemWrapper.isProperty()) {
                navigatable = isTarget ? itemWrapper.getSetterMethod() : itemWrapper.getGetterMethod();
            } else {
                navigatable = itemWrapper.getPsiField();
            }
            OpenSourceUtil.navigate(navigatable);
        }
    }
}