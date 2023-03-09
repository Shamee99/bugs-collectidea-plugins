package com.ruijie.plugins.bugscollect.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.ui.EditorTextField;
import com.ruijie.plugins.bugscollect.datacenter.DataCenter;
import com.ruijie.plugins.bugscollect.jdbc.JdbcQuery;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 彭耀煌
 */
public class BugCollectDialog extends DialogWrapper {

    private static final Logger logger = LoggerFactory.getLogger(BugCollectDialog.class);

    private EditorTextField editorTextFieldTitle;

    private EditorTextField editorTextFieldSuggest;

    public BugCollectDialog() {
        super(true);
        init();
        setTitle("添加Bug代码片段信息");
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        editorTextFieldTitle = new EditorTextField("异味代码描述");
        editorTextFieldSuggest = new EditorTextField("异味代码修改建议");
        editorTextFieldSuggest.setPreferredSize(new Dimension(300, 200));
        panel.add(editorTextFieldTitle, BorderLayout.NORTH);
        panel.add(editorTextFieldSuggest, BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("添加到异味代码列表");
        // 按钮点击事件
        addButton.addActionListener(e -> {
            // 获取标题
            String title = editorTextFieldTitle.getText();
            // 获取建议
            String suggest = editorTextFieldSuggest.getText();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            String sql = "INSERT INTO `bugs-collect`.`bugs-collect-info` (title, suggest, code, create_date) VALUES('" + title + "', '" + suggest + "', '" + DataCenter.SELECT_CODE + "', '" + date + "');";
            Connection connection = null;
            Statement statement = null;
            try {
                connection = JdbcQuery.getConnection();
                statement = JdbcQuery.getStatement(connection);
                statement.executeUpdate(sql);



            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            } finally {
                JdbcQuery.relase(connection, statement, null);
            }

            MessageDialogBuilder.yesNo("操作结果", "添加成功").show();
            BugCollectDialog.this.dispose();
        });
        panel.add(addButton);
        String s = "123";
        String b = "234";
        if(s == b){
            // todo...
        }
        return panel;
    }

}
