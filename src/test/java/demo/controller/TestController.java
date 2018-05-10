package demo.controller;

import demo.model.User;
import com.jfcore.jfinal.base.JfController;
import com.jfcore.web.vo.JsonRtn;
import com.jfcore.web.vo.RtnFactory;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind( controllerKey = "/test")
public class TestController extends JfController {
    
    public void add() {
        String name = getPara("name");

        User user = new User();
        user.set("name", name);
        user.set("inputtime", System.currentTimeMillis());
        user.save();

        renderRtn(new JsonRtn<Object>(1, "新增成功"));
    }
    
    public void query() {
        User user = User.dao.findById(getParaToInt("id"));
        renderRtn(RtnFactory.newSucc(user.getStr("name")));
    }
}
