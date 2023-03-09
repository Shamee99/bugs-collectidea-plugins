package com.ruijie.plugins.bugscollect.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.ruijie.plugins.bugscollect.datacenter.DataCenter;
import com.ruijie.plugins.bugscollect.dialog.BugCollectDialog;

/**
 * @author 彭耀煌
 */
public class EditorBugsPopupAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取到idea编辑界面实例
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        // 获取编辑实例选择模式
        SelectionModel selectionModel = editor.getSelectionModel();
        // 获取选中文本信息
        String selectedText = selectionModel.getSelectedText();
        // 设置数据中心数据
        DataCenter.SELECT_CODE = selectedText;
        // 开启弹窗
        new BugCollectDialog().show();

    }
}
