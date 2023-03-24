package com.zhangbyby.bfc.component.button;

import com.intellij.ide.util.ClassFilter;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.psi.search.GlobalSearchScope;
import com.zhangbyby.bfc.component.list.item.JListItemWrapper;
import com.zhangbyby.bfc.ui.BFCMainUI;
import com.zhangbyby.bfc.util.PsiClassUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 类选择按钮监听器
 *
 * @author zhangbyby
 */
public class ClassChooserButtonListener implements ActionListener {
    private final BFCMainUI mainUI;
    private final String title;
    private final JTextField classPathText;
    private final JList<JListItemWrapper> fieldList;
    private final JList<JListItemWrapper> anotherList;
    private final boolean isTarget;

    public ClassChooserButtonListener(BFCMainUI mainUI, String title, JTextField classPathText, JList<JListItemWrapper> fieldList, JList<JListItemWrapper> anotherList, boolean isTarget) {
        this.mainUI = mainUI;
        this.title = title;
        this.classPathText = classPathText;
        this.fieldList = fieldList;
        this.anotherList = anotherList;
        this.isTarget = isTarget;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TreeClassChooser classChooser = TreeClassChooserFactory.getInstance(mainUI.getProject())
                .createWithInnerClassesScopeChooser(title, GlobalSearchScope.allScope(mainUI.getProject()), ClassFilter.ALL, null);
        classChooser.showDialog();
        if (classChooser.getSelected() != null) {
            classPathText.setText(classChooser.getSelected().getQualifiedName());
            fieldList.setListData(PsiClassUtils.filterElements(classChooser.getSelected(), mainUI, isTarget));
            anotherList.updateUI();
        }
    }
}