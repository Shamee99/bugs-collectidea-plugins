# 该项目为IDEA开发过程中，进行代码CR或常见的一些代码编码问题汇总。通过选中代码片段，右键一键添加集中收集。替代传统手工复制黏贴的工作。

## 1. plugin.xml详情
配置中提前配置了插件详细信息，以及插件对应的行为。
下面的配置信息可以用devkit插件生成，描述了该插件功能添加的位置，菜单等等。
```xml
<action id="BugsCollectPluginId" class="com.shamee.plugins.bugscollect.action.EditorBugsPopupAction"
                text="添加Bug代码" description="右击添加该选中代码记录bug系统">
            <add-to-group group-id="EditorPopupMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="alt B"/>
        </action>
```
下面为完整配置：
```xml
<idea-plugin>
    <id>com.shamee.ide.plugins.bug-collect</id>

    <name>BugsCollect</name>

    <vendor email="xxxxx" url="https://xxxxxx">shamee</vendor>

    <description>
        <![CDATA[
            Common odor codes can be added to the bug collection system with the right mouse button
          ]]>
    </description>

    <depends>com.intellij.modules.platform</depends>

    <extensions defaultExtensionNs="com.intellij">

    </extensions>

    <actions>
        <!-- Add your actions here -->
        <action id="BugsCollectPluginId" class="com.shamee.plugins.bugscollect.action.EditorBugsPopupAction"
                text="添加Bug代码" description="右击添加该选中代码记录bug系统">
            <add-to-group group-id="EditorPopupMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="alt B"/>
        </action>
    </actions>
</idea-plugin>
```
## 3. 设置获取选中代码片段行为类
EditorBugsPopupAction继承了AnAction，并重写actionPerformed方法。作用是获取idea编辑界面选中的文本内容，并且打开信息填写对话框。
```java
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
```
## 4. 新建BugCollectDialog，用于填写异味代码详细信息
BugCollectDialog为代码信息填写的对话框。该对话框绘制了标题填写栏editorTextFieldTitle，建议填写栏editorTextFieldSuggest，以及按钮组件addButton。使用jpanel绘制弹窗布局。
```java
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
            
        });
        panel.add(addButton);
        return panel;
    }
```
## 5. 添加按钮监听
BugCollectDialog底部按钮添加事件监听。当填写完代码信息后点击该按钮，将详细信息，代码片段统一入库。
```java
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
```        
## 6. 数据库操作工具类
```java
public class JdbcQuery {

    static {
        try {
            Class.forName(JdbcConstants.JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取链接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JdbcConstants.JDBC_URL, JdbcConstants.JDBC_USERNAME, JdbcConstants.JDBC_PASSWORD);
    }

    public static Statement getStatement(Connection con) throws SQLException {
        return con.createStatement();
    }

    //释放连接资源
    public static void relase(Connection co, Statement st, ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (co != null) {
            try {
                co.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
```
## 7. 调试看效果
### 1. 选中代码右键，可以看到“添加Bug代码”功能已添加：
![image](https://user-images.githubusercontent.com/24385660/223919107-841619a2-eaf6-4094-8127-39639a1f7fe8.png)

### 2. 点击添加Bug代码：
![image](https://user-images.githubusercontent.com/24385660/223919127-5b574151-55a2-471a-b485-5c41ea0792bf.png)

### 3. 点击添加到异味代码列表，提示操作成功：
![image](https://user-images.githubusercontent.com/24385660/223919136-5c30fbd6-6486-4bc3-a056-b0b934196c06.png)

### 4. 看一眼数据库数据：
![image](https://user-images.githubusercontent.com/24385660/223919149-faa8c09b-df56-4d41-83ff-3e8dca4434ec.png)

## 8. 待处理列表
数据统一收集后，便可以进行数据的分发推送操作处理。
1. 代码推送管理；
2. 每次添加都直接入库，看不到添加的效果；
3. ......
## 9. Gradle打包
点击Tasks -> intellij -> buildPlugin。
![image](https://user-images.githubusercontent.com/24385660/223919308-df0bc694-8d10-4d5b-9ecf-0ae7c77a579d.png)

控制台提示打包完成后，查看工程build-> distributions下，已经生成了该插件压缩包。
![image](https://user-images.githubusercontent.com/24385660/223919321-6a470821-4f7e-45de-aa6b-c2edeb059d83.png)

## 10. 安装试用
File -> Settings -> Plugins。
![image](https://user-images.githubusercontent.com/24385660/223918644-89e83f0b-4436-4a75-9791-519c5defebc5.png)

选择刚打包好的zip就可以直接安装使用啦。
