package com.zhangbyby.bfc.component.button;

import com.intellij.ide.util.ClassFilter;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 类选择按钮监听器
 *
 * @author zhangbyby
 */
public class ClassChooserButtonListener implements ActionListener {
    private final Project project;
    private final String title;
    private final JTextField classPathText;
    private final JList<PsiField> fieldList;

    private final JList<PsiField> anotherList;

    public ClassChooserButtonListener(Project project, String title, JTextField classPathText, JList<PsiField> fieldList, JList<PsiField> anotherList) {
        this.project = project;
        this.title = title;
        this.classPathText = classPathText;
        this.fieldList = fieldList;
        this.anotherList = anotherList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TreeClassChooser classChooser = TreeClassChooserFactory.getInstance(project)
                .createWithInnerClassesScopeChooser(title, GlobalSearchScope.allScope(project), ClassFilter.ALL, null);
        classChooser.showDialog();
        if (classChooser.getSelected() != null) {
            classPathText.setText(classChooser.getSelected().getQualifiedName());
            fieldList.setListData(classChooser.getSelected().getAllFields());
            anotherList.updateUI();
        }
    }
}