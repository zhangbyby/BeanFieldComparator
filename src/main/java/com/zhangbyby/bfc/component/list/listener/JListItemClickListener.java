package com.zhangbyby.bfc.component.list.listener;

import com.intellij.psi.PsiField;
import com.intellij.util.OpenSourceUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * JList组件 元素点击监听器
 *
 * @author zhangbyby
 */
public class JListItemClickListener extends MouseAdapter {
    public static final int DOUBLE_CLICK_COUNT = 2;
    private final JList<PsiField> selfList;

    public JListItemClickListener(JList<PsiField> list) {
        this.selfList = list;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= DOUBLE_CLICK_COUNT) {
            int index = selfList.locationToIndex(e.getPoint());
            PsiField psiField = selfList.getModel().getElementAt(index);

            // 关闭弹窗
            // BuCPCompareDialogWrapper.getInstance().close(1, true);
            OpenSourceUtil.navigate(psiField);
        }
    }
}