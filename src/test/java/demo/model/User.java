package demo.model;

import demo.common.Constant;
import com.jfcore.jfinal.base.JfModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(configName = Constant.db_dataSource, tableName = "user_info", pkName = "id")
public class User extends JfModel<User> {
    private static final long serialVersionUID = 1L;
    public static final User dao = new User();

    public boolean isExistBy(String name) {
        String sql = "SELECT 1 FROM `user_info` WHERE `name`=?";
        return dao.findFirst(sql, name)!=null;
    }
}
