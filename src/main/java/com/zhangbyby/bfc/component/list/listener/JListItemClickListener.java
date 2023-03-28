package com.zhangbyby.bfc.component.list.listener;

import com.intellij.pom.Navigatable;
import com.intellij.util.OpenSourceUtil;
import com.zhangbyby.bfc.component.item.FOPItemWrapper;

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
    private final JList<FOPItemWrapper> selfList;
    private final boolean isTarget;

    public JListItemClickListener(JList<FOPItemWrapper> list, boolean isTarget) {
        this.selfList = list;
        this.isTarget = isTarget;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= DOUBLE_CLICK_COUNT) {
            int index = selfList.locationToIndex(e.getPoint());
            FOPItemWrapper itemWrapper = selfList.getModel().getElementAt(index);

            // close dialog
            // BuCPCompareDialogWrapper.getInstance().close(1, true);
            Navigatable navigatable;
            if (itemWrapper.isProperty()) {
                navigatable = isTarget ? itemWrapper.getPropertySetterMethod() : itemWrapper.getPropertyGetterMethod();
            } else {
                navigatable = itemWrapper.getPsiField();
            }
            OpenSourceUtil.navigate(navigatable);
        }
    }
}