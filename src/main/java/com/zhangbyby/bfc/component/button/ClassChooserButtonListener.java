package com.zhangbyby.bfc.component.button;

import com.intellij.ide.util.ClassFilter;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
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
    private final JList<PsiField> fieldList;

    private final JList<PsiField> anotherList;

    public ClassChooserButtonListener(BFCMainUI mainUI, String title, JTextField classPathText, JList<PsiField> fieldList, JList<PsiField> anotherList) {
        this.mainUI = mainUI;
        this.title = title;
        this.classPathText = classPathText;
        this.fieldList = fieldList;
        this.anotherList = anotherList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TreeClassChooser classChooser = TreeClassChooserFactory.getInstance(mainUI.getProject())
                .createWithInnerClassesScopeChooser(title, GlobalSearchScope.allScope(mainUI.getProject()), ClassFilter.ALL, null);
        classChooser.showDialog();
        if (classChooser.getSelected() != null) {
            classPathText.setText(classChooser.getSelected().getQualifiedName());
            fieldList.setListData(PsiClassUtils.filterFields(classChooser.getSelected().getAllFields(), mainUI.getHideStatic(), mainUI.getHideFinal()));
            anotherList.updateUI();
        }
    }
}